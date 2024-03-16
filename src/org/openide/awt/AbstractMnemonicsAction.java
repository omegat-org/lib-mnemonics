package org.openide.awt;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Hiroshi Miura
 */
public abstract class AbstractMnemonicsAction extends AbstractAction {
    public AbstractMnemonicsAction() {
        super();
    }

    public AbstractMnemonicsAction(String key, ResourceBundle bundle) {
        super();
        setText(key, bundle);
    }

    public AbstractMnemonicsAction(String text, Locale locale) {
        super();
        setText(text, locale);
    }

    public AbstractMnemonicsAction(String text) {
        super();
        setText(text);
    }

    public void setText(String key, ResourceBundle bundle) {
        setText(bundle.getString(key), bundle.getLocale());
    }

    public void setText(String text) {
        setText(text, new Locale("en"));
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
