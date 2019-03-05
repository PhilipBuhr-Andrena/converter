package de.andrena.converter.processor.generator;

import de.andrena.converter.processor.informationextractor.FieldInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StatementBuilderTest {

    private static final String FIELD_NAME = "field";
    private StatementBuilder statementBuilder;

    @BeforeEach
    void setUp() {
        statementBuilder = new StatementBuilder();
    }

    @Test
    void assignsAndCallsPublicFieldsDirectly() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, true);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, true);

        String statement = statementBuilder.mapField(toField, fromField);

        assertThat(statement).isEqualTo("result.field = from.field");
    }

    @Test
    void usesGetterIfFormIsNonPublic() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, true);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, false);

        String statement = statementBuilder.mapField(toField, fromField);

        assertThat(statement).isEqualTo("result.field = from.getField()");
    }

    @Test
    void usesSetterIfToIsNonPublic() {
        FieldInformation toField = new FieldInformation(FIELD_NAME, false);
        FieldInformation fromField = new FieldInformation(FIELD_NAME, true);

        String statement = statementBuilder.mapField(toField, fromField);

        assertThat(statement).isEqualTo("result.setField(from.field)");
    }
}