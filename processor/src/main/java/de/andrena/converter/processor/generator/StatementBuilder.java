package de.andrena.converter.processor.generator;

import de.andrena.converter.processor.informationextractor.FieldInformation;

class StatementBuilder {
    String mapField(FieldInformation toField, FieldInformation fromField) {
        StringBuilder result = new StringBuilder()
                .append("result.")
                .append(toField.findSetter())
                .append(createAssignment(toField, fromField));
        return result.toString();
    }

    private String createAssignment(FieldInformation toField, FieldInformation fromField) {
        if (toField.isPublic()) {
            return " = from." + fromField.findGetter();
        }
        return "(from." + fromField.findGetter() + ")";
    }
}
