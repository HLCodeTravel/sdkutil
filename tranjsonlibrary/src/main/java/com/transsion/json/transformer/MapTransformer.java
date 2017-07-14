package com.transsion.json.transformer;

import com.transsion.json.JSONContext;
import com.transsion.json.JSONException;
import com.transsion.json.Path;
import com.transsion.json.TypeContext;

import java.util.Map;

public class MapTransformer extends AbstractTransformer {

    public void transform(Object object) {
        JSONContext context = getContext();
        Path path = context.getPath();
        Map value = (Map) object;

        try {
            TypeContext typeContext = getContext().writeOpenObject();
            for (Object key : value.keySet()) {

                path.enqueue(key != null ? key.toString() : null);

                if (context.isIncluded(key != null ? key.toString() : null, value.get(key))) {

                    Transformer transformer = context.getTransformer(null, value.get(key));


                    if(!(transformer instanceof Inline) || !((Inline)transformer).isInline()) {
                        if (typeContext.isFirst()) getContext().writeComma();
                        typeContext.increment();
                        if( key != null ) {
                            getContext().writeName(key.toString());
                        } else {
                            getContext().writeName(null);
                        }
                    }

                    if( key != null ) {
                        typeContext.setPropertyName(key.toString());
                    } else {
                        typeContext.setPropertyName(null);
                    }

                    transformer.transform(value.get(key));

                }

                path.pop();

            }
            getContext().writeCloseObject();
        } catch( Exception ex ) {
            throw new JSONException(String.format("%s: Error while trying to serialize.", path), ex);
        }
    }

}
