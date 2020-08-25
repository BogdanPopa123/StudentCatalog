package com.packagename.myapp.models;

import java.util.Collection;

public interface UniversityModel {

    int getId();

    String getName();

    UniversityModel getParent();

    Collection<UniversityModel> getChildren();
}
