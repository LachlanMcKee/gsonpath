package gsonpath.annotation;

import com.google.gson.FieldNamingPolicy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnumGsonAdapter {
    FieldNamingPolicy[] fieldNamingPolicy() default {};

    /**
     * Specifies whether a default value needs to be specified.
     * If this is set to false, the processor will throw an error if no default is provided.
     */
    boolean ignoreDefaultValue() default false;

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface DefaultValue {
    }
}
