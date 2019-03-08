/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.example;

import org.junit.jupiter.api.Test;
import some.otherpackage.FooDto2;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class FooTest {

    private static final String NAME = "name";

    @Test
    void converterCreatesTargetPojo() {
        assertThat(FooConverter.createFoo(new FooDto())).isNotNull();
        assertThat(FooConverter.createFooDto(new Foo())).isNotNull();
    }

    @Test
    void convertsFieldsWithSamePublicPrimitiveTypeAndName() {
        FooDto fooDto = new FooDto();
        fooDto.name = NAME;
        Foo foo = FooConverter.createFoo(fooDto);
        assertThat(foo.name).isEqualTo(NAME);
    }

    @Test
    void ignoresFieldsWhichAreNotInBothClasses() {
        FooWithExtraField extraFoo = new FooWithExtraField();
        extraFoo.name = NAME;
        extraFoo.extraField = 5;

        Foo foo = FooConverter.createFoo(extraFoo);
        assertThat(foo.name).isEqualTo(NAME);
        List<String> fooFields = Arrays.stream(Foo.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        assertThat(fooFields).doesNotContain("extraField");
    }

    @Test
    void usesGetterIfFieldNotPublic() {
        FooWithPrivateField fooWithPrivateField = new FooWithPrivateField(NAME);

        Foo foo = FooConverter.createFoo(fooWithPrivateField);
        assertThat(foo.name).isEqualTo(NAME);
    }

    @Test
    void mapsFieldsWithDifferentNamesViaAnnotation() {
        FooWithDifferentFieldName fooWithDifferentFieldName = new FooWithDifferentFieldName();
        fooWithDifferentFieldName.otherName = NAME;

        Foo foo = FooConverter.createFoo(fooWithDifferentFieldName);
        assertThat(foo.name).isEqualTo(NAME);
    }

    /*@Test
    @Disabled
    void convertsNestedAnnotatedClasses() {
        FooDtoWrapper fooDtoWrapper = new FooDtoWrapper(new FooDto2(NAME));

        FooWrapper fooWrapper = FooWrapperConverter.createFooWrapper(fooDtoWrapper);

        assertThat(fooWrapper.foo.name).isEqualTo(NAME);
    }*/

    @Test
    void usesConversionAdaptersToConvertField() {
        FooWithDate fooWithDate = new FooWithDate(LocalDate.of(2019, 3, 4));
        FooWithDateString fooWithDateString = DateFooConverter.createFooWithDateString(fooWithDate);

        assertThat(fooWithDateString.date).isEqualTo("04/03/2019");
    }

    @Test
    void fooFromOtherPackage() {
        FooDto2 fooDto2 = new FooDto2();
        fooDto2.name = NAME;

        Foo foo = FooConverter.createFoo(fooDto2);

        assertThat(foo.name).isEqualTo(NAME);
    }
}