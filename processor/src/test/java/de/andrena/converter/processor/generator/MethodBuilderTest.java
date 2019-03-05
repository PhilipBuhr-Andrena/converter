package de.andrena.converter.processor.generator;

import com.squareup.javapoet.ClassName;
import de.andrena.converter.processor.informationextractor.ClassInformation;
import de.andrena.converter.processor.informationextractor.FieldInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

@Disabled
class MethodBuilderTest {

    private static final String NAME = "name";
    private static final String CLASS_NAME = "className";
    private StatementBuilder statementBuilder;
    private ClassInformation to;
    private ClassInformation from;
    private MethodBuilder methodBuilder;
    private FieldInformation fromField;
    private FieldInformation toField;
    private FieldInformation additionalField;

    @BeforeEach
    void setUp() {
        statementBuilder = mock(StatementBuilder.class);

        to = mock(ClassInformation.class);

        toField = new FieldInformation(NAME, true);
        when(to.getFields()).thenAnswer(invocation -> Set.of(toField));
        when(to.getSimpleName()).thenReturn(CLASS_NAME);

        from = new ClassInformation();
        fromField = new FieldInformation(NAME, true);
        from.addField(fromField);

        additionalField = new FieldInformation("otherName", true);
        from.addField(additionalField);

        methodBuilder = new MethodBuilder(statementBuilder);
    }

    @Test
    void findsCorrespondingFields() {
        methodBuilder.generateConversionMethod(to, from);
        verify(statementBuilder).mapField(toField, fromField);
        verify(statementBuilder, never()).mapField(toField, additionalField);
    }
}