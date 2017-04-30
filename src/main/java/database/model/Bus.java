package database.model;

import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;

public class Bus extends Model<Bus> {
    private SimpleIntegerProperty mCode = new SimpleIntegerProperty();

    @Override
    public Bus from(RowMap rowMap) {
        Bus bus = new Bus();

        Integer primaryKey = rowMap.getAsInteger("id");
        if (primaryKey != null)
            bus.setPrimaryKey(primaryKey);

        Integer code = rowMap.getAsInteger("code");
        if (code != null)
            bus.setCode(code);

        return bus;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("code", getCode());

        return row;
    }

    public Integer getCode() {
        return mCode.get();
    }

    public void setCode(Integer code) {
        mCode.set(code);
    }

    public SimpleIntegerProperty codeProperty() {
        return mCode;
    }
}
