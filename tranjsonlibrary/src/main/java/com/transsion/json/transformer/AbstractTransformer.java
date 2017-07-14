package com.transsion.json.transformer;

import com.transsion.json.JSONContext;

public abstract class AbstractTransformer implements Transformer, Inline {

    public JSONContext getContext() {
        return JSONContext.get();
    }

    public Boolean isInline() {
        return false;
    }

}
