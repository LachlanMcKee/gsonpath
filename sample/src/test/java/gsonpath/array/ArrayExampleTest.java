package gsonpath.array;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.TestGsonTypeFactoryImpl;
import org.junit.Assert;
import org.junit.Test;

public class ArrayExampleTest {
    private static final String ARRAY_VALUE = "{\"test1\":[null,1],\"test2\":[null,null,{\"child\":10,\"child2\":20}],\"test3\":[null,null,null,{\"child\":[null,50]}],\"test4\":{\"child\":[null,100]}}";

    @Test
    public void testSerialize() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new TestGsonTypeFactoryImpl(null));
        Gson gson = builder.create();

        ArrayExample model = new ArrayExample();
        model.plainArray = 1;
        model.arrayWithNestedObject = 10;
        model.arrayWithNestedObject2 = 20;
        model.arrayWithNestedArray = 50;
        model.objectWithNestedArray = 100;

        String test = gson.toJson(model);
        Assert.assertEquals(ARRAY_VALUE, test);
    }

    @Test
    public void testDeserialize() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(new TestGsonTypeFactoryImpl(null));
        Gson gson = builder.create();

        ArrayExample vanillaModel = gson.fromJson(ARRAY_VALUE, ArrayExample.class);

        Assert.assertEquals(1, vanillaModel.plainArray);
        Assert.assertEquals(10, vanillaModel.arrayWithNestedObject);
        Assert.assertEquals(20, vanillaModel.arrayWithNestedObject2);
        Assert.assertEquals(50, vanillaModel.arrayWithNestedArray);
        Assert.assertEquals(100, vanillaModel.objectWithNestedArray);
    }
}