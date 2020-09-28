package com.packagename.myapp.models;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel {
    public abstract int getId();

    public abstract String getName();

    public abstract BaseModel getParent();

    public abstract List<BaseModel> getChildren();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass() || this.getName() == null) {
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

    @Override
    public String toString() {
        return "BaseModel{" +
                "id='" + getId() + "'" +
                "name='" + getName() + "'" +
                "class='" + getClass() + "'" +
                '}';

    }

    public String toShortString() {
        return "id= '" + getId() + " - " + "name= " + getName();
    }

    public String getEntityTableName() {
        return StringUtils.capitalize(this.getClass().getAnnotation(Table.class).name());
    }

    public List<BaseModel> getParentsTree() {
        ArrayList<BaseModel> parentsTree = new ArrayList<>();

        BaseModel currentParent = getParent();
        while (currentParent != null) {
            parentsTree.add(currentParent);

            currentParent = currentParent.getParent();
        }

        return parentsTree;
    }
}