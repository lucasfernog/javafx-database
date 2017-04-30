package database.model;

import javafx.beans.property.SimpleIntegerProperty;

public class Bus extends Model<Bus> {
    private SimpleIntegerProperty mCode;

    public Bus(Integer code) {
        mCode = new SimpleIntegerProperty(code);
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
