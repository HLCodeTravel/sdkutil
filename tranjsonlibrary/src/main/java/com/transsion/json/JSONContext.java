package com.transsion.json;

import com.transsion.json.transformer.Transformer;
import com.transsion.json.transformer.TypeTransformerMap;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSONContext {

    private static final ThreadLocal<JSONContext> context = new ThreadLocal<JSONContext>() {
        protected JSONContext initialValue() {
            return new JSONContext();
        }
    };

    private String rootName;
    private OutputHandler out;
    private boolean prettyPrint = false;
    private final Stack<TypeContext> typeContextStack = new Stack<>();

    private int indent = 0;
    private TypeTransformerMap typeTransformerMap;
    private Map<Path, Transformer> pathTransformerMap;
    private List<PathExpression> pathExpressions;

    private SerializationType serializationType = SerializationType.SHALLOW;

    private ChainedSet visits = new ChainedSet(Collections.EMPTY_SET);
    private final LinkedList<Object> objectStack = new LinkedList<>();

    private final Path path = new Path();

    private boolean commaWritePending;

    public JSONContext() {
    }

    // CONFIGURE SERIALIZATION
    public void serializationType(SerializationType serializationType) {
        this.serializationType = serializationType;
    }
    // CONFIGURE TRANSFORMERS


    public void transform(Object object) {
        Transformer transformer = getPathTransformer();
        if (transformer == null) {
            transformer = getTypeTransformer(object);
        }
        transformer.transform(object);

    }


    public Transformer getTransformer(BeanProperty prop, Object object) throws IllegalAccessException, InstantiationException {
        Transformer transformer = getPathTransformer();
        if (transformer == null) {
            if (prop != null) {
                transformer = prop.getTransformer();
            }
            if (transformer == null) {
                transformer = getTypeTransformer(object);
            }
        }
        return transformer;
    }

    private Transformer getPathTransformer() {
        return pathTransformerMap.get(path);
    }

    private Transformer getTypeTransformer(Object object) {
        return typeTransformerMap.getTransformer(object);
    }


    public void setTypeTransformers(TypeTransformerMap typeTransformerMap) {
        this.typeTransformerMap = typeTransformerMap;
    }


    public void setPathTransformers(Map<Path, Transformer> pathTransformerMap) {
        this.pathTransformerMap = pathTransformerMap;
    }

    // OUTPUT


    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public void pushTypeContext(TypeContext contextEnum) {
        typeContextStack.push(contextEnum);
    }

    public void popTypeContext() {
        typeContextStack.pop();
    }

    public TypeContext peekTypeContext() {
        if (!typeContextStack.isEmpty()) {
            return typeContextStack.peek();
        } else {
            return null;
        }
    }

    public void setOut(OutputHandler out) {
        this.out = out;
    }


    public OutputHandler getOut() {
        return out;
    }


    public void write(String value) {
        commitComma();
        TypeContext currentTypeContext = peekTypeContext();
        if (currentTypeContext != null &&
                currentTypeContext.getBasicType() == BasicType.ARRAY) {
            writeIndent();
        }
        out.write(value);
    }

    public TypeContext writeOpenObject() {
        commitComma();
        if (prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null &&
                    currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        TypeContext typeContext = new TypeContext(BasicType.OBJECT);
        pushTypeContext(typeContext);
        out.write("{");
        if (prettyPrint) {
            indent += 4;
            out.write("\n");
        }
        return typeContext;
    }

    public void writeCloseObject() {
        discardComma();
        if (prettyPrint) {
            out.write("\n");
            indent -= 4;
            writeIndent();
        }
        out.write("}");
        popTypeContext();
    }

    public void writeName(String name) {
        commitComma();
        if (prettyPrint) writeIndent();
        if (name != null)
            writeQuoted(name);
        else
            write("null");
        out.write(":");
        if (prettyPrint) out.write(" ");
    }

    public void writeComma() {
        commaWritePending = true;
    }

    private void discardComma() {
        commaWritePending = false;
    }

    private void commitComma() {
        if (commaWritePending) {
            out.write(",");
            if (prettyPrint) {
                out.write("\n");
            }
            commaWritePending = false;
        }
    }

    public TypeContext writeOpenArray() {
        commitComma();
        if (prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null &&
                    currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        TypeContext typeContext = new TypeContext(BasicType.ARRAY);
        pushTypeContext(typeContext);
        out.write("[");
        if (prettyPrint) {
            indent += 4;
            out.write("\n");
        }
        return typeContext;
    }

    public void writeCloseArray() {
        discardComma();
        if (prettyPrint) {
            out.write("\n");
            indent -= 4;
            writeIndent();
        }
        out.write("]");
        popTypeContext();
    }

    public void writeIndent() {
        for (int i = 0; i < indent; i++) {
            out.write(" ");
        }
    }

    public String alertValueNull(String value) {
        if (value == null || ("null").equals(value)) {
            value = "";
        }
        return value;
    }

    public void writeQuoted(String value) {
        commitComma();
        if (prettyPrint) {
            TypeContext currentTypeContext = peekTypeContext();
            if (currentTypeContext != null &&
                    currentTypeContext.getBasicType() == BasicType.ARRAY) {
                writeIndent();
            }
        }
        out.write("\"");
        int last = 0;
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == '"') {
                last = out.write(value, last, i, "\\u0022");
            } else if (c == '&') {
                last = out.write(value, last, i, "\\u0026");
            } else if (c == '\'') {
                last = out.write(value, last, i, "\\u0027");
            } else if (c == '<') {
                last = out.write(value, last, i, "\\u003c");
            } else if (c == '>') {
                last = out.write(value, last, i, "\\u003e");
            } else if (c == '\\') {
                last = out.write(value, last, i, "\\\\");
            } else if (c == '\b') {
                last = out.write(value, last, i, "\\b");
            } else if (c == '\f') {
                last = out.write(value, last, i, "\\f");
            } else if (c == '\n') {
                last = out.write(value, last, i, "\\n");
            } else if (c == '\r') {
                last = out.write(value, last, i, "\\r");
            } else if (c == '\t') {
                last = out.write(value, last, i, "\\t");
            } else if (Character.isISOControl(c)) {
                last = out.write(value, last, i) + 1;
                unicode(c);
            }
        }
        if (last < value.length()) {
            out.write(value, last, value.length());
        }
        out.write("\"");
    }

    private void unicode(char c) {
        out.write("\\u");
        int n = c;
        for (int i = 0; i < 4; ++i) {
            int digit = (n & 0xf000) >> 12;
            out.write(String.valueOf(JSONSerializer.HEX[digit]));
            n <<= 4;
        }
    }

    // MANAGE CONTEXT


    public static JSONContext get() {
        return context.get();
    }

    /**
     * static moethod to clean up thread when serialization is complete
     */
    public static void cleanup() {
        context.remove();
    }

    // INCLUDE/EXCLUDE METHODS

    public ChainedSet getVisits() {
        return visits;
    }

    public void setVisits(ChainedSet visits) {
        this.visits = visits;
    }

    public LinkedList<Object> getObjectStack() {
        return objectStack;
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public Path getPath() {
        return this.path;
    }

    public void setPathExpressions(List<PathExpression> pathExpressions) {
        this.pathExpressions = pathExpressions;
    }

    public boolean isIncluded(BeanProperty prop) {
        PathExpression expression = matches(pathExpressions);
        if (expression != null) {
            return expression.isIncluded();
        }

        Boolean included = prop.isIncluded();
        if (included != null) {
            return included;
        }

        if (prop.isTransient()) return false;

        if (serializationType == SerializationType.SHALLOW) {
            Class propType = prop.getPropertyType();
            return !(propType.isArray() || Iterable.class.isAssignableFrom(propType) || Map.class.isAssignableFrom(propType));
        } else {
            return true;
        }
    }

    public boolean isIncluded(String key, Object value) {
        PathExpression expression = matches(pathExpressions);
        if (expression != null) {
            return expression.isIncluded();
        }
        String rootName = context.get().getRootName();
        if (value != null &&
                ((serializationType == SerializationType.SHALLOW && (rootName != null && path.length() > 1)) ||
                        (serializationType == SerializationType.SHALLOW && (rootName == null)))) {
            Class type = value.getClass();
            return !(type.isArray() || Iterable.class.isAssignableFrom(type));

        } else {
            return true;
        }
    }

    protected PathExpression matches(List<PathExpression> expressions) {
        for (PathExpression expr : expressions) {
            if (expr.matches(path)) {
                return expr;
            }
        }
        return null;
    }

}
