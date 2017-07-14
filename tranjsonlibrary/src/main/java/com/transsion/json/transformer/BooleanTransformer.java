package com.transsion.json.transformer;

public class BooleanTransformer extends AbstractTransformer {

    public void transform(Object object) {
        getContext().write(((Boolean) object) ? "true" : "false");
    }

}