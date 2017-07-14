package com.transsion.json;

public class TypeContext {

    private BasicType basicType;
    private int count;
    private String propertyName;

    public TypeContext(BasicType basicType) {
        this.basicType = basicType;
        count = 0;
    }

    public BasicType getBasicType() {
        return basicType;
    }

    public void setBasicType(BasicType basicType) {
        this.basicType = basicType;
    }

    public boolean isFirst() {
        return 0 != count;
    }

    public void decrement() {
        count = Math.max(count-1, 0);
    }
    
    public void increment() {
        count++;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}
