package generator.standard.using_constructor.valid;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.GsonUtil;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class ValidConstructorTest_GsonTypeAdapter extends GsonPathTypeAdapter<ValidConstructorTest> {
    public ValidConstructorTest_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public ValidConstructorTest readImpl(JsonReader in) throws IOException {
        String value_parent_child_value1 = null;
        boolean value_isBooleanTest1 = false;
        Boolean value_isBooleanTest2 = null;
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 3)) {
            switch (in.nextName()) {
                case "parent":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "child":
                                while (jsonReaderHelper.handleObject(2, 1)) {
                                    switch (in.nextName()) {
                                        case "value1":
                                            value_parent_child_value1 = gson.getAdapter(String.class).read(in);
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "isBooleanTest1":
                    value_isBooleanTest1 = gson.getAdapter(Boolean.class).read(in);
                    break;

                case "isBooleanTest2":
                    value_isBooleanTest2 = gson.getAdapter(Boolean.class).read(in);
                    break;

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return new ValidConstructorTest(
                value_parent_child_value1,
                value_isBooleanTest1,
                value_isBooleanTest2);
    }

    @Override
    public void writeImpl(JsonWriter out, ValidConstructorTest value) throws IOException {
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
            GsonUtil.writeWithGenericAdapter(gson, obj0.getClass(), out, obj0);
        }

        // End parentchild
        out.endObject();
        // End parent
        out.endObject();
        boolean obj1 = value.isBooleanTest1();
        out.name("isBooleanTest1");
        gson.getAdapter(Boolean.class).write(out, obj1);

        Boolean obj2 = value.isBooleanTest2();
        if (obj2 != null) {
            out.name("isBooleanTest2");
            GsonUtil.writeWithGenericAdapter(gson, obj2.getClass(), out, obj2);
        }

        // End
        out.endObject();
    }
}