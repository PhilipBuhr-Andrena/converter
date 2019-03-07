/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.generator;

import de.andrena.converter.processor.informationextractor.ConversionMethods;
import de.andrena.converter.processor.informationextractor.FieldInformation;

class StatementBuilder {

    String mapField(FieldInformation toField, FieldInformation fromField, ConversionMethods conversionMethods) {
        return "result." +
                toField.findSetter() +
                createAssignment(toField, fromField, conversionMethods);
    }

    private String createAssignment(FieldInformation toField, FieldInformation fromField, ConversionMethods conversionMethods) {
        String accessor = convertAccessor(toField, fromField, conversionMethods);
        if (toField.isPublic()) {
            return " = " + accessor;
        }
        return "(" + accessor + ")";
    }

    private String convertAccessor(FieldInformation toField, FieldInformation fromField, ConversionMethods conversionMethods) {
        String accessor = "from." + fromField.findGetter();
        if (toField.hasDifferentType(fromField)) {
            String adapterName = conversionMethods.findAdapter(toField.getType(), fromField.getType());
            accessor = adapterName + "(" + accessor + ")";
        }
        return accessor;
    }
}
