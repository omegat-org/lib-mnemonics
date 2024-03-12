/**************************************************************************
 Copyright (C) 2023 Thomas Cordonnier
               Home page: http://www.omegat.org/
               Support center: https://omegat.org/support

 **************************************************************************/
 
package org.openide.awt;

import org.junit.Test;
import org.junit.Assert;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.util.Locale;

import static org.openide.awt.Mnemonics.isMacOS;

public final class MnemonicsTest {

    @Test
    public void testRemoveMnemonics() {
        Assert.assertEquals("Simple test", Mnemonics.removeMnemonics("&Simple test"));
        Assert.assertEquals("Rock & Roll", Mnemonics.removeMnemonics("Rock & Roll"));
        // Parenthesis at the end, but with latin characters
        Assert.assertEquals("Parenthesis", Mnemonics.removeMnemonics("Parenthesis (&P)"));
        Assert.assertEquals("Parenthesis...", Mnemonics.removeMnemonics("Parenthesis (&P)..."));
        // Parenthesis at the end, but with CJK characters
        Assert.assertEquals("\u691C\u7D22", Mnemonics.removeMnemonics("\u691C\u7D22(&S)"));
        // Mnemonics with non-latin character: russian
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", Mnemonics.removeMnemonics("&\u041F\u043E\u0438\u0441\u043A"));
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", Mnemonics.removeMnemonics("\u041F\u043E&\u0438\u0441\u043A"));
        Assert.assertEquals("\u0424\u0430\u0439\u043B\u044B \u043E\u0440\u0438\u0433\u0438\u043D\u0430\u043B\u0430", 
            Mnemonics.removeMnemonics("\u0424\u0430\u0439\u043B\u044B &\u043E\u0440\u0438\u0433\u0438\u043D\u0430\u043B\u0430"));
    }

    @Test
    public void testSetLocalizedTextLabel() {
        JLabel item = new JLabel();
        Mnemonics.setLocalizedText(item, "&Simple Text");
        Assert.assertEquals("Simple Text", item.getText());
        Assert.assertEquals(isMacOS()? -1: 0, item.getDisplayedMnemonicIndex());
        //
        Mnemonics.setLocalizedText(item, "Rock & Roll");
        Assert.assertEquals("Rock & Roll", item.getText());
        Assert.assertEquals(-1, item.getDisplayedMnemonicIndex());
        //
        Mnemonics.setLocalizedText(item, "&\u041F\u043E\u0438\u0441\u043A", new Locale("ru"));
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", item.getText());
        Assert.assertEquals(isMacOS()? -1: 0, item.getDisplayedMnemonicIndex());
        Assert.assertEquals(isMacOS()? 0: 'G', item.getDisplayedMnemonic());
    }

    @Test
    public void testSetLocalizedTextAction() {
        AbstractMnemonicsAction item = new MyAbstractMnemonicsAction();

        Assert.assertEquals("Simple Text", item.getValue(Action.NAME));
        Assert.assertEquals(isMacOS()? -1: 0, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        //
        item.setText("Rock & Roll", new Locale("en"));
        Assert.assertEquals("Rock & Roll", item.getValue(Action.NAME));
        Assert.assertEquals(-1, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        //
        item.setText("&\u041F\u043E\u0438\u0441\u043A", new Locale("ru"));
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", item.getValue(Action.NAME));
        Assert.assertEquals(isMacOS()? -1: 0, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        Assert.assertEquals(isMacOS()? 0: 71, item.getValue(Action.MNEMONIC_KEY));
    }

    @Test
    public void testSetLocalizedTextMenuItem() {
        JMenuItem item = new JMenuItem();
        Mnemonics.setLocalizedText(item, "&Simple Text");
        Assert.assertEquals("Simple Text", item.getText());
        Assert.assertEquals(isMacOS()? -1: 0, item.getDisplayedMnemonicIndex());
        Assert.assertEquals(isMacOS()? 0: 'S', item.getMnemonic());
        //
        Mnemonics.setLocalizedText(item, "Rock & Roll");
        Assert.assertEquals("Rock & Roll", item.getText());
        Assert.assertEquals(-1, item.getDisplayedMnemonicIndex());
        //
        Mnemonics.setLocalizedText(item, "&\u041F\u043E\u0438\u0441\u043A", new Locale("ru"));
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", item.getText());
        Assert.assertEquals(isMacOS()? -1: 0, item.getDisplayedMnemonicIndex());
        Assert.assertEquals(isMacOS()? 0: 'G', item.getMnemonic());
    }

    private static class MyAbstractMnemonicsAction extends AbstractMnemonicsAction {
        public MyAbstractMnemonicsAction() {
            super("&Simple Text", new Locale("en"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}
