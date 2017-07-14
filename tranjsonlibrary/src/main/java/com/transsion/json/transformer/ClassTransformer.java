package com.transsion.json.transformer;

public class ClassTransformer extends AbstractTransformer {

    public void transform(Object object) {
        getContext().writeQuoted(((Class) object).getName());
    }

}
