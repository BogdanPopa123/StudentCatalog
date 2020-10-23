package com.packagename.myapp.services;

import com.packagename.myapp.Application;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {
    private int duration = 5000;
    private Notification.Position position = Notification.Position.TOP_CENTER;

    public NotificationService() {
    }

    public Notification info(String message) {
        return showThemedNotification(message, Theme.INFO);
    }

    public Notification error(String message) {
        return showThemedNotification(message, Theme.ERROR);
    }

    public Notification alert(String message) {
        return error(message);
    }

    public Notification success(String message) {
        return showThemedNotification(message, Theme.SUCCESS);
    }

    private Notification showThemedNotification(String message, Theme theme) {
        Notification notification = new Notification(message, duration, position);
        notification.getElement().getThemeList().add(theme.getValue());

        notification.open();

        return notification;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPosition(Notification.Position position) {
        this.position = position;
    }

    public enum Theme {
        INFO("primary"),
        SUCCESS("success"),
        ERROR("error");


        private final String value;

        Theme(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static NotificationService getService(){
        return Application.context.getBean(NotificationService.class);
    }
}
