package app.views.dialogs;

import app.controllers.model.dialog.RouteDialogController;
import app.controllers.model.dialog.ScheduleDialogController;
import database.model.Route;
import database.model.Schedule;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ScheduleDialog extends ModelDialog<Schedule, ScheduleDialogController> {

    public ScheduleDialog(StackPane dialogContainer, Schedule schedule) throws IOException {
        this(dialogContainer, schedule, true);
    }

    public ScheduleDialog(StackPane dialogContainer, Schedule schedule, boolean canSave) throws IOException {
        super(dialogContainer, "/fxml/schedule_dialog.fxml", schedule, canSave);
    }

}
