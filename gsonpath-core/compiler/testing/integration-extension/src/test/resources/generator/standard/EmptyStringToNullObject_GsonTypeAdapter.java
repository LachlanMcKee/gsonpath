package generator.standard;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
    value = "gsonpath.GsonProcessor",
    comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class EmptyStringToNullObject_GsonTypeAdapter extends TypeAdapter<EmptyStringToNullObject> {
    private final Gson mGson;

    public EmptyStringToNullObject_GsonTypeAdapter(Gson gson) {
        this.mGson = gson;
    }

    @Override
    public EmptyStringToNullObject read(JsonReader in) throws IOException {
        // Ensure the object is not null.
        if (!isValidValue(in)) {
            return null;
        }
        EmptyStringToNullObject result = new EmptyStringToNullObject();

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 2) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "element1":
                    jsonFieldCounter0++;

                    JsonElement value_element1 = mGson.getAdapter(JsonElement.class).read(in);
                    if (value_element1 != null) {
                        result.element1 = value_element1.toString();
                    }

                    // Gsonpath Extensions
                    if (result.element1 != null) {

                        // Extension - 'EmptyStringToNull' Annotation
                        if (result.element1.trim().length() == 0) {
                            result.element1 = null;
                        }

                    }
                    break;

                case "element2":
                    jsonFieldCounter0++;

                    String value_element2 = mGson.getAdapter(String.class).read(in);
                    if (value_element2 != null) {
                        result.element2 = value_element2;
                    }

                    // Gsonpath Extensions
                    if (result.element2 != null) {

                        // Extension - 'EmptyStringToNull' Annotation
                        if (result.element2.trim().length() == 0) {
                            result.element2 = null;
                        }

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
    public void write(JsonWriter out, EmptyStringToNullObject value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        // Begin
        out.beginObject();
        String obj0 = value.element1;
        if (obj0 != null) {
            out.name("element1");
            mGson.getAdapter(String.class).write(out, obj0);
        }

        String obj1 = value.element2;
        if (obj1 != null) {
            out.name("element2");
            mGson.getAdapter(String.class).write(out, obj1);
        }

        // End
        out.endObject();
    }
}