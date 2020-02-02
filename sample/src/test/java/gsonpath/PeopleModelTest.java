package gsonpath;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonpath.generated.PersonModelGenerated;
import gsonpath.vanilla.PeopleModelVanilla;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class PeopleModelTest {

    private static final int PEOPLE_SIZE = 50000;
    private static final int testSize = 50;

    // Create very large json file in memory to help benchmark.
    private static final String JSON_TEST_STRING;

    static {
        StringBuilder sb = new StringBuilder("{\"people\": [");

        int max = PEOPLE_SIZE;
        for (int i = 0; i < max; i++) {
            sb.append("{ \"person\": { \"names\": { \"first\": \"Lachlan\", \"last\": \"McKee\",");
            sb.append("\"Unused1\": \"ABC\", \"Unused2\": \"ABC\", \"Unused3\": \"ABC\",");
            sb.append("\"Unused4\": \"ABC\", \"Unused5\": \"ABC\", \"Unused6\": \"ABC\",");
            sb.append("\"Unused7\": \"ABC\", \"Unused8\": \"ABC\", \"Unused9\": \"ABC\",");
            sb.append("\"Unused8\": \"ABC\", \"Unused9\": \"ABC\", \"Unused10\": \"ABC\",");
            sb.append("\"Unused11\": \"ABC\", \"Unused12\": \"ABC\", \"Unused13\": \"ABC\",");
            sb.append("\"Unused14\": \"ABC\", \"Unused15\": \"ABC\", \"Unused16\": \"ABC\"");
            sb.append("} } }");

            if (i < max - 1) {
                sb.append(",");
            }
        }
        sb.append("]}");

        JSON_TEST_STRING = sb.toString();
    }

    // Create the gson objects once.
    private Gson vanillaGson;
    private Gson gsonPath;

    @Test
    public void test() {
        vanillaGson = new GsonBuilder().create();

        GsonBuilder gsonPathBuilder = new GsonBuilder();
        gsonPathBuilder.registerTypeAdapterFactory(new TestGsonTypeFactoryImpl(null));
        gsonPath = gsonPathBuilder.create();

        // Benchmark regular gson.
        long vanillaAverage = 0;
        for (int i = 0; i < testSize; i++) {
            vanillaAverage += testVanillaGson();
        }
        System.out.println("vanillaAverage: " + (vanillaAverage / testSize));

        // Benchmark gson with gson path.
        long pathAverage = 0;
        for (int i = 0; i < testSize; i++) {
            pathAverage += testGsonPath();
        }
        System.out.println("pathAverage: " + (pathAverage / testSize));
    }

    private long testVanillaGson() {
        long start = System.nanoTime();
        PeopleModelVanilla vanillaModel = vanillaGson.fromJson(JSON_TEST_STRING, PeopleModelVanilla.class);

        long duration = ((System.nanoTime() - start) / 1000000);
        System.out.println("vanillaModel. Time taken: " + duration);

        Assert.assertEquals(PEOPLE_SIZE, vanillaModel.people.length);
        Assert.assertEquals("Lachlan", vanillaModel.people[0].person.names.first);

        return duration;
    }

    private long testGsonPath() {
        long start = System.nanoTime();
        PersonModelGenerated personModelGenerated = gsonPath.fromJson(new StringReader(JSON_TEST_STRING), PersonModelGenerated.class);

        long duration = ((System.nanoTime() - start) / 1000000);
        System.out.println("gsonPathModel. Time taken: " + duration);

        PersonModelGenerated.PersonModel[] people = personModelGenerated.getPeople();
        Assert.assertEquals(PEOPLE_SIZE, people.length);
        Assert.assertEquals("Lachlan", people[0].getFirstName());

        return duration;
    }

}
