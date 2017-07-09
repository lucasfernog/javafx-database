package app.views.textfields;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;

public class MaskTextField extends JFXTextField {
    private String mask;
    private String pattern;

    public MaskTextField() {
        super();
        initialize();
    }

    public MaskTextField(String text) {
        super(text);
        initialize();
    }

    public String removeMask() {
        return getText().replaceAll(pattern, "");
    }

    boolean isValid(char toAppend, int position) {
        return toAppend >= '0' && toAppend <= '9';
    }

    void initialize() {
        lengthProperty().addListener((ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) -> {
            String withoutMask = removeMask();
            StringBuilder result = new StringBuilder();
            int i = 0;
            int quant = 0;

            if (newValue.intValue() > oldValue.intValue()) {
                if (getText().length() <= mask.length()) {
                    while (i < mask.length()) {
                        if (quant < withoutMask.length()) {
                            if ("#".equals(mask.substring(i, i + 1))) {
                                char toAppend = withoutMask.substring(quant, ++quant).charAt(0);
                                if (isValid(toAppend, i))
                                    result.append(toAppend);
                            } else {
                                result.append(mask.substring(i, i + 1));
                            }
                        }
                        i++;
                    }
                    setText(result.toString());
                }
                if (getText().length() > mask.length()) {
                    setText(getText(0, mask.length()));
                }
            }
        });

        focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && getText().length() != mask.length())
                setText("");

        });
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
