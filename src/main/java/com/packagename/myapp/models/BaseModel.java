package com.packagename.myapp.models;

import java.util.List;

public abstract class BaseModel {
    public abstract int getId();

    public abstract String getName();

    public abstract BaseModel getParent();

    public abstract List<BaseModel> getChildren();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        BaseModel other = (BaseModel) obj;

        return other.getId() == this.getId() && other.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 53 * hash + (this.getName() != null ? this.getName().hashCode() : 0);
        hash = 53 * hash + this.getId();

        return hash;
    }
}
