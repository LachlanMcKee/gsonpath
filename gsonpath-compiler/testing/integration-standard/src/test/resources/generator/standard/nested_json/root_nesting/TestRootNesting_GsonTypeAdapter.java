package generator.standard.nested_json.root_nesting;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestRootNesting_GsonTypeAdapter extends TypeAdapter<TestRootNesting> {
    private final Gson mGson;

    public TestRootNesting_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestRootNesting read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestRootNesting result = new TestRootNesting();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 1) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "Root":
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
                            case "Nest1":
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

                                            Integer value_value1 = mGson.getAdapter(Integer.class).read(in);
                                            if (value_value1 != null) {
                                                result.value1 = value_value1;
                                            }
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

                default:
                    in.skipValue();
                    break;
            }
        }


        in.endObject();
        return result;
    }

    @Override
    public void write(JsonWriter out, TestRootNesting value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin Root
        out.name("Root");
        out.beginObject();

        // Begin Root.Nest1
        out.name("Nest1");
        out.beginObject();
        int obj0 = value.value1;
        out.name("value1");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End Root.Nest1
        out.endObject();
        // End Root
        out.endObject();
        // End
        out.endObject();
    }
}