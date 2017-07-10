package database.model;

import database.RowMap;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student extends NonCompositePrimaryKeyModel<Student> {

    private User mUser;
    private SimpleStringProperty mDueDate = new SimpleStringProperty();
    private SimpleFloatProperty mBalance = new SimpleFloatProperty();
    private boolean mIsLoaded = false;

    @Override
    public String getTableName() {
        return "estudantes";
    }

    @Override
    String getPrimaryKeyName() {
        return "usuario";
    }

    @Override
    boolean shouldUpdate() {
        return mIsLoaded;
    }

    @Override
    public Student from(RowMap rowMap) {
        Student student = new Student();

        student.setUserId(rowMap.getAsInteger("usuario"));
        student.setDueDate(rowMap.getAsString("vencimento"));
        student.setBalance(rowMap.getAsFloat("saldo"));
        student.mIsLoaded = true;

        if (rowMap.containsKey("nome")) {
            student.mUser = new User();
            student.mUser.setName(rowMap.getAsString("nome"));
        }

        return student;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("usuario", getUserId());
        row.put("vencimento", getDueDate());
        row.put("saldo", getBalance());

        return row;
    }

    public String getDueDate() {
        return dueDateProperty().get();
    }

    public void setDueDate(String dueDate) {
        dueDateProperty().set(dueDate);
    }

    public SimpleStringProperty dueDateProperty() {
        return mDueDate;
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

    public int getUserId() {
        return userIdProperty().get();
    }

    public void setUserId(int userId) {
        userIdProperty().set(userId);
    }

    public SimpleIntegerProperty userIdProperty() {
        return primaryKeyProperty();
    }

    public User getUser() {
        return mUser;
    }
}
