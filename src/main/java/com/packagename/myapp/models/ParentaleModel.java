package com.packagename.myapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.packagename.myapp.models.annotations.Parent;
import com.packagename.myapp.views.customComponents.HierarchicalCombobox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.ReflectionUtils;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ParentaleModel {
    private final static Logger logger = LogManager.getLogger(ParentaleModel.class);

    // TODO: 09-Oct-20 Use @Parent for getting parent
    @JsonIgnore
    public abstract BaseModel getParent();

    // TODO: 12-Oct-20 Setter using reflection
    public abstract void setParent(BaseModel parent);

    @JsonIgnore
    public abstract List<BaseModel> getChildren();

    @JsonIgnore
    public List<BaseModel> getParentsTree() {
        ArrayList<BaseModel> parentsTree = new ArrayList<>();

        BaseModel currentParent = getParent();
        while (currentParent != null) {
            parentsTree.add(currentParent);

            currentParent = currentParent.getParent();
        }

        return parentsTree;
    }

    @JsonIgnore
    public List<BaseModel> getEmptyParentsTree() {
        ArrayList<BaseModel> parentsTree = new ArrayList<>();

        BaseModel currentParent = getParentNewInstance();
        while (currentParent != null) {
            parentsTree.add(currentParent);

            currentParent = currentParent.getParentNewInstance();
        }

        return parentsTree;
    }

    @JsonIgnore
    public List<CrudRepository<? extends BaseModel, Integer>> getParentsRepository() {
        return getEmptyParentsTree().stream().map(BaseModel::getRepository).collect(Collectors.toList());
    }

    // TODO: 12-Oct-20 Add change value listener to filter valid parent items
    @JsonIgnore
    public ArrayList<HierarchicalCombobox> getParentTreeCombobox() {
        ArrayList<HierarchicalCombobox> hierarchicalComboboxes = new ArrayList<>();

        BaseModel current = getParentNewInstance();
        while (current != null) {
            String propertyName = current.getEntityTableNameCapitalized();
            CrudRepository<? extends BaseModel, Integer> repository = current.getRepository();

            HierarchicalCombobox field = new HierarchicalCombobox(propertyName);

            field.setItemLabelGenerator(BaseModel::getName);
            field.setItems(Lists.newArrayList(repository.findAll()));
            field.setPlaceholder(current.getEntityTableNameCapitalized());
            field.setWidth("300px");
            field.setRequired(true);
            field.setAllowCustomValue(false);
            field.setPreventInvalidInput(true);

            if (!hierarchicalComboboxes.isEmpty()) {
                field.setChildComboBox(Iterables.getLast(hierarchicalComboboxes));
            }

            hierarchicalComboboxes.add(field);

            current = current.getParentNewInstance();
        }

        return hierarchicalComboboxes;
    }

    @JsonIgnore
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

    @JsonIgnore
    public Optional<? extends Class<?>> getParentClass() {
        Optional<Field> parentField = getParentField();

        return parentField.isPresent() ? Optional.of(parentField.get().getType()) : Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    protected Optional<Field> getParentField() {
        return ReflectionUtils.getAllFields(this.getClass(), ReflectionUtils.withAnnotation(Parent.class)).stream().findFirst();
    }

    @SuppressWarnings("unchecked")
    @JsonIgnore
    private Optional<Method> getParentSetter() {
        return ReflectionUtils.getAllMethods(this.getClass(),
                ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withName("set" + getParentNewInstance().getEntityTableNameCapitalized()))
                .stream().findFirst();
    }

    public boolean hasParent() {
        return getParentField().isPresent();
    }

}
