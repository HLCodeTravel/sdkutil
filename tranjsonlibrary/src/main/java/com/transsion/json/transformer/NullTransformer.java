package com.transsion.json.transformer;

public class NullTransformer extends AbstractTransformer {

    public void transform(Object object) {
        getContext().write("null");
    }

}
