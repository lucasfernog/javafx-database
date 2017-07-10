package app.controllers.model.query;

import app.views.dialogs.ModelDialog;
import app.views.dialogs.ScheduleDialog;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import database.Database;
import database.QueryBuilder;
import database.model.Schedule;
import io.datafx.controller.ViewController;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import util.NodeUtils;
import util.Utils;

import javax.annotation.PostConstruct;
import java.io.IOException;

@ViewController(value = "/fxml/schedules.fxml")
public class ScheduleController extends ModelQueryController<Schedule> {

    @FXML
    private StackPane mRoot;

    @FXML
    private JFXTextField mSearchCode;

    @FXML
    private JFXTreeTableColumn<Schedule, String> mTimeColumn;

    @Override
    QueryBuilder<Schedule> getSearchQuery() {
        QueryBuilder<Schedule> query = Database.from(Schedule.class)
                .select("saida", "tipo", "veiculo", "linha", "motorista");

        String searchCode = mSearchCode.getText();
        if (!Utils.isEmpty(searchCode))
            query.where("codigo", "=", Integer.valueOf(searchCode));

        return query;
    }

    @Override
    void clearQuery() {
        mSearchCode.setText("");
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
