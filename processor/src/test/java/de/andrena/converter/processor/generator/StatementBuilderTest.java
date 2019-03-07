package de.andrena.converter.processor.generator;

import de.andrena.converter.processor.informationextractor.ConversionMethods;
import de.andrena.converter.processor.informationextractor.FieldInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatementBuilderTest {

    private static final String FIELD_NAME = "field";
    private StatementBuilder statementBuilder;
    private ConversionMethods conversionMethods;
    private TypeMirror type;

    @BeforeEach
    void setUp() {
        type = mock(TypeMirror.class);
        conversionMethods = mock(ConversionMethods.class);
        statementBuilder = new StatementBuilder();
    }

    @Test
    void assignsAndCallsPublicFieldsDirectly() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, type, true);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, type, true);

        String statement = statementBuilder.mapField(toField, fromField, conversionMethods);

        assertThat(statement).isEqualTo("result.field = from.field");
    }

    @Test
    void usesGetterIfFormIsNonPublic() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, type, true);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, type, false);

        String statement = statementBuilder.mapField(toField, fromField, conversionMethods);

        assertThat(statement).isEqualTo("result.field = from.getField()");
    }

    @Test
    void usesSetterIfToIsNonPublic() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, type, false);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, type, true);

        String statement = statementBuilder.mapField(toField, fromField, conversionMethods);

        assertThat(statement).isEqualTo("result.setField(from.field)");
    }

    @Test
    void usesConversionAdaptersIfTypesDontMatch() {
        TypeMirror toType = mock(TypeMirror.class);
        when(toType.toString()).thenReturn("com.example.TypeOne");

        TypeMirror fromType = mock(TypeMirror.class);
        when(fromType.toString()).thenReturn("com.example.TypeTwo");

        FieldInformation toField = new FieldInformation(FIELD_NAME, toType, true);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, fromType, true);
        when(conversionMethods.findAdapter(toType, fromType)).thenReturn("com.example.TestAdapters.testConversionMethod");

        String statement = statementBuilder.mapField(toField, fromField, conversionMethods);

        assertThat(statement).isEqualTo("result.field = com.example.TestAdapters.testConversionMethod(from.field)");
    }
}