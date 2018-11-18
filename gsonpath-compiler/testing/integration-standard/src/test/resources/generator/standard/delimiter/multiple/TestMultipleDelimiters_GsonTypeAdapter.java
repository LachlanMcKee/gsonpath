package generator.standard.delimiter.multiple;

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
public final class TestMultipleDelimiters_GsonTypeAdapter extends TypeAdapter<TestMultipleDelimiters> {
    private final Gson mGson;

    public TestMultipleDelimiters_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestMultipleDelimiters read(JsonReader in) throws IOException {

        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestMultipleDelimiters result = new TestMultipleDelimiters();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "Json1":
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

                                Integer value_Json1_Nest1 = mGson.getAdapter(Integer.class).read(in);
                                if (value_Json1_Nest1 != null) {
                                    result.value1 = value_Json1_Nest1;
                                }
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }


                    in.endObject();
                    break;

                case "Json2.Nest1":
                    jsonFieldCounter0++;

                    Integer value_Json2_Nest1 = mGson.getAdapter(Integer.class).read(in);
                    if (value_Json2_Nest1 != null) {
                        result.value2 = value_Json2_Nest1;
                    }
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
    public void write(JsonWriter out, TestMultipleDelimiters value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin Json1
        out.name("Json1");
        out.beginObject();
        int obj0 = value.value1;
        out.name("Nest1");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End Json1
        out.endObject();
        int obj1 = value.value2;
        out.name("Json2.Nest1");
        mGson.getAdapter(Integer.class).write(out, obj1);

        // End
        out.endObject();
    }
}