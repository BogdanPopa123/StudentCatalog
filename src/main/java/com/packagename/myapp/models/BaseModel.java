package com.packagename.myapp.models;

import java.util.List;
import java.util.Set;

public interface BaseModel {
    int getId();

    String getName();

    BaseModel getParent();

    List<BaseModel> getChildren();
}
