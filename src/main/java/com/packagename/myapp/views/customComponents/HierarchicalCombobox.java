package com.packagename.myapp.views.customComponents;

import com.packagename.myapp.models.BaseModel;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HierarchicalCombobox extends ComboBox<BaseModel> {

    HierarchicalCombobox parent;
    HierarchicalCombobox child;

    public HierarchicalCombobox(String propertyName) {
        super(propertyName);

        this.addValueChangeListener(this::changeValueEvent);
    }

    private void changeValueEvent(ComponentValueChangeEvent<ComboBox<BaseModel>, BaseModel> event) {
        BaseModel value = event.getValue();

        if (value == null) {
            return;
        }

        this.getParentComboBox().ifPresent(parent -> parent.setValue(value.getParent()));

        getChildComboBox().ifPresent(child -> {
            List<BaseModel> children = event.getValue().getChildren();

            child.setItems(children);

            child.getChildComboBox().ifPresent(childOfChild -> {
                ArrayList<BaseModel> list = children.stream().map(BaseModel::getChildren).collect(ArrayList::new, List::addAll, List::addAll);

                childOfChild.setItems(list);
            });
        });

        this.setValue(value);
    }


    public Optional<HierarchicalCombobox> getParentComboBox() {
        return Optional.ofNullable(parent);
    }

    public void setParentCombBox(HierarchicalCombobox parent) {
        this.parent = parent;

        if (!parent.getChildComboBox().isPresent() || !this.equals(parent.getChildComboBox().get())) {
            parent.setChildComboBox(this);
        }
    }

    public Optional<HierarchicalCombobox> getChildComboBox() {
        return Optional.ofNullable(child);
    }

    public void setChildComboBox(HierarchicalCombobox child) {
        this.child = child;

        if (!child.getParentComboBox().isPresent() || !this.equals(child.getParentComboBox().get())) {
            child.setParentCombBox(this);
        }
    }

    public Optional<HierarchicalCombobox> getFirstParent() {
        Optional<HierarchicalCombobox> parent = this.getParentComboBox();

        while (parent.isPresent() && parent.get().hasParentComboBox()) {
            parent = parent.get().getParentComboBox();
        }

        return parent;
    }

    public Optional<HierarchicalCombobox> getLastChild() {
        Optional<HierarchicalCombobox> child = this.getChildComboBox();

        while (child.isPresent() && child.get().hasChildComboBox()) {
            child = child.get().getParentComboBox();
        }

        return child;
    }

    public boolean hasParentComboBox() {
        return this.getParent().isPresent();
    }

    public boolean hasChildComboBox() {
        return this.getChildComboBox().isPresent();
    }


}
