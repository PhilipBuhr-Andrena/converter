/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import org.junit.jupiter.api.Test;

import javax.lang.model.type.TypeMirror;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FieldInformationTest {

    private static final String NAME = "name";
    private static final String MAPPING = "mapping";
    private static final String OTHER_NAME = "otherName";

    @Test
    void haveSameMappingIfFieldsHaveSameName() {
        FieldInformation wanted = new FieldInformation(NAME, null, true);
        FieldInformation other = new FieldInformation(NAME, null, true);

        assertThat(wanted.hasSameMapping(other)).isTrue();
    }

    @Test
    void dontHaveSameMappingIfNamesAreDiffernt() {
        FieldInformation first = new FieldInformation(NAME, null, true);
        FieldInformation second = new FieldInformation(OTHER_NAME, null, true);

        assertThat(first.hasSameMapping(second)).isFalse();
        assertThat(second.hasSameMapping(first)).isFalse();
    }

    @Test
    void haveSameMappingIfSameMapping() {
        FieldInformation first = new FieldInformation(NAME, null, true, MAPPING);
        FieldInformation second = new FieldInformation(OTHER_NAME, null, true, MAPPING);

        assertThat(first.hasSameMapping(second)).isTrue();
        assertThat(second.hasSameMapping(first)).isTrue();
    }

    @Test
    void haveSameMappingIsReflexive() {
        FieldInformation fieldInformation = new FieldInformation(NAME, null, true, MAPPING);

        assertThat(fieldInformation.hasSameMapping(fieldInformation)).isTrue();
    }

    @Test
    void haveSameMappingIfMappingOfOneEqualsFieldNameOfOther() {
        FieldInformation first = new FieldInformation(NAME, null, true);
        FieldInformation second = new FieldInformation(OTHER_NAME, null, true, NAME);

        assertThat(first.hasSameMapping(second)).isTrue();
        assertThat(second.hasSameMapping(first)).isTrue();
    }

    @Test
    void getterAndSetterIsFieldNameIfPublic() {
        FieldInformation fieldInformation = new FieldInformation(NAME, null, true);

        assertThat(fieldInformation.findGetter()).isEqualTo(NAME);
        assertThat(fieldInformation.findSetter()).isEqualTo(NAME);
    }

    @Test
    void getterAndSetterAreMethodNamesIfNonPublic() {
        FieldInformation fieldInformation = new FieldInformation(NAME, null, false);

        assertThat(fieldInformation.findSetter()).isEqualTo("setName");
        assertThat(fieldInformation.findGetter()).isEqualTo("getName()");
    }

    @Test
    void haveDifferentType() {
        FieldInformation first = new FieldInformation(NAME, mock(TypeMirror.class), true);
        FieldInformation second = new FieldInformation(NAME, mock(TypeMirror.class), true);

        assertThat(first.hasDifferentType(second)).isTrue();
        assertThat(second.hasDifferentType(first)).isTrue();
    }

    @Test
    void haveSameType() {
        TypeMirror type = mock(TypeMirror.class);
        FieldInformation first = new FieldInformation(NAME, type, true);
        FieldInformation second = new FieldInformation(NAME, type, true);

        assertThat(first.hasDifferentType(second)).isFalse();
        assertThat(second.hasDifferentType(first)).isFalse();

    }
}