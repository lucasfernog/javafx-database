package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User extends NonCompositePrimaryKeyModel<User> {

    private SimpleLongProperty mCPF = new SimpleLongProperty();
    private SimpleStringProperty mName = new SimpleStringProperty("");

    private SimpleFloatProperty mBalance = new SimpleFloatProperty();

    public static ObservableList<User> getAll() {
        ObservableList<User> list = FXCollections.observableArrayList();

        Database.from(User.class)
                .select("*")
                .execute(new Database.Callback<User>() {
                    public void onSuccess(User user) {
                        list.add(user);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "usuarios";
    }

    @Override
    public User from(RowMap rowMap) {
        User user = new User();

        user.setPrimaryKey(rowMap.getAsInteger("codigo"));
        user.setCPF(rowMap.getAsLong("cpf"));
        user.setName(rowMap.getAsString("nome"));
        user.setBalance(rowMap.getAsFloat("saldo"));

        return user;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("cpf", getCPF());
        row.put("nome", getName());
        row.put("saldo", getBalance());

        return row;
    }

    public long getCPF() {
        return cpfProperty().get();
    }

    public SimpleLongProperty cpfProperty() {
        return mCPF;
    }

    public void setCPF(long cpf) {
        cpfProperty().set(cpf);
    }

    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        nameProperty().set(name);
    }

    public SimpleStringProperty nameProperty() {
        return mName;
    }

    public float getBalance() {
        return balanceProperty().get();
    }

    public void setBalance(float balance) {
        balanceProperty().set(balance);
    }

    public SimpleFloatProperty balanceProperty() {
        return mBalance;
    }

    @Override
    public String toString() {
        return getName();
    }
}
