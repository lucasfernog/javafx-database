package app.views.textfields;

import util.Utils;

public class LicensePlateTextField extends MaskTextField {

    @Override
    void initialize() {
        super.initialize();
        setMask(Utils.LICENSE_PLATE_MASK);
        setPattern("[-/.]");
    }

    @Override
    boolean isValid(char toAppend, int position) {
        return position < 3 ? ((toAppend >= 'A' && toAppend <= 'Z') || (toAppend >= 'a' && toAppend <= 'z'))
                : super.isValid(toAppend, position);
    }
}
