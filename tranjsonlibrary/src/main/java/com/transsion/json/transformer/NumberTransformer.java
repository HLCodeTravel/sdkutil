package com.transsion.json.transformer;

public class NumberTransformer extends AbstractTransformer {

    public void transform(Object object) {
        if( object instanceof Double && (Double.isInfinite((Double)object) || Double.isNaN((Double)object)) ) {
            getContext().write( "null" );
            return;
        } else if( object instanceof Float && (Float.isInfinite((Float)object) || Float.isNaN((Float)object)) ) {
            getContext().write("null");
            return;
        }
        getContext().write(object.toString());
    }
}
