package com.transsion.json;

/**
 * Created by wenshuai.liu on 2017/4/28.
 * ========================================
 * CopyRight (c) 2016 TRANSSION.Co.Ltd.
 * All rights reserved.
 */
/**
 * This is the main class for using Tson.
 *
 * <p>You can create a Tson, custom like FlexJson usage
 * {@link JSONSerializer}s, {@link JSONDeserializer}s.</p>
 *
 * <p>Here is an example of how Tson is used for a simple Class:
 *
 * <pre>
 * MyType target = new MyType();
 * String json = Tson.toJson(target); // serializes target to Json
 * MyType target2 = Tson.fromJson(json, MyType.class); // deserializes json into target2
 * </pre></p>
 */


public class Tson {

    public static String toJson(Object src) {
        return toJson(src,false);
    }
    public static String toJson(Object src,boolean isPretty) {
        if (src == null) {
            throw new IllegalStateException("The Object is Null: ");
        }
        return new JSONSerializer().exclude("*.class").prettyPrint(isPretty).serialize(src);
    }

    public static <T> T fromJson(String src, Class<T> root) {
        if (src == null) {
            throw new IllegalStateException("The Json is Null: ");
        }
        return new JSONDeserializer<T>().deserialize(src, root);
    }
}
