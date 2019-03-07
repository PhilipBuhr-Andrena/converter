package de.andrena.converter.processor;

import com.google.auto.service.AutoService;
import com.google.common.annotations.VisibleForTesting;
import de.andrena.annotation.ConversionAdapter;
import de.andrena.annotation.ConversionSource;
import de.andrena.annotation.Converter;
import de.andrena.converter.processor.generator.ConverterBuilder;
import de.andrena.converter.processor.informationextractor.AnnotatedClassExtractor;
import de.andrena.converter.processor.informationextractor.ConversionInformation;
import de.andrena.converter.processor.informationextractor.ConversionMethodExtractor;
import de.andrena.converter.processor.informationextractor.ConversionMethods;

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
    private ConversionMethodExtractor conversionMethodExtractor;

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
        annotations.forEach(element -> log(element.getSimpleName().toString()));
        List<ConversionInformation> conversionInformationList = annotatedClassExtractor.extract(roundEnv);
        ConversionMethods conversionMethods = conversionMethodExtractor.extract(roundEnv);
        conversionInformationList.forEach(conversionInformation -> converterBuilder.generate(conversionInformation, conversionMethods));
        return true;
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Converter.class.getCanonicalName());
        annotations.add(ConversionSource.class.getCanonicalName());
        annotations.add(ConversionAdapter.class.getCanonicalName());
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

    @VisibleForTesting
    void setConversionMethodExtractor(ConversionMethodExtractor conversionMethodExtractor) {
        this.conversionMethodExtractor = conversionMethodExtractor;
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
        if (conversionMethodExtractor == null) {
            log("inject ConversionMethodExtractor");
            conversionMethodExtractor = new ConversionMethodExtractor();
        }
    }

    private void log(String message) {
        messager.printMessage(NOTE, message);
    }
}
