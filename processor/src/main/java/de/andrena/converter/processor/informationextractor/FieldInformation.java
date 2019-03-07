package de.andrena.converter.processor.informationextractor;

import javax.lang.model.type.TypeMirror;

public class FieldInformation {

    private String name;
    private TypeMirror type;
    private boolean isPublic;
    private String mapping;

    public FieldInformation(String name, TypeMirror type, boolean isPublic) {
        this.name = name;
        this.type = type;
        this.isPublic = isPublic;
        this.mapping = name;
    }

    public FieldInformation(String name, TypeMirror type, boolean isPublic, String mapping) {
        this.name = name;
        this.type = type;
        this.isPublic = isPublic;
        this.mapping = mapping;
    }



    public String getName() {
        return name;
    }

    public String findSetter() {
        if (isPublic()) {
            return name;
        }
        return "set" + capitalisedName();
    }

    public String findGetter() {
        if (isPublic()) {
            return name;
        }
        return "get" + capitalisedName() + "()";
    }

    private String capitalisedName() {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    boolean hasSameMapping(FieldInformation other) {
        return mapping.equals(other.getMapping()
        );
    }

    public boolean isPublic() {
        return isPublic;
    }

    String getMapping() {
        return mapping;
    }

    public TypeMirror getType() {
        return type;
    }

    public boolean hasDifferentType(FieldInformation fromField) {
        return !type.equals(fromField.getType());
    }
}
