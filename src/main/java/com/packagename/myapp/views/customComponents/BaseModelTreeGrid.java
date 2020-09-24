package com.packagename.myapp.views.customComponents;

import com.google.common.collect.Lists;
import com.packagename.myapp.models.BaseModel;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public class BaseModelTreeGrid extends TreeGrid<BaseModel> {

    private List<CrudRepository<? extends BaseModel, Integer>> repositories;

    public BaseModelTreeGrid(List<CrudRepository<? extends BaseModel, Integer>> repositories) {
        super();
        this.repositories = repositories;
        this.setGridItemsFromRepositories(repositories);
    }

    public BaseModelTreeGrid() {
        super();
    }

    public void updateData() {
        this.setGridItemsFromRepositories(repositories);
    }

    public void updateData(List<CrudRepository<? extends BaseModel, Integer>> repositories) {
        this.setGridItemsFromRepositories(repositories);
    }

    public void setGridItemsFromRepositories(List<CrudRepository<? extends BaseModel, Integer>> repositories) {
        TreeData<BaseModel> treeData = new TreeData<>();

        repositories.forEach(crudRepository -> addItemsToTreeDataFromRepository(crudRepository, treeData));

        TreeDataProvider<BaseModel> treeDataProvider = new TreeDataProvider<>(treeData);

        this.setDataProvider(treeDataProvider);
    }

    private void addItemsToTreeDataFromRepository(CrudRepository<? extends BaseModel, Integer> repository, TreeData<BaseModel> treeData) {
        addItemsToTreeData(Lists.newArrayList(repository.findAll()), treeData);
    }

    private void addItemsToTreeData(List<BaseModel> items, TreeData<BaseModel> treeData) {
        items.forEach(item -> treeData.addItem(item.getParent(), item));
    }

    public List<CrudRepository<? extends BaseModel, Integer>> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<CrudRepository<? extends BaseModel, Integer>> repositories) {
        this.repositories = repositories;
    }

    public void expandAll() {
        repositories.forEach(repository -> repository.findAll().forEach(this::expand));
    }
}
