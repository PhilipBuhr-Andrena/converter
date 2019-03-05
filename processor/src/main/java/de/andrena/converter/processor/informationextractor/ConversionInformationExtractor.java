package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Converter;

import javax.lang.model.element.TypeElement;
import java.util.Set;

class ConversionInformationExtractor {


    private ClassInformationExtractor classInformationExtractor;

    ConversionInformationExtractor() {
        this(new ClassInformationExtractor());
    }

    ConversionInformationExtractor(ClassInformationExtractor classInformationExtractor) {
        this.classInformationExtractor = classInformationExtractor;
    }

    ConversionInformation extract(TypeElement model, Set<TypeElement> sources) {
        ConversionInformation result = new ConversionInformation();
        result.setName(model.getAnnotation(Converter.class).name());
        result.setModel(classInformationExtractor.extract(model));
        sources.forEach(element -> {
            ClassInformation source = classInformationExtractor.extract(element);
            result.addSource(source);
        });
        return result;
    }
}
