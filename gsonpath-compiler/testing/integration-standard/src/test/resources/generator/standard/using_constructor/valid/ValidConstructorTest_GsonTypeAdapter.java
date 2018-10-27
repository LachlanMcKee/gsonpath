package generator.standard.using_constructor.valid;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class ValidConstructorTest_GsonTypeAdapter extends TypeAdapter<ValidConstructorTest> {
    private final Gson mGson;

    public ValidConstructorTest_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public ValidConstructorTest read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        String value_parent_child_value1 = null;
        boolean value_isBooleanTest1 = false;
        Boolean value_isBooleanTest2 = null;

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 3) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "parent":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter1 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter1 == 1) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "child":
                                jsonFieldCounter1++;

                                // Ensure the object is not null.
                                if (!isValidValue(in)) {
                                    break;
                                }
                                int jsonFieldCounter2 = 0;
                                in.beginObject();

                                while (in.hasNext()) {
                                    if (jsonFieldCounter2 == 1) {
                                        in.skipValue();
                                        continue;
                                    }

                                    switch (in.nextName()) {
                                        case "value1":
                                            jsonFieldCounter2++;

                                            value_parent_child_value1 = mGson.getAdapter(String.class).read(in);
                                            break;

                                        default:
                                            in.skipValue();
                                            break;
                                    }
                                }

                                in.endObject();
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject();
                    break;

                case "isBooleanTest1":
                    jsonFieldCounter0++;

                    value_isBooleanTest1 = mGson.getAdapter(Boolean.class).read(in);
                    break;

                case "isBooleanTest2":
                    jsonFieldCounter0++;

                    value_isBooleanTest2 = mGson.getAdapter(Boolean.class).read(in);
                    break;

                default:
                    in.skipValue();
                    break;
            }
        }

        in.endObject();
        return new ValidConstructorTest(
                value_parent_child_value1,
                value_isBooleanTest1,
                value_isBooleanTest2
        );
    }

    @Override
    public void write(JsonWriter out, ValidConstructorTest value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin parent
        out.name("parent");
        out.beginObject();

        // Begin parentchild
        out.name("child");
        out.beginObject();
        String obj0 = value.getValue1();
        if (obj0 != null) {
            out.name("value1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        // End parentchild
        out.endObject();
        // End parent
        out.endObject();
        boolean obj1 = value.isBooleanTest1();
        out.name("isBooleanTest1");
        mGson.getAdapter(Boolean.class).write(out, obj1);

        Boolean obj2 = value.isBooleanTest2();
        if (obj2 != null) {
            out.name("isBooleanTest2");
            mGson.getAdapter(Boolean.class).write(out, obj2);
        }

        // End
        out.endObject();
    }
}