package database;

import java.util.HashMap;

public class RowMap extends HashMap<String, Object> {

    public Integer getAsInteger(String key) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).intValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public String getAsString(String key) {
        Object value = get(key);
        return value != null ? value.toString() : null;
    }

    public Long getAsLong(String key) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).longValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Gets a value and converts it to a Double.
     *
     * @param key the value to get
     * @return the Double value, or null if the value is missing or cannot be converted
     */
    public Double getAsDouble(String key) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).doubleValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public Float getAsFloat(String key) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).floatValue() : null;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public Boolean getAsBoolean(String key) {
        Object value = get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else {
                return null;
            }
        }
    }
}
