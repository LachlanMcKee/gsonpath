package gsonpath;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import com.google.gson.annotations.SerializedName;
import gsonpath.generator.AutoGsonAdapterGenerator;
import gsonpath.generator.AutoGsonArrayAdapterGenerator;
import gsonpath.generator.HandleResult;
import gsonpath.generator.LoaderGenerator;

/**
 * Created by Lachlan on 1/03/2016.
 */
@AutoService(Processor.class)
public class GsonProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        Set<? extends Element> generatedAdapters = env.getElementsAnnotatedWith(AutoGsonAdapter.class);

        // Handle the standard type adapters.
        List<HandleResult> autoGsonAdapterResults = new ArrayList<>();
        AutoGsonAdapterGenerator adapterGenerator = new AutoGsonAdapterGenerator(processingEnv);
        for (Element element : generatedAdapters) {
            System.out.println("Handling element: " + element.getSimpleName());

            try {
                autoGsonAdapterResults.add(adapterGenerator.handle((TypeElement) element));
            } catch (ProcessingException e) {
                return false;
            }

        }

        if (autoGsonAdapterResults.size() > 0) {
            if (!new LoaderGenerator(processingEnv).generate(autoGsonAdapterResults)) {
                return false;
            }
        }

        // Handle the array adapters.
        Set<? extends Element> generatedArrayAdapters = env.getElementsAnnotatedWith(AutoGsonArrayAdapter.class);

        AutoGsonArrayAdapterGenerator arrayAdapterGenerator = new AutoGsonArrayAdapterGenerator(processingEnv);
        for (Element element : generatedArrayAdapters) {
            System.out.println("Handling element: " + element.getSimpleName());

            try {
                arrayAdapterGenerator.handle((TypeElement) element);
            } catch (ProcessingException e) {
                return false;
            }

        }

        return false;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportedTypes = new LinkedHashSet<>();
        supportedTypes.add(AutoGsonAdapter.class.getCanonicalName());
        supportedTypes.add(AutoGsonArrayAdapter.class.getCanonicalName());
        supportedTypes.add(FlattenJson.class.getCanonicalName());
        supportedTypes.add(SerializedName.class.getCanonicalName());
        return supportedTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}