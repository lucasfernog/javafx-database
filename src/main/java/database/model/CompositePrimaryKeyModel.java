package database.model;

import javafx.beans.property.ReadOnlyProperty;

import java.util.HashMap;
import java.util.Map;

public abstract class CompositePrimaryKeyModel<T extends Model> extends Model<T> {
    private Map<String, Object> mPreviousPrimaryKeyValues = new HashMap<>();

    @Override
    boolean shouldUpdate() {
        return !isPreviousPrimaryKeyValuesEmpty();
    }

    @Override
    String getWhereClauseForPrimaryKey() {
        StringBuilder buffer = new StringBuilder();
        int entryCount = mPreviousPrimaryKeyValues.size(), i = 0;

        for (Map.Entry<String, Object> entry : mPreviousPrimaryKeyValues.entrySet()) {
            i++;
            Object value = entry.getValue();
            buffer.append(entry.getKey())
                    .append("=");

            if (value instanceof CharSequence)
                buffer.append("'")
                        .append(value)
                        .append("'");
            else
                buffer.append(value);

            if (i != entryCount)
                buffer.append(" AND ");
        }

        return buffer.toString();
    }

    private boolean isPreviousPrimaryKeyValuesEmpty() {
        if (mPreviousPrimaryKeyValues == null || mPreviousPrimaryKeyValues.size() == 0)
            return true;

        for (Map.Entry<String, Object> entry : mPreviousPrimaryKeyValues.entrySet()) {
            Object value = entry.getValue();
            if (value == null || (value instanceof Integer && ((Integer) value) <= 0))
                return true;
        }

        return false;
    }

    void savePreviousPrimaryKeyValues(ReadOnlyProperty... properties) {
        for (ReadOnlyProperty property : properties)
            mPreviousPrimaryKeyValues.put(property.getName(), property.getValue());
    }
}
