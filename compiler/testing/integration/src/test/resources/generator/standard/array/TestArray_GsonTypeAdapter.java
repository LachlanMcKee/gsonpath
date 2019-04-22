package generator.standard.array;

import static gsonpath.GsonUtil.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import gsonpath.GsonPathTypeAdapter;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import javax.annotation.Generated;

@Generated(
        value = "gsonpath.GsonProcessor",
        comments = "https://github.com/LachlanMcKee/gsonpath"
)
public final class TestArray_GsonTypeAdapter extends GsonPathTypeAdapter<TestArray> {

    public TestArray_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestArray readImpl(JsonReader in) throws IOException {
        TestArray result = new TestArray();
        ArrayHandler arrayHandler = new ArrayHandler(in);

        int jsonFieldCounter0 = 0;
        in.beginObject();

        while (in.hasNext()) {
            if (jsonFieldCounter0 == 4) {
                in.skipValue();
                continue;
            }

            switch (in.nextName()) {
                case "test1":
                    jsonFieldCounter0++;

                    // Ensure the array is not null.
                    if (!isValidValue(in)) {
                        break;
                    }

                    // Iterate through the array.
                    while (arrayHandler.handleArray(0)) {
                        switch (arrayHandler.getIndex(0)) {
                            case 1:
                                Integer value_test1_1_ = gson.getAdapter(Integer.class).read(in);
                                if (value_test1_1_ != null) {
                                    result.plainArray = value_test1_1_;
                                }
                                break;

                            default:
                                arrayHandler.onFieldNotFound(0);
                                break;
                        }
                    }
                    break;

                case "test2":
                    jsonFieldCounter0++;

                    in.beginArray();
                    int test2_arrayIndex = 0;

                    // Iterate through the array.
                    while (in.hasNext()) {
                        switch (test2_arrayIndex) {
                            case 2:
                                int jsonFieldCounter1 = 0;
                                in.beginObject();

                                while (in.hasNext()) {
                                    if (jsonFieldCounter1 == 2) {
                                        in.skipValue();
                                        continue;
                                    }

                                    switch (in.nextName()) {
                                        case "child":
                                            jsonFieldCounter1++;

                                            Integer value_test2_2__child = gson.getAdapter(Integer.class).read(in);
                                            if (value_test2_2__child != null) {
                                                result.arrayWithNestedObject = value_test2_2__child;
                                            }
                                            break;

                                        case "child2":
                                            jsonFieldCounter1++;

                                            Integer value_test2_2__child2 = gson.getAdapter(Integer.class).read(in);
                                            if (value_test2_2__child2 != null) {
                                                result.arrayWithNestedObject2 = value_test2_2__child2;
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
                        test2_arrayIndex++;
                    }
                    in.endArray();
                    break;

                case "test3":
                    jsonFieldCounter0++;

                    // Ensure the array is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    in.beginArray();
                    int test3_arrayIndex = 0;

                    // Iterate through the array.
                    while (in.hasNext()) {
                        switch (test3_arrayIndex) {
                            case 3:
                                int jsonFieldCounter2 = 0;
                                in.beginObject();

                                while (in.hasNext()) {
                                    if (jsonFieldCounter2 == 1) {
                                        in.skipValue();
                                        continue;
                                    }

                                    switch (in.nextName()) {
                                        case "child":
                                            jsonFieldCounter2++;

                                            // Ensure the array is not null.
                                            if (!isValidValue(in)) {
                                                break;
                                            }
                                            in.beginArray();
                                            int child_arrayIndex = 0;

                                            // Iterate through the array.
                                            while (in.hasNext()) {
                                                switch (child_arrayIndex) {
                                                    case 1:
                                                        Integer value_test3_3__child_1_ = gson.getAdapter(Integer.class).read(in);
                                                        if (value_test3_3__child_1_ != null) {
                                                            result.arrayWithNestedArray = value_test3_3__child_1_;
                                                        }
                                                        break;

                                                    default:
                                                        in.skipValue();
                                                        break;
                                                }
                                                child_arrayIndex++;
                                            }
                                            in.endArray();
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
                        test3_arrayIndex++;
                    }
                    in.endArray();
                    break;

                case "test4":
                    jsonFieldCounter0++;

                    // Ensure the object is not null.
                    if (!isValidValue(in)) {
                        break;
                    }
                    int jsonFieldCounter3 = 0;
                    in.beginObject();

                    while (in.hasNext()) {
                        if (jsonFieldCounter3 == 1) {
                            in.skipValue();
                            continue;
                        }

                        switch (in.nextName()) {
                            case "child":
                                jsonFieldCounter3++;

                                // Ensure the array is not null.
                                if (!isValidValue(in)) {
                                    break;
                                }
                                in.beginArray();
                                int child_arrayIndex = 0;

                                // Iterate through the array.
                                while (in.hasNext()) {
                                    switch (child_arrayIndex) {
                                        case 1:
                                            Integer value_test4_child_1_ = gson.getAdapter(Integer.class).read(in);
                                            if (value_test4_child_1_ != null) {
                                                result.objectWithNestedArray = value_test4_child_1_;
                                            }
                                            break;

                                        default:
                                            in.skipValue();
                                            break;
                                    }
                                    child_arrayIndex++;
                                }
                                in.endArray();
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
    public void writeImpl(JsonWriter out, TestArray value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Array: 'test1'
        out.name("test1");
        out.beginArray();

        out.nullValue(); // Set Value: 'test1[0]'

        // Set Value: 'test1[1]'
        int obj0 = value.plainArray;
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Array: 'test1'
        out.endArray();

        // Begin Array: 'test2'
        out.name("test2");
        out.beginArray();

        out.nullValue(); // Set Value: 'test2[0]'
        out.nullValue(); // Set Value: 'test2[1]'

        // Begin Object: 'test2[2]'
        out.beginObject();

        // Set Value: 'test2[2].child'
        int obj1 = value.arrayWithNestedObject;
        out.name("child");
        gson.getAdapter(Integer.class).write(out, obj1);

        // Set Value: 'test2[2].child2'
        int obj2 = value.arrayWithNestedObject2;
        out.name("child2");
        gson.getAdapter(Integer.class).write(out, obj2);

        // End Object: 'test2[2]'
        out.endObject();
        // End Array: 'test2'
        out.endArray();

        // Begin Array: 'test3'
        out.name("test3");
        out.beginArray();

        out.nullValue(); // Set Value: 'test3[0]'
        out.nullValue(); // Set Value: 'test3[1]'
        out.nullValue(); // Set Value: 'test3[2]'

        out.beginObject();
        out.name("child");
        out.beginArray();

        out.nullValue(); // Set Value: 'test3[3].child[0]'

        // Set Value: 'test3[3].child[1]'
        int obj3 = value.arrayWithNestedArray;
        gson.getAdapter(Integer.class).write(out, obj3);

        out.endArray();
        out.endObject();

        // End Array: 'test3'
        out.endArray();

        // Begin Object: 'test4'
        out.name("test4");
        out.beginObject();

        // Begin Array: 'test4.child'
        out.name("child");
        out.beginArray();

        out.nullValue(); // Set Value: 'test4.child[0]'

        // Set Value: 'test4.child[1]'
        int obj4 = value.objectWithNestedArray;
        gson.getAdapter(Integer.class).write(out, obj4);

        // End Array: 'test4.child'
        out.endArray();

        // End Object: 'test4'
        out.endObject();

        out.endObject();
    }

    @Override
    public String getModelClassName() {
        return "generator.standard.array.TestArray";
    }
}