package generator.standard.substitution.valid;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonErrors;
import gsonpath.annotation.GsonPathGenerated;
import gsonpath.internal.GsonUtil;
import gsonpath.internal.JsonReaderHelper;
import gsonpath.internal.adapter.GsonPathTypeAdapter;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestSubstitutionImpl2_GsonTypeAdapter extends GsonPathTypeAdapter<TestSubstitutionImpl2> {
    public TestSubstitutionImpl2_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestSubstitutionImpl2 readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestSubstitutionImpl2 result = new TestSubstitutionImpl2();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "Impl2_A":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Json1":
                                Integer value_Impl2_A_Json1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                if (value_Impl2_A_Json1 != null) {
                                    result.value1 = value_Impl2_A_Json1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "Impl2_B":
                    while (jsonReaderHelper.handleObject(2, 1)) {
                        switch (in.nextName()) {
                            case "Json1":
                                Integer value_Impl2_B_Json1 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                if (value_Impl2_B_Json1 != null) {
                                    result.value2 = value_Impl2_B_Json1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(2);
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
    public void writeImpl(JsonWriter out, TestSubstitutionImpl2 value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Impl2_A
        out.name("Impl2_A");
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Impl2_A
        out.endObject();

        // Begin Impl2_B
        out.name("Impl2_B");
        out.beginObject();
        int obj1 = value.value2;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj1);

        // End Impl2_B
        out.endObject();
        // End
        out.endObject();
    }
}