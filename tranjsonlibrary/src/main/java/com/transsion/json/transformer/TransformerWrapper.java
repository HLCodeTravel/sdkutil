package com.transsion.json.transformer;

public class TransformerWrapper extends AbstractTransformer {

    protected final Transformer transformer;

    public TransformerWrapper(Transformer transformer) {
        this.transformer = transformer;
    }

    public void transform(Object object) {
        getContext().getObjectStack().addFirst(object);
        this.transformer.transform(object);
        getContext().getObjectStack().removeFirst();

    }

    @Override
    public Boolean isInline() {
        return transformer instanceof Inline && ((Inline) transformer).isInline();
    }

}
