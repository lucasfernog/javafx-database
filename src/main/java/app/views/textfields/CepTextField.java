package app.views.textfields;

import util.Utils;

public class CepTextField extends MaskTextField {

    @Override
    void initialize() {
        super.initialize();
        setMask(Utils.CEP_MASK);
        setPattern("-");
    }
}
