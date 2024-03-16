package org.openide.awt;

import org.junit.Assert;
import org.junit.Test;

import javax.swing.Action;
import java.awt.event.ActionEvent;
import java.util.Locale;

import static org.openide.awt.Mnemonics.isMacOS;

public class MnemonicsActionTest {

    @Test
    public void testSetLocalizedTextAction() {
        AbstractMnemonicsAction item = new AbstractMnemonicsAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            }
        };
        item.setText("&Simple Text");
        Assert.assertEquals("Simple Text", item.getValue(Action.NAME));
        Assert.assertEquals(isMacOS()? -1: 0, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        //
        item.setText("Rock & Roll");
        Assert.assertEquals("Rock & Roll", item.getValue(Action.NAME));
        Assert.assertEquals(-1, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        //
        item.setText("&\u041F\u043E\u0438\u0441\u043A", new Locale("ru"));
        Assert.assertEquals("\u041F\u043E\u0438\u0441\u043A", item.getValue(Action.NAME));
        Assert.assertEquals(isMacOS()? -1: 0, item.getValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY));
        Assert.assertEquals(isMacOS()? 0: 71, item.getValue(Action.MNEMONIC_KEY));
    }
}
