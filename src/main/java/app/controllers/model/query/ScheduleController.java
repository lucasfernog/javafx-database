package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.ScheduleDialog;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.model.Schedule;
import io.datafx.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/schedules.fxml")
public class ScheduleController extends ModelQueryController<Schedule> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTreeTableColumn<Schedule, String> mTimeColumn;

    @Override
    ObservableList<Schedule> getList() {
        ObservableList<Schedule> schedules = FXCollections.observableArrayList();

        Database.from(Schedule.class)
                .select("saida", "tipo", "veiculo", "linha", "motorista")
                .execute(new Database.Callback<Schedule>() {
                    public void onSuccess(Schedule schedule) {
                        schedules.add(schedule);
                    }
                });

        return schedules;
    }

    @Override
    ModelDialog getModelDialog(Schedule schedule, boolean canSave) throws IOException {
        return new ScheduleDialog(mRoot, schedule, canSave);
    }

    @PostConstruct
    public void init() {
        super.init();

        NodeUtils.setupCellValueFactory(mTimeColumn, Schedule::timeProperty);
    }
}
