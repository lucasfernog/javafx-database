package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ScheduleType extends NonCompositePrimaryKeyModel<ScheduleType> {
    private SimpleStringProperty mDescription = new SimpleStringProperty("");

    public static ObservableList<ScheduleType> getAll() {
        ObservableList<ScheduleType> list = FXCollections.observableArrayList();

        Database.from(ScheduleType.class)
                .select("codigo", "descricao")
                .execute(new Database.Callback<ScheduleType>() {
                    public void onSuccess(ScheduleType manufacturer) {
                        list.add(manufacturer);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "tipos";
    }

    @Override
    public ScheduleType from(RowMap rowMap) {
        ScheduleType scheduleType = new ScheduleType();

        scheduleType.setPrimaryKey(rowMap.getAsInteger("codigo"));
        scheduleType.setDescription(rowMap.getAsString("descricao"));

        return scheduleType;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("descricao", getDescription());

        return row;
    }

    public String getDescription() {
        return descriptionProperty().get();
    }

    public void setDescription(String name) {
        descriptionProperty().set(name);
    }

    public SimpleStringProperty descriptionProperty() {
        return mDescription;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
