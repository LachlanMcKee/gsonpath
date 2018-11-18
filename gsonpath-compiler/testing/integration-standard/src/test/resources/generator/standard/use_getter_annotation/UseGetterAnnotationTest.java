package generator.standard.use_getter_annotation;

import com.google.gson.annotations.SerializedName;
import gsonpath.AutoGsonAdapter;
import gsonpath.GsonFieldValidationType;
import org.jetbrains.annotations.NotNull;

public abstract class UseGetterAnnotationTest {
    @SerializedName("common.")
    @NotNull
    public abstract String getName();

    private UseGetterAnnotationTest() {
    }

    @AutoGsonAdapter(fieldValidationType = GsonFieldValidationType.VALIDATE_EXPLICIT_NON_NULL)
    public static final class Implementation extends UseGetterAnnotationTest {
        private final String name;
        @SerializedName("specific.")
        private int intTest = 5;

        @Override
        public String getName() {
            return this.name;
        }

        public final int getIntTest() {
            return this.intTest;
        }

        public Implementation(@NotNull String name, int intTest) {
            this.name = name;
            this.intTest = intTest;
        }
    }
}
