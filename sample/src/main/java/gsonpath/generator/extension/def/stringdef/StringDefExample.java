package gsonpath.generator.extension.def.stringdef;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static gsonpath.generator.extension.def.stringdef.StringDefExample.VALUE_1;
import static gsonpath.generator.extension.def.stringdef.StringDefExample.VALUE_2;

@Retention(RetentionPolicy.SOURCE)
@StringDef({VALUE_1, VALUE_2})
@interface StringDefExample {
    String VALUE_1 = "1";
    String VALUE_2 = "2";
}
