package de.andrena.converter.processor;

import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import de.andrena.annotation.Converter;
import de.andrena.converter.processor.generator.ConverterBuilder;
import de.andrena.converter.processor.informationextractor.AnnotatedClassExtractor;
import de.andrena.converter.processor.informationextractor.ConversionInformation;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.NOTE;


@AutoService(Processor.class)
public class ConverterProcessor extends AbstractProcessor {
    private AnnotatedClassExtractor annotatedClassExtractor;

    private ConverterBuilder converterBuilder;

    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        log("start init");
        injectDependencies();
        converterBuilder.setFiler(processingEnv.getFiler());
        converterBuilder.setMessager(messager);
        annotatedClassExtractor.setMessager(messager);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log("start processing");
        annotations.forEach(element -> messager.printMessage(NOTE, element.getSimpleName()));
        List<ConversionInformation> conversionInformationList = annotatedClassExtractor.extract(roundEnv);
        conversionInformationList.forEach(converterBuilder::generate);
        return true;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Converter.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @VisibleForTesting
    void setAnnotatedClassExtractor(AnnotatedClassExtractor annotatedClassExtractor) {
        this.annotatedClassExtractor = annotatedClassExtractor;
    }

    @VisibleForTesting
    void setConverterBuilder(ConverterBuilder converterBuilder) {
        this.converterBuilder = converterBuilder;
    }

    private void injectDependencies() {
        log("injecting");
        if (converterBuilder == null) {
            log("inject ConverterBuilder");
            converterBuilder = new ConverterBuilder();
        }
        if (annotatedClassExtractor == null) {
            log("inject AnnotatedClassExtractor");
            annotatedClassExtractor = new AnnotatedClassExtractor();
        }
    }

    private void log(String start_init) {
        messager.printMessage(NOTE, start_init);
    }
}
