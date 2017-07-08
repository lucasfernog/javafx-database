package database;

import java.util.HashMap;

public class RowMap extends HashMap<String, Object> {

    public Integer getAsInteger(String key) {
        return getAsInteger(key, 0);
    }

    public Integer getAsInteger(String key, Integer defaultValue) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).intValue() : defaultValue;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
    }

    public String getAsString(String key) {
        return getAsString(key, "");
    }

    public String getAsString(String key, String defaultValue) {
        Object value = get(key);
        return value != null ? value.toString() : defaultValue;
    }

    public Long getAsLong(String key) {
        return getAsLong(key, 0L);
    }

    public Long getAsLong(String key, Long defaultValue) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).longValue() : defaultValue;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Long.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
    }

    public Double getAsDouble(String key) {
        return getAsDouble(key, 0d);
    }

    public Double getAsDouble(String key, Double defaultValue) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).doubleValue() : defaultValue;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Double.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
    }

    public Float getAsFloat(String key) {
        return getAsFloat(key, 0f);
    }

    public Float getAsFloat(String key, Float defaultValue) {
        Object value = get(key);
        try {
            return value != null ? ((Number) value).floatValue() : defaultValue;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                try {
                    return Float.valueOf(value.toString());
                } catch (NumberFormatException e2) {
                    return defaultValue;
                }
            } else {
                return defaultValue;
            }
        }
    }

    public Boolean getAsBoolean(String key) {
        return getAsBoolean(key, false);
    }

    public Boolean getAsBoolean(String key, Boolean defaultValue) {
        Object value = get(key);
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            if (value instanceof CharSequence) {
                return Boolean.valueOf(value.toString());
            } else if (value instanceof Number) {
                return ((Number) value).intValue() != 0;
            } else {
                return defaultValue;
            }
        }
    }
}
