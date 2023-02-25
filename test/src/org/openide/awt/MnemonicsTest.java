/**************************************************************************
 Copyright (C) 2023 Thomas Cordonnier
               Home page: http://www.omegat.org/
               Support center: https://omegat.org/support

 **************************************************************************/
 
package org.openide.awt;

import org.junit.Test;
import org.junit.Assert;

import javax.swing.JLabel;
import javax.swing.JMenuItem;

import java.util.Locale;

import static org.openide.awt.Mnemonics.isMacOS;

public final class MnemonicsTest {

    @Test
    public void testRemoveMnemonics() throws Exception {
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
}
