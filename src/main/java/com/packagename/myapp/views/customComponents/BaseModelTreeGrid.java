package com.packagename.myapp.views.customComponents;

import com.google.common.collect.Sets;
import com.packagename.myapp.models.BaseModel;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;
import com.vaadin.flow.data.provider.hierarchy.TreeDataProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BaseModelTreeGrid extends TreeGrid<BaseModel> {
    private static final Logger logger = LogManager.getLogger(BaseModelTreeGrid.class);

    private List<CrudRepository<? extends BaseModel, Integer>> repositories = new ArrayList<>();
    private Set<BaseModel> items = new LinkedHashSet<>();

    public BaseModelTreeGrid(List<CrudRepository<? extends BaseModel, Integer>> repositories) {
        super();
        this.repositories = repositories;
//        this.setGridItemsFromRepositories(repositories);
        updateDataAndExpandAll();
    }

    public BaseModelTreeGrid(Class<? extends BaseModel> clazz) {
        super();
        try {
            this.repositories = clazz.getDeclaredConstructor().newInstance().getHierarchicalRepositories();
            updateDataAndExpandAll();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("Failed to load parents tree grid", e);
        }

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

    // TODO: 09-Dec-20 Use with a hash set of items
//    public void setGridItems(Set<BaseModel> items) {
//        TreeData<BaseModel> treeData = new TreeData<>();
//
//        addItemsToTreeData(items, treeData);
//
//        TreeDataProvider<BaseModel> treeDataProvider = new TreeDataProvider<>(treeData);
//
//        this.setDataProvider(treeDataProvider);
//    }

    private void addItemsToTreeDataFromRepository(CrudRepository<? extends BaseModel, Integer> repository, TreeData<BaseModel> treeData) {
        items = Sets.newHashSet(repository.findAll());

        addItemsToTreeData(items, treeData);
    }

    private void addItemsToTreeData(Set<? extends BaseModel> items, TreeData<BaseModel> treeData) {
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
//        items.forEach(this::expand);
    }

    public void updateDataAndExpandAll() {
        // TODO: 09-Dec-20 update a set instead of requesting new items from db
        // TODO: 09-Dec-20  trailing items on delete or create ?

//        items.clear();
//        repositories.forEach(repo -> repo.findAll().forEach(item -> items.add(item)));
//        repositories.get(0).findAll().forEach(item -> items.add(item));
//        repositories.remove(null);
//        this.getDataProvider().refreshAll();
//        this.setTreeData(null);
//        this.setGridItems(items);


        updateData();
        expandAll();
    }
}
