package com.transsion.json;

import com.transsion.json.transformer.ArrayTransformer;
import com.transsion.json.transformer.BooleanTransformer;
import com.transsion.json.transformer.CharacterTransformer;
import com.transsion.json.transformer.ClassTransformer;
import com.transsion.json.transformer.EnumTransformer;
import com.transsion.json.transformer.IterableTransformer;
import com.transsion.json.transformer.MapTransformer;
import com.transsion.json.transformer.NullTransformer;
import com.transsion.json.transformer.NumberTransformer;
import com.transsion.json.transformer.ObjectTransformer;
import com.transsion.json.transformer.StringTransformer;
import com.transsion.json.transformer.Transformer;
import com.transsion.json.transformer.TransformerWrapper;
import com.transsion.json.transformer.TypeTransformerMap;
import java.util.Arrays;
import java.util.Map;

public class TransformerUtil {

    private static final TypeTransformerMap defaultTransformers = new TypeTransformerMap() {
        {
            // define all standard type transformers
            Transformer transformer = new NullTransformer();
            putTransformer(void.class, new TransformerWrapper(transformer));

            transformer = new ObjectTransformer();
            putTransformer(Object.class, new TransformerWrapper(transformer));

            transformer = new ClassTransformer();
            putTransformer(Class.class, new TransformerWrapper(transformer));

            transformer = new BooleanTransformer();
            putTransformer(boolean.class, new TransformerWrapper(transformer));
            putTransformer(Boolean.class, new TransformerWrapper(transformer));

            transformer = new NumberTransformer();
            putTransformer(Number.class, new TransformerWrapper(transformer));

            putTransformer(Integer.class, new TransformerWrapper(transformer));
            putTransformer(int.class, new TransformerWrapper(transformer));

            putTransformer(Long.class, new TransformerWrapper(transformer));
            putTransformer(long.class, new TransformerWrapper(transformer));

            putTransformer(Double.class, new TransformerWrapper(transformer));
            putTransformer(double.class, new TransformerWrapper(transformer));

            putTransformer(Float.class, new TransformerWrapper(transformer));
            putTransformer(float.class, new TransformerWrapper(transformer));

            transformer = new StringTransformer();
            putTransformer(String.class, new TransformerWrapper(transformer));

            transformer = new CharacterTransformer();
            putTransformer(Character.class, new TransformerWrapper(transformer));
            putTransformer(char.class, new TransformerWrapper(transformer));

            transformer = new EnumTransformer();
            putTransformer(Enum.class, new TransformerWrapper(transformer));

            transformer = new IterableTransformer();
            putTransformer(Iterable.class, new TransformerWrapper(transformer));

            transformer = new MapTransformer();
            putTransformer(Map.class, new TransformerWrapper(transformer));

            transformer = new ArrayTransformer();
            putTransformer(Arrays.class, new TransformerWrapper(transformer));

            locked = true;

        }
    };

    public static TypeTransformerMap getDefaultTypeTransformers() {
        return defaultTransformers;
    }

}
