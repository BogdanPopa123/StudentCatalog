package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailsDialog extends Dialog {
    private final BaseModel item;
    private List<BaseModel> parentsTree = new ArrayList<>();

    public DetailsDialog(BaseModel item) {
        this.item = item;
        setDetails();
    }

    private void setDetails() {
        int id = item.getId();
        String name = item.getName();

        VerticalLayout itemDetails = new VerticalLayout(
                new H5("Id: " + id),
                new H5("Name: " + name)
        );

        item.getParentsTree().forEach(parent -> {
            String propertyName = parent.getEntityTableName();
            String propertyValue = parent.getName();

            String property = String.format("%s: %s", propertyName, propertyValue);

            H5 propertyH5 = new H5(property);
            itemDetails.add(propertyH5);
        });

        String header = item.getEntityTableName();

        Button close = new Button("Close");
        close.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addClickListener(click -> this.close());

        VerticalLayout details = new VerticalLayout(
                new H2(header),
                new HtmlComponent("hr"),
                itemDetails,
                close
        );

        details.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(details);

    }


}
