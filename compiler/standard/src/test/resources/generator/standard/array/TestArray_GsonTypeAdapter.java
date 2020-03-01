package generator.standard.array;

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
public final class TestArray_GsonTypeAdapter extends GsonPathTypeAdapter<TestArray> {
    public TestArray_GsonTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public TestArray readImpl(JsonReader in, GsonErrors gsonErrors) throws IOException {
        TestArray result = new TestArray();
        JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 4, 5);

        while (jsonReaderHelper.handleObject(0, 4)) {
            switch (in.nextName()) {
                case "test1":
                    while (jsonReaderHelper.handleArray(0)) {
                        switch (jsonReaderHelper.getArrayIndex(0)) {
                            case 1:
                                Integer value_test1_1_ = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                if (value_test1_1_ != null) {
                                    result.plainArray = value_test1_1_;
                                }
                                break;

                            default:
                                jsonReaderHelper.onArrayFieldNotFound(0);
                                break;

                        }
                    }
                    break;

                case "test2":
                    while (jsonReaderHelper.handleArray(1)) {
                        switch (jsonReaderHelper.getArrayIndex(1)) {
                            case 2:
                                while (jsonReaderHelper.handleObject(1, 2)) {
                                    switch (in.nextName()) {
                                        case "child":
                                            Integer value_test2_2__child = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                            if (value_test2_2__child != null) {
                                                result.arrayWithNestedObject = value_test2_2__child;
                                            }
                                            break;

                                        case "child2":
                                            Integer value_test2_2__child2 = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                            if (value_test2_2__child2 != null) {
                                                result.arrayWithNestedObject2 = value_test2_2__child2;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(1);
                                            break;

                                    }
                                }
                                break;

                            default:
                                jsonReaderHelper.onArrayFieldNotFound(1);
                                break;

                        }
                    }
                    break;

                case "test3":
                    while (jsonReaderHelper.handleArray(2)) {
                        switch (jsonReaderHelper.getArrayIndex(2)) {
                            case 3:
                                while (jsonReaderHelper.handleObject(2, 1)) {
                                    switch (in.nextName()) {
                                        case "child":
                                            while (jsonReaderHelper.handleArray(3)) {
                                                switch (jsonReaderHelper.getArrayIndex(3)) {
                                                    case 1:
                                                        Integer value_test3_3__child_1_ = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                                        if (value_test3_3__child_1_ != null) {
                                                            result.arrayWithNestedArray = value_test3_3__child_1_;
                                                        }
                                                        break;

                                                    default:
                                                        jsonReaderHelper.onArrayFieldNotFound(3);
                                                        break;

                                                }
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onObjectFieldNotFound(2);
                                            break;

                                    }
                                }
                                break;

                            default:
                                jsonReaderHelper.onArrayFieldNotFound(2);
                                break;

                        }
                    }
                    break;

                case "test4":
                    while (jsonReaderHelper.handleObject(3, 1)) {
                        switch (in.nextName()) {
                            case "child":
                                while (jsonReaderHelper.handleArray(4)) {
                                    switch (jsonReaderHelper.getArrayIndex(4)) {
                                        case 1:
                                            Integer value_test4_child_1_ = GsonUtil.read(gson, Integer.class, gsonErrors, in);
                                            if (value_test4_child_1_ != null) {
                                                result.objectWithNestedArray = value_test4_child_1_;
                                            }
                                            break;

                                        default:
                                            jsonReaderHelper.onArrayFieldNotFound(4);
                                            break;

                                    }
                                }
                                break;

                            default:
                                jsonReaderHelper.onObjectFieldNotFound(3);
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
    public void writeImpl(JsonWriter out, TestArray value) throws IOException {
        // Begin
        out.beginObject();

        // Begin Array: '.test1'
        out.name("test1");
        out.beginArray();

        out.nullValue(); // Set Value: 'test1[0]'

        // Set Value: 'test1[1]'
        int obj0 = value.plainArray;
        gson.getAdapter(Integer.class).write(out, obj0);

        // End Array: 'test1'
        out.endArray();

        // Begin Array: '.test2'
        out.name("test2");
        out.beginArray();

        out.nullValue(); // Set Value: 'test2[0]'
        out.nullValue(); // Set Value: 'test2[1]'

        // Begin Object: 'test2[2]'
        out.beginObject();
        int obj1 = value.arrayWithNestedObject;
        out.name("child");
        gson.getAdapter(Integer.class).write(out, obj1);

        int obj2 = value.arrayWithNestedObject2;
        out.name("child2");
        gson.getAdapter(Integer.class).write(out, obj2);

        // End test2[2]
        out.endObject();
        // End Array: 'test2'
        out.endArray();

        // Begin Array: '.test3'
        out.name("test3");
        out.beginArray();

        out.nullValue(); // Set Value: 'test3[0]'
        out.nullValue(); // Set Value: 'test3[1]'
        out.nullValue(); // Set Value: 'test3[2]'

        // Begin Object: 'test3[3]'
        out.beginObject();

        // Begin Array: 'test3[3].child'
        out.name("child");
        out.beginArray();

        out.nullValue(); // Set Value: 'test3[3].child[0]'

        // Set Value: 'test3[3].child[1]'
        int obj3 = value.arrayWithNestedArray;
        gson.getAdapter(Integer.class).write(out, obj3);

        // End Array: 'child'
        out.endArray();
        // End test3[3]
        out.endObject();
        // End Array: 'test3'
        out.endArray();

        // Begin test4
        out.name("test4");
        out.beginObject();

        // Begin Array: 'test4.child'
        out.name("child");
        out.beginArray();

        out.nullValue(); // Set Value: 'test4.child[0]'

        // Set Value: 'test4.child[1]'
        int obj4 = value.objectWithNestedArray;
        gson.getAdapter(Integer.class).write(out, obj4);

        // End Array: 'child'
        out.endArray();
        // End test4
        out.endObject();
        // End
        out.endObject();
    }
}