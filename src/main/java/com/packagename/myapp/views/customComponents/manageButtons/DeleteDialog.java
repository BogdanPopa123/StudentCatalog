package com.packagename.myapp.views.customComponents.manageButtons;

import com.packagename.myapp.models.BaseModel;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;

public class DeleteDialog<T extends BaseModel> extends ConfirmDialog {

    private static final Logger logger = LogManager.getLogger(DeleteDialog.class);

    private final T item;
    private final CrudRepository<T, Integer> repository;


    public DeleteDialog(T item) {
        super(getMessage(item));

        this.item = item;
        this.repository = item.getRepository();

        setConfirmMessage(getDeleteMessage());
    }

    private static String getMessage(BaseModel item) {
        return "Are you sure you want to delete " + item.getEntityTableName() + " : " + "<br><br>" +
                item.toShortString() + " ?";
    }

    public void confirm(ClickEvent<Button> event) {
        repository.deleteById(item.getId());

        logger.info(this::getDeleteMessage);
        super.confirm(event);
    }

    private String getDeleteMessage() {
        return "Deleted " + item.getEntityTableName() + " : " + item.toString();
    }
}
