package com.transsion.json.transformer;

import com.transsion.json.*;

public class ObjectTransformer extends AbstractTransformer {

    public void transform(Object object) {
        JSONContext context = getContext();
        Path path = context.getPath();
        ChainedSet visits = context.getVisits();
        try {
            if (!visits.contains(object)) {
                context.setVisits(new ChainedSet(visits));
                context.getVisits().add(object);
                // traverse object
                BeanAnalyzer analyzer = BeanAnalyzer.analyze(resolveClass(object));
                TypeContext typeContext = context.writeOpenObject();
                for (BeanProperty prop : analyzer != null ? analyzer.getProperties() : null) {
                    String name = prop.getName();
                    path.enqueue(name);
                    if (context.isIncluded(prop) && prop.isReadable()) {
                        Object value = prop.getValue(object);
                        if (!context.getVisits().contains(value)) {
                            Transformer transformer;
                            transformer = context.getTransformer(prop, value);
                            if (!(transformer instanceof Inline) || !((Inline) transformer).isInline()) {
                                if (typeContext.isFirst()) context.writeComma();
                                typeContext.increment();
                                context.writeName(prop.getJsonName());
                            }
                            typeContext.setPropertyName(prop.getJsonName());

                            transformer.transform(value);
                        }
                    }
                    path.pop();
                }
                context.writeCloseObject();
                context.setVisits((ChainedSet) context.getVisits().getParent());

            } else {
                TypeContext parentTypeContext = getContext().peekTypeContext();
                if (parentTypeContext != null) {
                    parentTypeContext.decrement();
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected Class resolveClass(Object obj) {
        return obj.getClass();
    }

}
