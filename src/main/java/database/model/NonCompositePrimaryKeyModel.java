package database.model;

import database.QueryBuilder;
import javafx.beans.property.SimpleIntegerProperty;

public abstract class NonCompositePrimaryKeyModel<T extends Model> extends Model<T> {
    private SimpleIntegerProperty mPrimaryKey = new SimpleIntegerProperty(this, getPrimaryKeyName());

    @Override
    String getWhereClauseForPrimaryKey() {
        return primaryKeyProperty().getName() + "=" + getPrimaryKey();
    }

    @Override
    boolean shouldUpdate() {
        return getPrimaryKey() > 0;
    }

    @Override
    void onSave(Integer generatedKey) {
        if (generatedKey != null)
            setPrimaryKey(generatedKey);
    }

    String getPrimaryKeyName() {
        return "codigo";
    }

    public int getPrimaryKey() {
        return primaryKeyProperty().get();
    }

    protected void setPrimaryKey(int primaryKey) {
        primaryKeyProperty().set(primaryKey);
    }

    public SimpleIntegerProperty primaryKeyProperty() {
        return mPrimaryKey;
    }
}
