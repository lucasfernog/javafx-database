package util;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static <T> String joinToSql(CharSequence delimiter, List<T> tokens) {
        if (tokens.get(0) instanceof String) {
            List<String> strTokens = new ArrayList<>();
            for (Object token : tokens) {
                strTokens.add("'" + token + "'");
            }
            return join(delimiter, strTokens);
        } else
            return join(delimiter, tokens);
    }
}
