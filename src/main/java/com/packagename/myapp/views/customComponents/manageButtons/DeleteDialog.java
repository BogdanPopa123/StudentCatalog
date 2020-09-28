package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.packagename.myapp.services.NotificationService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

public class DeleteDialog extends ConfirmDialog {

    private static final Logger logger = LogManager.getLogger(DeleteDialog.class);

    private final BaseModel item;
    private final NotificationService notificationService;
    private final CrudRepository<? extends BaseModel, Integer> repository;

    public DeleteDialog(BaseModel item,
                        CrudRepository<? extends BaseModel, Integer> repository,
                        NotificationService notificationService) {
        super(getMessage(item));

        this.item = item;
        this.repository = repository;
        this.notificationService = notificationService;
    }

    private static String getMessage(BaseModel item) {
        return "Are you sure you want to delete " + item.getEntityTableName() + " : " + "<br><br>" +
                item.toShortString() + " ?";
    }

    public void confirm(ClickEvent<Button> event) {
        repository.deleteById(item.getId());

        String message = "Deleted " + item.getEntityTableName() + " : " + item.toString();

        notificationService.success(message);
        logger.info(message);

        super.confirm(event);
    }
}
