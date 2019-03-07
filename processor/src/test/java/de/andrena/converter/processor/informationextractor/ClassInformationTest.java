package de.andrena.converter.processor.informationextractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class ClassInformationTest {

    private static final String FIELD = "field";
    private ClassInformation classInformation;

    @BeforeEach
    void setUp() {
        classInformation = new ClassInformation();
    }

    @Test
    void hasFieldReturnsTrueIfClassHasField() {
        FieldInformation field = new FieldInformation(FIELD, null, true);
        FieldInformation wanted = new FieldInformation(FIELD, null, true);

        classInformation.addField(field);

        assertThat(classInformation.hasField(wanted)).isTrue();
    }

    @Test
    void hasFieldReturnsFalseIfClassDoesNotHaveField() {
        FieldInformation field = new FieldInformation("otherName", null, true);

        FieldInformation wanted = new FieldInformation(FIELD, null, true);

        classInformation.addField(field);

        assertThat(classInformation.hasField(wanted)).isFalse();
    }

    @Test
    void findFieldByName() {
        FieldInformation field = new FieldInformation(FIELD, null, true);
        FieldInformation wanted = new FieldInformation(FIELD, null, true);

        classInformation.addField(field);

        Optional<FieldInformation> result = classInformation.findField(wanted);

        result.ifPresentOrElse(fieldInformation -> assertThat(fieldInformation).isEqualTo(field),
                () -> fail("Field not found"));

    }
}