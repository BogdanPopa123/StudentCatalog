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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public abstract class BaseModel {
    private final static Logger logger = LogManager.getLogger(BaseModel.class);

    public abstract int getId();

    public abstract String getName();

    public abstract void setName(String name);

    // TODO: 09-Oct-20 Use @Parent for getting parent
    public abstract BaseModel getParent();

    // TODO: 12-Oct-20 Setter using reflection
    public abstract void setParent(BaseModel parent);

//    public void setParent(BaseModel parent) {
//        getParentSetter().ifPresent(method -> {
//            try {
//                method.invoke(parent);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                logger.error("Failed to invoke parent setter", e);
//            }
//        });
//    }

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
            field.setPlaceholder(current.getEntityTableNameCapitalized());

            fields.add(field);

            current = current.getParentNewInstance();
        }

        return fields;
    }

    public BaseModel getParentNewInstance() {
        Optional<? extends Class<?>> parentClass = getParentClass();

        if (parentClass.isPresent()) {
            try {
                return (BaseModel) parentClass.get().getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("Failed to create new parent instance: " + parentClass.get().getSimpleName(), e);
            }
        }
        return null;
    }

    public Optional<? extends Class<?>> getParentClass() {
        Optional<Field> parentField = getParentField();

        return parentField.isPresent() ? Optional.of(parentField.get().getType()) : Optional.empty();
    }

    public List<Component> getPropertiesField() {
        List<Class<?>> acceptedReturnType = Arrays.asList(String.class, Integer.class, BaseModel.class);

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
                    logger.warn("Not a valid field type: "+propertyName);
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

    public boolean hasParent() {
        return getParentField().isPresent();
    }

    private Optional<Field> getParentField() {
        return ReflectionUtils.getAllFields(this.getClass(), ReflectionUtils.withAnnotation(Parent.class)).stream().findFirst();
    }

    private Optional<Method> getParentSetter() {
        return ReflectionUtils.getAllMethods(this.getClass(),
                ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withName("set" + getParentNewInstance().getEntityTableNameCapitalized()))
                .stream().findFirst();
    }

    public boolean checkNameAvailability(String name){
        return StreamSupport.stream(this.getRepository().findAll().spliterator(), false).noneMatch(t -> t.getName().equals(name));
    }

}