package generator.standard.substitution.valid;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import gsonpath.GsonPathGenerated;
import gsonpath.GsonPathListener;
import gsonpath.GsonPathTypeAdapter;
import gsonpath.JsonReaderHelper;
import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;

@GsonPathGenerated
public final class TestSubstitutionImpl1_GsonTypeAdapter extends GsonPathTypeAdapter<TestSubstitutionImpl1> {
    public TestSubstitutionImpl1_GsonTypeAdapter(Gson gson, GsonPathListener listener) {
        super(gson, listener);
    }

    @Override
    public TestSubstitutionImpl1 readImpl(JsonReader in) throws IOException {
        TestSubstitutionImpl1 result = new TestSubstitutionImpl1();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 3, 0);

        while (jsonReaderHelper.handleObject(0, 2)) {
            switch (in.nextName()) {
                case "Impl1_A":
                    while (jsonReaderHelper.handleObject(1, 1)) {
                        switch (in.nextName()) {
                            case "Json1":
                                Integer value_Impl1_A_Json1 = gson.getAdapter(Integer.class).read(in);
                                if (value_Impl1_A_Json1 != null) {
                                    result.value1 = value_Impl1_A_Json1;
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "Impl1_B":
                    while (jsonReaderHelper.handleObject(2, 1)) {
                        switch (in.nextName()) {
                            case "Json1":
                                Integer value_Impl1_B_Json1 = gson.getAdapter(Integer.class).read(in);
                                if (value_Impl1_B_Json1 != null) {
                                    result.value2 = value_Impl1_B_Json1;
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
    public void writeImpl(JsonWriter out, TestSubstitutionImpl1 value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Impl1_A
        out.name("Impl1_A");
        out.beginObject();
        int obj0 = value.value1;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Impl1_A
        out.endObject();

        // Begin Impl1_B
        out.name("Impl1_B");
        out.beginObject();
        int obj1 = value.value2;
        out.name("Json1");
        gson.getAdapter(Integer.class).write(out, obj1);

        // End Impl1_B
        out.endObject();
        // End
        out.endObject();
    }
}