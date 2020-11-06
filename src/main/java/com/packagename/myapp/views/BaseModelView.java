package com.packagename.myapp.views;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.views.customComponents.manageButtons.ManageButtons;
import com.packagename.myapp.views.layouts.VerticalLayoutAuthRestricted;

import javax.annotation.PostConstruct;

public abstract class BaseModelView<T extends BaseModel> extends VerticalLayoutAuthRestricted {

    protected final ManageButtons<T> manageButtons;

    public BaseModelView(Class<T> clazz) {
        manageButtons = new ManageButtons<>(clazz);
    }

    @PostConstruct
    private void init() {
        addHeader();
        addManageButtons();
        addGrid();
        configureManageButtons();
    }

    protected abstract void addHeader();

    protected void addManageButtons() {
        if (loginService.getAuthenticatedUser().isNotAdmin()) {
            return;
        }

        add(manageButtons);
    }


    protected abstract void addGrid();

    protected abstract void configureManageButtons();

    protected abstract void updateGrid();

}
