package gsonpath;

import gsonpath.annotation.AutoGsonAdapterFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AutoGsonAdapterFactory(
        fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL
)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SampleAdapterFactory {
}
