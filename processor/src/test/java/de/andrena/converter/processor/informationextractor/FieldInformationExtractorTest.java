package de.andrena.converter.processor.informationextractor;

import de.andrena.annotation.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import java.util.Set;

import static javax.lang.model.element.Modifier.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldInformationExtractorTest {

    private static final String NAME = "field";
    private Element field;
    private Name name;
    private FieldInformationExtractor extractor;
    private Mapping mapping;

    @BeforeEach
    void setUp() {
        name = mock(Name.class);
        when(name.toString()).thenReturn(NAME);
        field = mock(Element.class);
        when(field.getSimpleName()).thenReturn(name);
        extractor = new FieldInformationExtractor();
        mapping = mock(Mapping.class);
    }

    @Test
    void extractsName() {
        FieldInformation result = extractor.extract(field);
        assertThat(result.getName()).isEqualTo(NAME);
    }

    @Test
    void getterAndSetterAreFieldNameIfFieldIsPublic() {
        when(field.getModifiers()).thenAnswer(invocation -> Set.of(PUBLIC));
        FieldInformation result = extractor.extract(field);
        assertThat(result.findGetter()).isEqualTo(NAME);
        assertThat(result.findSetter()).isEqualTo(NAME);
    }

    @Test
    void checksIfPublic() {
        when(field.getModifiers()).thenAnswer(invocation -> Set.of(PUBLIC));
        FieldInformation result = extractor.extract(field);
        assertThat(result.isPublic()).isTrue();

    }

    @Test
    void isPublicRetrunsFalseForNonPublicFields() {
        when(field.getModifiers()).thenAnswer(invocation -> Set.of(PRIVATE));

        Element packagePrivateField = mock(Element.class);
        when(packagePrivateField.getSimpleName()).thenReturn(name);

        Element protectedField = mock(Element.class);
        when(protectedField.getSimpleName()).thenReturn(name);
        when(protectedField.getModifiers()).thenAnswer(invocation -> Set.of(PROTECTED));

        FieldInformation privateFieldInformation = extractor.extract(field);
        FieldInformation packagePrivateFieldInformation = extractor.extract(packagePrivateField);
        FieldInformation protectedFieldInformation = extractor.extract(protectedField);

        assertThat(privateFieldInformation.isPublic()).isFalse();
        assertThat(packagePrivateFieldInformation.isPublic()).isFalse();
        assertThat(protectedFieldInformation.isPublic()).isFalse();
    }

    @Test
    void useGetterAndSetterMethodsIfFieldIsNonPublic() {
        when(field.getModifiers()).thenAnswer(invocation -> Set.of(PRIVATE));

        FieldInformation result = extractor.extract(field);

        assertThat(result.findSetter()).isEqualTo("setField");
        assertThat(result.findGetter()).isEqualTo("getField()");
    }

    @Test
    void setsMappingNameToFieldNameIfNotSpecified() {
        FieldInformation result = extractor.extract(field);
        assertThat(result.getMapping()).isEqualTo(NAME);
    }

    @Test
    void setsMappingNameIfAnnotated() {
        when(mapping.value()).thenReturn("mappingName");

        Element annotatedField = mock(Element.class);
        when(annotatedField.getSimpleName()).thenReturn(name);
        when(annotatedField.getAnnotation(Mapping.class)).thenReturn(mapping);

        FieldInformation result = extractor.extract(annotatedField);

        assertThat(result.getMapping()).isEqualTo("mappingName");
    }
}