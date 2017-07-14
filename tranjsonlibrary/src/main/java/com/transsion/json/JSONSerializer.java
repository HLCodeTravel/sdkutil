package com.transsion.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.transsion.json.transformer.Transformer;
import com.transsion.json.transformer.TransformerWrapper;
import com.transsion.json.transformer.TypeTransformerMap;


public class JSONSerializer {

    public final static char[] HEX = "0123456789ABCDEF".toCharArray();

    private final TypeTransformerMap typeTransformerMap;
    private final Map<Path, Transformer> pathTransformerMap = new HashMap<>();
    private final List<PathExpression> pathExpressions = new ArrayList<>();

    private boolean prettyPrint;
    private String rootName;

    public JSONSerializer() {
        this.typeTransformerMap = new TypeTransformerMap(TransformerUtil.getDefaultTypeTransformers());
    }

    public JSONSerializer(TypeTransformerMap defaultTypeTransformers) {
        this.typeTransformerMap = new TypeTransformerMap(defaultTypeTransformers);
    }

    public JSONSerializer prettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
        return this;
    }


    public JSONSerializer rootName(String rootName) {
        this.rootName = rootName;
        return this;
    }

    public String serialize(Object target) {
        return serialize(target, SerializationType.SHALLOW, new StringBuilderOutputHandler(new StringBuilder()));
    }

    public String deepSerialize(Object target) {
        return serialize(target, SerializationType.DEEP, new StringBuilderOutputHandler(new StringBuilder()));
    }


    protected String serialize(Object target, SerializationType serializationType, OutputHandler out) {
        String output = "";
        // initialize context
        JSONContext context = JSONContext.get();
        context.setRootName(rootName);
        context.setPrettyPrint(prettyPrint);
        context.setOut(out);
        context.serializationType(serializationType);
        context.setTypeTransformers(typeTransformerMap);
        context.setPathTransformers(pathTransformerMap);
        context.setPathExpressions(pathExpressions);

        try {
            //initiate serialization of target tree
            String rootName = context.getRootName();
            if (rootName == null || rootName.trim().equals("")) {
                context.transform(target);
            } else {
                context.writeOpenObject();
                context.writeName(rootName);
                context.transform(target);
                context.writeCloseObject();
            }
            output = context.getOut().toString();
        } finally {
            // cleanup context
            JSONContext.cleanup();

        }
        return output;
    }


    public JSONSerializer transform(Transformer transformer, String... fields) {
        transformer = new TransformerWrapper(transformer);
        for (String field : fields) {
            if (field.length() == 0) {
                pathTransformerMap.put(new Path(), transformer);
            } else {
                pathTransformerMap.put(new Path(field.split("\\.")), transformer);
            }
        }
        return this;
    }

    public JSONSerializer transform(Transformer transformer, Class... types) {
        transformer = new TransformerWrapper(transformer);
        for (Class type : types) {
            typeTransformerMap.putTransformer(type, transformer);
        }
        return this;
    }

    protected void addExclude(String field) {
        int index = field.lastIndexOf('.');
        if (index > 0) {
            PathExpression expression = new PathExpression(field.substring(0, index), true);
            if (!expression.isWildcard()) {
                pathExpressions.add(expression);
            }
        }
        pathExpressions.add(new PathExpression(field, false));
    }

    protected void addInclude(String field) {
        pathExpressions.add(new PathExpression(field, true));
    }


    public JSONSerializer exclude(String... fields) {
        for (String field : fields) {
            addExclude(field);
        }
        return this;
    }


    public JSONSerializer include(String... fields) {
        for (String field : fields) {
            addInclude(field);
        }
        return this;
    }

    public void setIncludes(List<String> fields) {
        for (String field : fields) {
            pathExpressions.add(new PathExpression(field, true));
        }
    }


    public void setExcludes(List<String> fields) {
        for (String field : fields) {
            addExclude(field);
        }
    }

}
