package de.andrena.converter.processor.informationextractor;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassInformation {

    private ClassName className;
    private List<FieldInformation> fields = new ArrayList<>();

    public ClassInformation() {
    }

    public ClassInformation(ClassName className) {
        this.className = className;
    }

    public ClassName getClassName() {
        return className;
    }

    public String getSimpleName() {
        return className.simpleName();
    }

    public String getPackageName() {
        return this.className.packageName();
    }

    public void addField(FieldInformation field) {
        fields.add(field);
    }

    public List<FieldInformation> getFields() {
        return fields;
    }

    public boolean hasField(FieldInformation fieldInformation) {
        long count = this.fields.stream()
                .filter(fieldInformation::hasSameMapping)
                .count();
        return count > 0;
    }

    public Optional<FieldInformation> findField(FieldInformation toField) {
        return this.fields.stream()
                .filter(toField::hasSameMapping)
                .findFirst();
    }
}
