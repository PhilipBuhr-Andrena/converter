package de.andrena.converter.processor.informationextractor;

public class FieldInformation {

    private String name;
    private boolean isPublic;
    private String mapping;

    public FieldInformation(String name, boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
        this.mapping = name;
    }

    public FieldInformation(String name, boolean isPublic, String mapping) {
        this.name = name;
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
}
