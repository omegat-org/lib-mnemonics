package org.openide.awt;

import javax.swing.Action;
import java.util.Locale;

public interface MnemonicsAction extends Action {
    /**
     * Set NAME attribute with mnemonics processing.
     * When given "&amp;Text" then it set NAME attribute as
     * "Text" and put mnemonics for "T".
     * @param text Label text.
     * @param locale locale of the text.
     */
    void setText(String text, Locale locale);

}
