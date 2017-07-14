package com.transsion.json;

public class StringBuilderOutputHandler implements OutputHandler {

    private final StringBuilder out;

    public StringBuilderOutputHandler(StringBuilder out) {
        this.out = out;
    }

    public OutputHandler write(String value) {
        out.append(value);
        return this;
    }

    public int write(String value, int start, int end, String append) {
        out.append( value, start, end );
        out.append( append );
        return end + 1;
    }

    public int write(String value, int start, int end) {
        out.append( value, start, end );
        return end;
    }

    public String toString() {
        return out.toString();
    }
}
