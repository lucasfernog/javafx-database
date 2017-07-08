package database.model;

import database.Database;
import database.RowMap;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Driver extends NonCompositePrimaryKeyModel<Driver> {
    private SimpleLongProperty mCPF = new SimpleLongProperty();
    private SimpleLongProperty mCTPS = new SimpleLongProperty();
    private SimpleStringProperty mName = new SimpleStringProperty("");
    private SimpleStringProperty mBirthDate = new SimpleStringProperty();
    private SimpleStringProperty mHiringDate = new SimpleStringProperty();
    private SimpleFloatProperty mSalary = new SimpleFloatProperty();

    public static ObservableList<Driver> getAll() {
        ObservableList<Driver> list = FXCollections.observableArrayList();

        Database.from(Driver.class)
                .select("*")
                .execute(new Database.Callback<Driver>() {
                    public void onSuccess(Driver driver) {
                        list.add(driver);
                    }
                });

        return list;
    }

    @Override
    public String getTableName() {
        return "motoristas";
    }

    @Override
    public Driver from(RowMap rowMap) {
        Driver driver = new Driver();

        driver.setPrimaryKey(rowMap.getAsInteger("codigo"));
        driver.setCPF(rowMap.getAsLong("cpf"));
        driver.setCTPS(rowMap.getAsLong("ctps"));
        driver.setName(rowMap.getAsString("nome"));
        driver.setBirthDate(rowMap.getAsString("nascimento"));
        driver.setHiringDate(rowMap.getAsString("contratacao"));
        driver.setSalary(rowMap.getAsFloat("salario"));

        return driver;
    }

    @Override
    RowMap getValues() {
        RowMap row = new RowMap();

        row.put("cpf", getCPF());
        row.put("ctps", getCTPS());
        row.put("nome", getName());
        row.put("nascimento", getBirthDate());
        row.put("contratacao", getHiringDate());
        row.put("salario", getSalary());

        return row;
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

    public long getCPF() {
        return mCPF.get();
    }

    public void setCPF(long cpf) {
        this.mCPF.set(cpf);
    }

    public SimpleLongProperty cpfProperty() {
        return mCPF;
    }

    public long getCTPS() {
        return mCTPS.get();
    }

    public void setCTPS(long ctps) {
        this.mCTPS.set(ctps);
    }

    public SimpleLongProperty ctpsProperty() {
        return mCTPS;
    }

    public String getBirthDate() {
        return mBirthDate.get();
    }

    public void setBirthDate(String birthDate) {
        this.mBirthDate.set(birthDate);
    }

    public SimpleStringProperty birthDateProperty() {
        return mBirthDate;
    }

    public String getHiringDate() {
        return mHiringDate.get();
    }

    public void setHiringDate(String hiringDate) {
        this.mHiringDate.set(hiringDate);
    }

    public SimpleStringProperty hiringDateProperty() {
        return mHiringDate;
    }

    public void setSalary(float salary) {
        this.mSalary.set(salary);
    }

    public float getSalary() {
        return mSalary.get();
    }

    public SimpleFloatProperty salaryProperty() {
        return mSalary;
    }

    @Override
    public String toString() {
        return getName();
    }
}
