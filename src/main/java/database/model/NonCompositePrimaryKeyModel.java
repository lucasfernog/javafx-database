package database.model;

import database.QueryBuilder;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

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

    static <T extends NonCompositePrimaryKeyModel> void sync(ObservableList<T> oldList, ObservableList<T> newList) {
        if (oldList != null)
            for (T item : oldList)
                if (!newList.contains(item))
                    item.delete();

        for (T item : newList)
            item.save();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NonCompositePrimaryKeyModel)
            return ((NonCompositePrimaryKeyModel) other).getPrimaryKey() == getPrimaryKey();

        return super.equals(other);
    }
}
