package util;

import database.model.NonCompositePrimaryKeyModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    public static final String LICENSE_PLATE_MASK = "###-####";
    public static final String CPF_MASK = "###.###.###-##";
    public static final String CEP_MASK = "#####-###";

    public static String applyMask(String mask, String str) {
        if (str == null || str.length() == 0)
            return str;

        StringBuilder result = new StringBuilder();
        for (int i = 0, n = mask.length(), j = 0; i < n; i++) {
            if (mask.charAt(i) == '#')
                result.append(str.charAt(j++));
            else
                result.append(mask.charAt(i));
        }
        return result.toString();
    }

    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    public static String replaceLast(String string, String substring, String replacement) {
        int index = string.lastIndexOf(substring);
        if (index == -1)
            return string;
        return string.substring(0, index) + replacement
                + string.substring(index + substring.length());
    }

    public static String formatDate(String date) {
        String[] pieces = date.split("-");
        return pieces[2] + "/" + pieces[1] + "/" + pieces[0];
    }

    public static <T extends NonCompositePrimaryKeyModel> T find(ObservableList<T> list, int key) {
        for (T item : list)
            if (item.getPrimaryKey() == key)
                return item;
        return null;
    }

    public static <T> ObservableList<T> filter(ObservableList<T> list, Predicate<T> predicate) {
        return FXCollections.observableArrayList(list.stream().filter(predicate).collect(Collectors.toList()));
    }

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
        if (tokens.get(0) instanceof CharSequence) {
            List<String> strTokens = new ArrayList<>();
            for (Object token : tokens) {
                strTokens.add("'" + token + "'");
            }
            return join(delimiter, strTokens);
        } else
            return join(delimiter, tokens);
    }
}
