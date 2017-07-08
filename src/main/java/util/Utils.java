package util;

import database.model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Utils {

    public static String formatDate(String date) {
        String[] pieces = date.split("-");
        return pieces[2] + "/" + pieces[1] + "/" + pieces[0];
    }

    public static <T extends Model> T find(ObservableList<T> list, int key) {
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
