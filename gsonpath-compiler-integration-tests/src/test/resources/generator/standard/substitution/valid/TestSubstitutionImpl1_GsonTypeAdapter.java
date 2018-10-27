package generator.standard.substitution.valid;

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
public final class TestSubstitutionImpl1_GsonTypeAdapter extends TypeAdapter<TestSubstitutionImpl1> {
    private final Gson mGson;

    public TestSubstitutionImpl1_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public TestSubstitutionImpl1 read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        TestSubstitutionImpl1 result = new TestSubstitutionImpl1();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "Impl1_A":
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
                            case "Json1":
                                jsonFieldCounter1++;

                                Integer value_Impl1_A_Json1 = mGson.getAdapter(Integer.class).read(in);
                                if (value_Impl1_A_Json1 != null) {
                                    result.value1 = value_Impl1_A_Json1;
                                }
                                break;

                            default:
                                in.skipValue();
                                break;
                        }
                    }

                    in.endObject();
                    break;

                case "Impl1_B":
                    jsonFieldCounter0++;

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
                            case "Json1":
                                jsonFieldCounter2++;

                                Integer value_Impl1_B_Json1 = mGson.getAdapter(Integer.class).read(in);
                                if (value_Impl1_B_Json1 != null) {
                                    result.value2 = value_Impl1_B_Json1;
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
        return result;
    }

    @Override
    public void write(JsonWriter out, TestSubstitutionImpl1 value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();

        // Begin Impl1_A
        out.name("Impl1_A");
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        mGson.getAdapter(Integer.class).write(out, obj0);

        // End Impl1_A
        out.endObject();

        // Begin Impl1_B
        out.name("Impl1_B");
        out.beginObject();
        int obj1 = value.value2;
        out.name("Json1");
        mGson.getAdapter(Integer.class).write(out, obj1);

        // End Impl1_B
        out.endObject();
        // End 
        out.endObject();
    }
}