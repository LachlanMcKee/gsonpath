package generator.standard.nested_json.root_nesting;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonPathTypeAdapter;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestRootNesting_GsonTypeAdapter extends GsonPathTypeAdapter<TestRootNesting> {
    public TestRootNesting_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestRootNesting readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestRootNesting result = new TestRootNesting();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 1)) {
            switch (in.nextName()) {
                case "Root":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Nest1":
                                while (jsonReaderHelper.handleObject(2, 1)) {
                                    switch (in.nextName()) {
                                        case "value1":
                                            Integer value_value1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                            if (value_value1 != null) {
                                                result.value1 = value_value1;
                                            }
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

                default:
                    jsonReaderHelper.onObjectFieldNotFound(0);
                    break;

            }
        }
        return result;
    }

    @Override
    public void writeImpl(JsonWriter out, TestRootNesting value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Root
        out.name("Root");
        out.beginObject();

        // Begin RootNest1
        out.name("Nest1");
        out.beginObject();
        int obj0 = value.value1;
        out.name("value1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End RootNest1
        out.endObject();
        // End Root
        out.endObject();
        // End
        out.endObject();
    }
}