package com.packagename.myapp.services;

import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;


@Service
public class NotificationService {

    private int duration = 5000;
    private Notification.Position position = Notification.Position.BOTTOM_END;

    public NotificationService() {
    }

    public Notification info(String message){
        return showThemedNotification(message, "info");
    }

    public Notification error(String message){
        return showThemedNotification(message, "error");
    }

    public Notification alert(String message){
        return error(message);
    }

    public Notification success(String message){
        return showThemedNotification(message, "success");
    }

    public Notification warning(String message){
        return showThemedNotification(message, "warning");
    }

    private Notification showThemedNotification(String message, String theme){
        Notification notification = new Notification(message, duration, position);
        notification.getElement().getThemeList().add(theme);

        notification.open();

        return notification;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPosition(Notification.Position position) {
        this.position = position;
    }
}
