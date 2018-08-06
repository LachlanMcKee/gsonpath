package gsonpath.extension;

import com.squareup.javapoet.CodeBlock;
import gsonpath.compiler.ExtensionFieldMetadata;
import gsonpath.compiler.GsonPathExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.ProcessingEnvironment;

public class TestExtension implements GsonPathExtension {
    @NotNull
    @Override
    public String getExtensionName() {
        return "Test";
    }

    @Nullable
    @Override
    public CodeBlock createFieldReadCodeBlock(@NotNull ProcessingEnvironment processingEnvironment, @NotNull ExtensionFieldMetadata extensionFieldMetadata) {
        return CodeBlock.builder().build();
    }
}
