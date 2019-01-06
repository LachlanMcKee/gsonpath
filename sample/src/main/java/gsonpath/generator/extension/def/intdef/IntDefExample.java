package gsonpath.generator.extension.def.intdef;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static gsonpath.generator.extension.def.intdef.IntDefExample.VALUE_1;
import static gsonpath.generator.extension.def.intdef.IntDefExample.VALUE_2;

@Retention(RetentionPolicy.SOURCE)
@IntDef({VALUE_1, VALUE_2})
public @interface IntDefExample {
    int VALUE_1 = 1;
    int VALUE_2 = 2;
}
