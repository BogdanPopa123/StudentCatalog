package com.packagename.myapp.models;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseModel {
    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

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
        return this.getClass().getAnnotation(Table.class).name();
    }

    public String getEntityTableNameCapitalized() {
        return StringUtils.capitalize(getEntityTableName());
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

    public List<Component> getPropertiesField() {
        List<Class<?>> acceptedReturnType = Arrays.asList(String.class, Integer.class, BaseModel.class);

        return Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.getName().matches("get(.*)") && acceptedReturnType.stream().anyMatch(c -> c.equals(method.getReturnType())))
                .filter(method -> Arrays.stream(this.getClass().getFields()).anyMatch(field -> field.getName().equals(method.getName().substring(3))))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(method -> {
                    Class<?> returnType = method.getReturnType();
                    String propertyName = method.getName().substring(3);

                    if (returnType.equals(String.class)) {
                        return new TextField(propertyName);
                    }

                    if (returnType.equals(Integer.class)) {
                        return new NumberField(propertyName);
                    }

                    if (returnType.equals(BaseModel.class)) {
                        return new ComboBox<BaseModel>();
                    }

                    return new Text(propertyName + ": Undefined type");
                }).collect(Collectors.toList());
    }
}