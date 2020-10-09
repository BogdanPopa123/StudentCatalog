package com.packagename.myapp.models;

import com.google.common.collect.Lists;
import com.packagename.myapp.Application;
import com.packagename.myapp.models.annotations.Parent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BaseModel {
    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

    // TODO: 09-Oct-20 Use @Parent for getting parent
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

    public List<ComboBox<BaseModel>> getParentTreeCombobox() {
        ArrayList<ComboBox<BaseModel>> fields = new ArrayList<>();

        BaseModel current = getParentNewInstance();
        while (current != null) {
            String propertyName = current.getEntityTableNameCapitalized();
            CrudRepository<? extends BaseModel, Integer> repository = current.getRepository();

            ComboBox<BaseModel> field = new ComboBox<>(propertyName);
            field.setItemLabelGenerator(BaseModel::getName);
            field.setItems(Lists.newArrayList(repository.findAll()));

            fields.add(field);

            current = current.getParentNewInstance();
        }

        return fields;
    }

    private BaseModel getParentNewInstance() {
        Optional<Field> field = Arrays.stream(this.getClass().getDeclaredFields()).filter(f -> f.getAnnotation(Parent.class) != null).findFirst();

        if (field.isPresent()) {
            try {
                return (BaseModel) field.get().getType().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public List<Component> getPropertiesField() {
        List<Class<?>> acceptedReturnType = Arrays.asList(String.class, Integer.class, BaseModel.class);

        ArrayList<Component> fields = new ArrayList<>();

        Arrays.stream(this.getClass().getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> method.getName().matches("get(.*)") && acceptedReturnType.stream().anyMatch(c -> c.isAssignableFrom(method.getReturnType())))
                .filter(method -> Arrays.stream(this.getClass().getDeclaredFields()).anyMatch(field -> field.getName().equalsIgnoreCase(method.getName().substring(3))))
                .forEach(method -> {
                    Class<?> returnType = method.getReturnType();
                    String propertyName = method.getName().substring(3);

                    if (returnType.equals(String.class)) {
                        fields.add(new TextField(propertyName));
                        return;
                    }

                    if (returnType.equals(Integer.class)) {
                        fields.add(new NumberField(propertyName));
                        return;
                    }

                    if (BaseModel.class.isAssignableFrom(returnType)) {
                        fields.addAll(this.getParentTreeCombobox());
                        return;
                    }

                    fields.add(new Text(propertyName + ": Undefined type"));
                });

        return fields;
    }

    public String getRepositoryName() {
        return getEntityTableName() + "Repository";
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseModel> CrudRepository<T, Integer> getRepository() {
        return (CrudRepository<T, Integer>) Application.context.getBean(getRepositoryName());
    }
}