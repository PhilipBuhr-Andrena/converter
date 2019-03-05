package de.andrena.converter.processor.informationextractor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FieldInformationTest {

    private static final String NAME = "name";
    private static final String MAPPING = "mapping";
    private static final String OTHER_NAME = "otherName";

    @Test
    void haveSameMappingIfFieldsHaveSameName() {
        FieldInformation wanted = new FieldInformation(NAME, true);
        FieldInformation other = new FieldInformation(NAME, true);

        assertThat(wanted.hasSameMapping(other)).isTrue();
    }

    @Test
    void dontHaveSameMappingIfNamesAreDiffernt() {
        FieldInformation first = new FieldInformation(NAME, true);
        FieldInformation second = new FieldInformation(OTHER_NAME, true);

        assertThat(first.hasSameMapping(second)).isFalse();
        assertThat(second.hasSameMapping(first)).isFalse();
    }

    @Test
    void haveSameMappingIfSameMapping() {
        FieldInformation first = new FieldInformation(NAME, true, MAPPING);
        FieldInformation second = new FieldInformation(OTHER_NAME, true, MAPPING);

        assertThat(first.hasSameMapping(second)).isTrue();
        assertThat(second.hasSameMapping(first)).isTrue();
    }

    @Test
    void haveSameMappingIsReflexive() {
        FieldInformation fieldInformation = new FieldInformation(NAME, true, MAPPING);

        assertThat(fieldInformation.hasSameMapping(fieldInformation)).isTrue();
    }

    @Test
    void haveSameMappingIfMappingOfOneEqualsFieldNameOfOther() {
        FieldInformation first = new FieldInformation(NAME, true);
        FieldInformation second = new FieldInformation(OTHER_NAME, true, NAME);

        assertThat(first.hasSameMapping(second)).isTrue();
        assertThat(second.hasSameMapping(first)).isTrue();
    }

    @Test
    void getterAndSetterIsFieldNameIfPublic() {
        FieldInformation fieldInformation = new FieldInformation(NAME, true);

        assertThat(fieldInformation.findGetter()).isEqualTo(NAME);
        assertThat(fieldInformation.findSetter()).isEqualTo(NAME);
    }

    @Test
    void getterAndSetterAreMethodNamesIfNonPublic() {
        FieldInformation fieldInformation = new FieldInformation(NAME, false);

        assertThat(fieldInformation.findSetter()).isEqualTo("setName");
        assertThat(fieldInformation.findGetter()).isEqualTo("getName()");
    }
}