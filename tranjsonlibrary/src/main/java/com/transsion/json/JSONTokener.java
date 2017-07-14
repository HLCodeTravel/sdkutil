package com.transsion.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONTokener {

    private int index;
    private final Reader reader;
    private char lastChar;
    private boolean useLastChar;

    public JSONTokener(Reader reader) {
        this.reader = reader.markSupported() ?
                reader : new BufferedReader(reader);
        this.useLastChar = false;
        this.index = 0;
    }

    public JSONTokener(String s) {
        this(new StringReader(s));
    }


    public void back() throws JSONException {
        if (useLastChar || index <= 0) {
            throw new JSONException("Stepping back two steps is not supported");
        }
        index -= 1;
        useLastChar = true;
    }

    public static int dehexchar(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - ('A' - 10);
        }
        if (c >= 'a' && c <= 'f') {
            return c - ('a' - 10);
        }
        return -1;
    }

    public boolean more() throws JSONException {
        char nextChar = next();
        if (nextChar == 0) {
            return false;
        }
        back();
        return true;
    }

    public char next() throws JSONException {
        if (this.useLastChar) {
            this.useLastChar = false;
            if (this.lastChar != 0) {
                this.index += 1;
            }
            return this.lastChar;
        }
        int c;
        try {
            c = this.reader.read();
        } catch (IOException exc) {
            throw new JSONException(exc);
        }

        if (c <= 0) { // End of stream
            this.lastChar = 0;
            return 0;
        }
        this.index += 1;
        this.lastChar = (char) c;
        return this.lastChar;
    }

    public char next(char c) throws JSONException {
        char n = next();
        if (n != c) {
            throw syntaxError("Expected '" + c + "' and instead saw '" +
                    n + "'");
        }
        return n;
    }


    public String next(int n) throws JSONException {
        if (n == 0) {
            return "";
        }
        char[] buffer = new char[n];
        int pos = 0;
        if (this.useLastChar) {
            this.useLastChar = false;
            buffer[0] = this.lastChar;
            pos = 1;
        }

        try {
            int len;
            while ((pos < n) && ((len = reader.read(buffer, pos, n - pos)) != -1)) {
                pos += len;
            }
        } catch (IOException exc) {
            throw new JSONException(exc);
        }
        this.index += pos;

        if (pos < n) {
            throw syntaxError("Substring bounds error");
        }

        this.lastChar = buffer[n - 1];
        return new String(buffer);
    }

    public char nextClean() throws JSONException {
        for (; ; ) {
            char c = next();
            if (c == 0 || c > ' ') {
                return c;
            }
        }
    }

    public String nextString(char quote) throws JSONException {
        char c;
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            c = next();
            switch (c) {
                case 0:
                case '\n':
                case '\r':
                    throw syntaxError("Unterminated string");
                case '\\':
                    c = next();
                    switch (c) {
                        case 'b':
                            sb.append('\b');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 'u':
                            sb.append((char) Integer.parseInt(next(4), 16));
                            break;
                        case 'x':
                            sb.append((char) Integer.parseInt(next(2), 16));
                            break;
                        default:
                            sb.append(c);
                    }
                    break;
                default:
                    if (c == quote) {
                        return sb.toString();
                    }
                    sb.append(c);
            }
        }
    }


    public String nextTo(char d) throws JSONException {
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            char c = next();
            if (c == d || c == 0 || c == '\n' || c == '\r') {
                if (c != 0) {
                    back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    public String nextTo(String delimiters) throws JSONException {
        char c;
        StringBuilder sb = new StringBuilder();
        for (; ; ) {
            c = next();
            if (delimiters.indexOf(c) >= 0 || c == 0 ||
                    c == '\n' || c == '\r') {
                if (c != 0) {
                    back();
                }
                return sb.toString().trim();
            }
            sb.append(c);
        }
    }

    public Object nextValue() throws JSONException {
        char c = nextClean();
        String s;

        switch (c) {
            case '"':
            case '\'':
                return nextString(c);
            case '{':
                back();
                return parseObject();
            case '[':
            case '(':
                back();
                return parseArray();
        }
        StringBuilder sb = new StringBuilder();
        while (c >= ' ' && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
            sb.append(c);
            c = next();
        }
        back();

        s = sb.toString().trim();
        if (s.equals("")) {
            throw syntaxError("Missing value");
        }
        return stringToValue(s);
    }

    public char skipTo(char to) throws JSONException {
        char c;
        try {
            int startIndex = this.index;
            reader.mark(Integer.MAX_VALUE);
            do {
                c = next();
                if (c == 0) {
                    reader.reset();
                    this.index = startIndex;
                    return c;
                }
            } while (c != to);
        } catch (IOException exc) {
            throw new JSONException(exc);
        }

        back();
        return c;
    }

    public JSONException syntaxError(String message) {
        return new JSONException(message + toString());
    }

    public String toString() {
        return " at character " + index;
    }

    private Map<String, Object> parseObject() {
        char c;
        String key;

        Map<String, Object> jsonObject = new HashMap<>();

        if (nextClean() != '{') {
            throw syntaxError("A JSONObject text must begin with '{'");
        }
        for (; ; ) {
            c = nextClean();
            switch (c) {
                case 0:
                    throw syntaxError("A JSONObject text must end with '}'");
                case '}':
                    return jsonObject;
                default:
                    back();
                    key = nextValue().toString();
            }
            /*
             * The key is followed by ':'. We will also tolerate '=' or '=>'.
             */
            c = nextClean();
            if (c == '=') {
                if (next() != '>') {
                    back();
                }
            } else if (c != ':') {
                throw syntaxError("Expected a ':' after a key");
            }
            putOnce(jsonObject, key, nextValue());

            /*
             * Pairs are separated by ','. We will also tolerate ';'.
             */
            switch (nextClean()) {
                case ';':
                case ',':
                    if (nextClean() == '}') {
                        return jsonObject;
                    }
                    back();
                    break;
                case '}':
                    return jsonObject;
                default:
                    throw syntaxError("Expected a ',' or '}'");
            }
        }
    }

    private void putOnce(Map<String, Object> jsonObject, String key, Object value) {
        if (key != null) {
            if (!jsonObject.containsKey(key)) {
                jsonObject.put(key, value);
            } else {
                throw new JSONException("Duplicate key \"" + key + "\"");
            }
        }
    }

    public List<Object> parseArray() {
        List<Object> list = new ArrayList<>();

        char c = nextClean();
        char q;
        if (c == '[') {
            q = ']';
        } else if (c == '(') {
            q = ')';
        } else {
            throw syntaxError("A JSONArray text must start with '['");
        }
        if (nextClean() == ']') {
            return list;
        }
        back();
        for (; ; ) {
            if (nextClean() == ',') {
                back();
                list.add(null);
            } else {
                back();
                list.add(nextValue());
            }
            c = nextClean();
            switch (c) {
                case ';':
                case ',':
                    if (nextClean() == ']') {
                        return list;
                    }
                    back();
                    break;
                case ']':
                case ')':
                    if (q != c) {
                        throw syntaxError("Expected a '" + q + "'");
                    }
                    return list;
                default:
                    throw syntaxError("Expected a ',' or ']'");
            }
        }
    }

    private Object stringToValue(String s) {
        if (s.equals("")) {
            return s;
        }
        if (s.equalsIgnoreCase("true")) {
            return Boolean.TRUE;
        }
        if (s.equalsIgnoreCase("false")) {
            return Boolean.FALSE;
        }
        if (s.equalsIgnoreCase("null")) {
            return null;
        }

        if (isNumber(s)) {
            return new JsonNumber(s);
        } else {
            return s;
        }
    }

    private boolean isNumber(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isDigit(c) && c != '-' && c != '.' && c != '+' && c != 'e' && c != 'E') {
                return false;
            }
        }
        return true;
    }
}