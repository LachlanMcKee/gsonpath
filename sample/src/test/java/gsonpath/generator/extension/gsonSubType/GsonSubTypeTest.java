package gsonpath.generator.extension.gsonSubType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import gsonpath.GsonPath;
import gsonpath.TestGsonTypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;

public class GsonSubTypeTest {

    @Test
    public void testValidGsonSubTypeUsingList() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .create();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_valid.json");

        TypesList typesList = gson.fromJson(new InputStreamReader(resourceAsStream), TypesList.class);
        Type[] types = typesList.getItems();
        Assert.assertEquals(5, types.length);

        validateValue1(types[0]);
        validateValue2(types[1]);
        validateValue3(types[2]);
        validateValue4(types[3]);
        validateValue5(types[4]);
    }

    private void validateValue1(Type type) {
        Type1 value1 = (Type1) type;
        Assert.assertEquals("type1", value1.type);
        Assert.assertEquals("Type1 Example 1", value1.name);
        Assert.assertEquals(1, value1.intTest);
    }

    private void validateValue2(Type type) {
        Type1 value2 = (Type1) type;
        Assert.assertEquals("type1", value2.type);
        Assert.assertEquals("Type1 Example 2", value2.name);
        Assert.assertEquals(2, value2.intTest);
    }

    private void validateValue3(Type type) {
        Type2 value3 = (Type2) type;
        Assert.assertEquals("type2", value3.type);
        Assert.assertEquals("Type2 Example 1", value3.name);
        Assert.assertEquals(1.0, value3.doubleTest, 0);
    }

    private void validateValue4(Type type) {
        Type3 value4 = (Type3) type;
        Assert.assertEquals("type3", value4.type);
        Assert.assertEquals("Type3 Example 1", value4.name);
        Assert.assertEquals("123", value4.stringTest);
    }

    private void validateValue5(Type type) {
        Assert.assertNull(type);
    }

    @Test
    public void testValidGsonSubTypeUsingPojo() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .create();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_valid.json");

        TypesPojo typesList = gson.fromJson(new InputStreamReader(resourceAsStream), TypesPojo.class);

        validateValue1(typesList.getItem0());
        validateValue2(typesList.getItem1());
        validateValue3(typesList.getItem2());
        validateValue4(typesList.getItem3());
        validateValue5(typesList.getItem4());
    }

    @Test
    public void givenNullTypeField_whenReadingJson_thenExpectException() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(GsonPath.createTypeAdapterFactory(TestGsonTypeFactory.class))
                .create();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("Polymorphism_type_is_null.json");

        try {
            gson.fromJson(new InputStreamReader(resourceAsStream), TypesList.class);

        } catch (JsonParseException e) {
            // Since the mandatory value is not found, we are expecting an exception.
            Assert.assertEquals(JsonParseException.class, e.getClass());
            Assert.assertEquals("cannot deserialize gsonpath.generator.extension.gsonSubType.Type because the subtype field 'type' is either null or does not exist.", e.getMessage());
            return;
        }

        Assert.fail("Expected JsonParseException was not triggered");
    }

}
