package app.views.textfields;

import util.Utils;

public class CpfTextField extends MaskTextField {

    @Override
    void initialize() {
        super.initialize();
        setMask(Utils.CPF_MASK);
        setPattern("[-/.]");
    }
}
