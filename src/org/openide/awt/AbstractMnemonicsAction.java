package org.openide.awt;

import javax.swing.*;
import java.util.Locale;

public abstract class AbstractMnemonicsAction extends AbstractAction implements MnemonicsAction {
    public AbstractMnemonicsAction(String text, Locale locale) {
        super();
        setText(text, locale);
    }

    public void setText(String text, Locale locale) {
        int i = Mnemonics.findMnemonicAmpersand(text);
        if (i < 0) {
            putValue(Action.NAME, text);
            putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, -1);
        } else {
            putValue(Action.NAME, text.substring(0, i) + text.substring(i + 1));
            Mnemonics.setMnemonicAndIndex(this, text.charAt(i + 1), i, locale);
        }
    }
}
