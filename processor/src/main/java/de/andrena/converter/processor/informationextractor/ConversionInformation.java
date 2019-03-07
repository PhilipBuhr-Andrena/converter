/*
 * Copyright (c) 2019. andrena objects
 */

package de.andrena.converter.processor.informationextractor;

import java.util.ArrayList;
import java.util.List;

public class ConversionInformation {
    private ClassInformation model;
    private String name;
    private List<ClassInformation> sources = new ArrayList<>();

    public ConversionInformation() {
    }

    public ClassInformation getModel() {
        return model;
    }

    void setModel(ClassInformation model) {

        this.model = model;
    }

    public List<ClassInformation> getSources() {
        return sources;
    }

    void addSource(ClassInformation source) {
        sources.add(source);
    }

    public String getPackageName() {
        return this.model.getPackageName();
    }

    void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
