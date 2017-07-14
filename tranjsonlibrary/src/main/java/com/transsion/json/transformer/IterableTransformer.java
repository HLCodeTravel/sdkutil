package com.transsion.json.transformer;

import com.transsion.json.TypeContext;

public class IterableTransformer extends AbstractTransformer {

    public void transform(Object object) {
        Iterable iterable = (Iterable) object;
        TypeContext typeContext = getContext().writeOpenArray();
        for (Object item : iterable) {
            if (typeContext.isFirst()) getContext().writeComma();
            typeContext.increment();
            getContext().transform(item);
        }
        getContext().writeCloseArray();
    }

}
