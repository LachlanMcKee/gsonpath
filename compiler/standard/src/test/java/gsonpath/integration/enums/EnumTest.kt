package gsonpath.integration.enums

import gsonpath.integration.common.IntegrationTester.inputSource
import gsonpath.integration.common.IntegrationTester.integrationTest
import gsonpath.integration.common.IntegrationTester.outputSource
import gsonpath.integration.common.IntegrationTester.testGsonTypeFactory
import org.junit.Test

class EnumTest {

    @Test
    fun testEnum() = integrationTest(
            testGsonTypeFactory,

            inputSource("TestEnum", """
                import com.google.gson.FieldNamingPolicy;
                import com.google.gson.annotations.SerializedName;
                import gsonpath.AutoGsonAdapter;
                
                @AutoGsonAdapter(fieldNamingPolicy = FieldNamingPolicy.LOWER_CASE_WITH_DASHES)
                enum TestEnum {
                    VALUE_ABC,
                    VALUE_DEF,
                    @SerializedName("custom")
                    VALUE_GHI,
                    VALUE_1
                }
                """
            ),

            outputSource("TestEnum_GsonTypeAdapter", """
                import static gsonpath.GsonUtil.*;

                import com.google.gson.Gson;
                import com.google.gson.stream.JsonReader;
                import com.google.gson.stream.JsonWriter;
                import gsonpath.GsonPathGenerated;
                import gsonpath.GsonPathTypeAdapter;
                import java.io.IOException;
                import java.lang.Override;
                
                @GsonPathGenerated
                public final class TestEnum_GsonTypeAdapter extends GsonPathTypeAdapter<TestEnum> {
                  public TestEnum_GsonTypeAdapter(Gson gson) {
                    super(gson);
                  }
                
                  @Override
                  public TestEnum readImpl(JsonReader in) throws IOException {
                    switch (in.nextString()) {
                      case "value-abc":
                        return gsonpath.testing.TestEnum.VALUE_ABC;
                
                      case "value-def":
                        return gsonpath.testing.TestEnum.VALUE_DEF;
                
                      case "custom":
                        return gsonpath.testing.TestEnum.VALUE_GHI;
                
                      case "value-1":
                        return gsonpath.testing.TestEnum.VALUE_1;
                
                      default:
                        return null;
                
                    }
                  }
                
                  @Override
                  public void writeImpl(JsonWriter out, TestEnum value) throws IOException {
                    switch (value) {
                      case VALUE_ABC:
                        out.value("value-abc");
                        break;
                
                      case VALUE_DEF:
                        out.value("value-def");
                        break;
                
                      case VALUE_GHI:
                        out.value("custom");
                        break;
                
                      case VALUE_1:
                        out.value("value-1");
                        break;
                
                    }
                  }
                }
                """
            )
    )
}
