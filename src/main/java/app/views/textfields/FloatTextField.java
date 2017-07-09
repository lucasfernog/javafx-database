package app.views.textfields;

public class FloatTextField extends NumericTextField {

    @Override
    void initialize() {
        setSeparator('.');
        super.initialize();
    }
}
