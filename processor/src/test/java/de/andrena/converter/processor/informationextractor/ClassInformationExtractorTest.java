package de.andrena.converter.processor.informationextractor;

import com.squareup.javapoet.ClassName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassInformationExtractorTest {

    private static final String NAME = "TestClass";
    private static final FieldInformation FIELD_INFORMATION = new FieldInformation(NAME, null, true);
    private TypeElement typeElement;
    private ClassInformationExtractor extractor;
    private ClassNameProvider classNameProvider;
    private FieldInformationExtractor fieldInformationExtractor;

    @BeforeEach
    void setUp() {
        typeElement = mock(TypeElement.class);
//        when(typeElement.getKind()).thenReturn(ElementKind.FIELD);

        classNameProvider = mock(ClassNameProvider.class);
        when(classNameProvider.getClassName(typeElement)).thenReturn(ClassName.get(TestClass.class));

        fieldInformationExtractor = mock(FieldInformationExtractor.class);

        extractor = new ClassInformationExtractor(fieldInformationExtractor, classNameProvider);

    }

    @Test
    void extractsClassName() {
        ClassInformation result = extractor.extract(typeElement);
        assertThat(result.getSimpleName()).isEqualTo(NAME);
    }

    @Test
    void extractsFieldInformation() {
        Element fieldElement = mock(Element.class);
        when(fieldElement.getKind()).thenReturn(ElementKind.FIELD);

        when(fieldInformationExtractor.extract(fieldElement)).thenReturn(FIELD_INFORMATION);

        when(typeElement.getEnclosedElements()).thenAnswer(invocation -> Collections.singletonList(fieldElement));

        ClassInformation result = extractor.extract(typeElement);

        assertThat(result.hasField(FIELD_INFORMATION)).isTrue();
    }

    private class TestClass {
    }
}