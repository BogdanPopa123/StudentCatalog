package com.packagename.myapp.models;

import com.packagename.myapp.Application;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

public abstract class BaseModel extends ParentableModel {
    private final static Logger logger = LogManager.getLogger(BaseModel.class);

    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

//    public void setParent(BaseModel parent) {
//        getParentSetter().ifPresent(method -> {
//            try {
//                method.invoke(parent);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                logger.error("Failed to invoke parent setter", e);
//            }
//        });
//    }

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

    // TODO: 23-Oct-20 Generify fields and configure binder
    @SuppressWarnings("unchecked")
    public List<Component> getPropertiesField() {
        List<Class<?>> acceptedReturnType = Arrays.asList(String.class, Integer.class);

        ArrayList<Component> fields = new ArrayList<>();

        ReflectionUtils.getAllMethods(this.getClass(), ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get")).stream()
                .filter(method -> acceptedReturnType.stream().anyMatch(c -> c.isAssignableFrom(method.getReturnType())))
                .filter(method -> ReflectionUtils.getAllFields(this.getClass()).stream().anyMatch(field -> field.getName().equalsIgnoreCase(method.getName().substring(3))))
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
                    logger.warn("Not a valid field type: " + propertyName);
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

    public boolean existsByName(String name) {
        return StreamSupport.stream(this.getRepository().findAll().spliterator(), false).noneMatch(t -> t.getName().equals(name) && t.getId() == this.getId());
    }
}