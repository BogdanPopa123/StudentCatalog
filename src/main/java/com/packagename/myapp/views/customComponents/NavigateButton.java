package com.packagename.myapp.views.customComponents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;

public class NavigateButton extends Button {

    public NavigateButton(String text, Class<? extends Component> locationClass) {
        super(text);

        this.addClickListener(event -> {
            if (lastPressedButton != null) {
                lastPressedButton.removeClassName("active-link");
            }
            this.addClassName("active-link");
            UI.getCurrent().navigate(locationClass);

            lastPressedButton = this;
        });
    }

    private static NavigateButton lastPressedButton;


}
