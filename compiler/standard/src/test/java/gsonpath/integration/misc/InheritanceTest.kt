package gsonpath.integration.misc

import gsonpath.integration.common.IntegrationTester.inputSource
import gsonpath.integration.common.IntegrationTester.integrationTest
import gsonpath.integration.common.IntegrationTester.outputSource
import gsonpath.integration.common.IntegrationTester.testGsonTypeFactory
import org.junit.Test

class InheritanceTest {

    @Test
    fun testInheritance() = integrationTest(
            testGsonTypeFactory,

            inputSource("TestInheritanceBase", """
                import com.google.gson.annotations.SerializedName;

                public class TestInheritanceBase {
                  @SerializedName("Json1")
                  public int value1;
                }
                """
            ),

            inputSource("TestInheritance", """
                import gsonpath.AutoGsonAdapter;
                
                @AutoGsonAdapter
                public class TestInheritance extends TestInheritanceBase {}
                """
            ),

            outputSource("TestInheritance_GsonTypeAdapter", """
                import com.google.gson.Gson;
                import com.google.gson.stream.JsonReader;
                import com.google.gson.stream.JsonWriter;
                import gsonpath.GsonPathGenerated;
                import gsonpath.GsonPathTypeAdapter;
                import gsonpath.JsonReaderHelper;
                import java.io.IOException;
                import java.lang.Integer;
                import java.lang.Override;
                
                @GsonPathGenerated
                public final class TestInheritance_GsonTypeAdapter extends GsonPathTypeAdapter<TestInheritance> {
                  public TestInheritance_GsonTypeAdapter(Gson gson) {
                    super(gson);
                  }
                
                  @Override
                  public TestInheritance readImpl(JsonReader in) throws IOException {
                    TestInheritance result = new TestInheritance();
                    JsonReaderHelper jsonReaderHelper = new JsonReaderHelper(in, 1, 0);
                
                    while (jsonReaderHelper.handleObject(0, 1)) {
                      switch (in.nextName()) {
                        case "Json1":
                          Integer value_Json1 = gson.getAdapter(Integer.class).read(in);
                          if (value_Json1 != null) {
                            result.value1 = value_Json1;
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
                  public void writeImpl(JsonWriter out, TestInheritance value) throws IOException {
                    // Begin
                    out.beginObject();
                    int obj0 = value.value1;
                    out.name("Json1");
                    gson.getAdapter(Integer.class).write(out, obj0);
                
                    // End 
                    out.endObject();
                  }
                }
                """
            )
    )
}
