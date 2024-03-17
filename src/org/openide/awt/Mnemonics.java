/**************************************************************************
 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <https://www.gnu.org/licenses/>.
**************************************************************************/

/*
 * According to Joint Copyright Assignment with Sun Microsystems,
 * I, Maxym Mykhalchuk, as a joint-copyright holder of this file,
 * decide to distribute it under GPL.
 *
 * Old license notice is below:
 */

/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2004 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * Contributors: Maxym Mykhalchuk
 */

package org.openide.awt;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JLabel;

/**
 * Support class for setting button, menu, and label text strings with mnemonics.
 * @author Maxym Mykhalchuk
 * @since 3.37
 * @see <a href="http://www.netbeans.org/issues/show_bug.cgi?id=26640">Issue #26640</a>
 */
public final class Mnemonics {
    
    private static final Pattern RE_MNEMONIC_END = Pattern.compile("\\s*\\(&[A-Za-z0-9]\\)(?=[.\\uFF1A]*$)");
    private static final Pattern RE_MNEMONIC_INSIDE = Pattern.compile("&(\\p{L})");
    
    /** Private constructor in order that this class is never instantiated. */
    private Mnemonics() {}
    
    /**
     * Removes any mnemonics in the given text. 
     * To be used for components which do not support mnemonics at all (example: contextual menus in Swing)
     * @param text text with mnemonics &amp; character
     * @return text without mnemonics &amp; character
     */
    public static String removeMnemonics(String text) {
        text = RE_MNEMONIC_END.matcher(text).replaceFirst("");
        text = RE_MNEMONIC_INSIDE.matcher(text).replaceFirst("$1");
        return text;
    }

    /**
     * Sets the text for a menu item or other subclass of AbstractButton.
     * <table cellspacing="2" cellpadding="3" border="1">
     *   <caption>Examples:</caption>
     *   <tr><th>Input String</th>                                   <th>View</th></tr>
     *   <tr><td><code>Save &amp;As</code></td>                      <td>Save <u>A</u>s</td></tr>
     *   <tr><td><code>Rock &amp; Roll</code></td>                   <td>Rock &amp; Roll</td></tr>
     *   <tr><td><code>Drag &amp; &amp;Drop</code></td>              <td>Drag &amp; <u>D</u>rop</td></tr>
     *   <tr><td><code>&amp;&#1060;&#1072;&#1081;&#1083;</code></td> <td><u>&#1060;</u>&#1072;&#1081;&#1083;</td></tr>
     * </table>
     * @param item a button whose text will be changed
     * @param text new label
     */
    public static void setLocalizedText(AbstractButton item, String text) {
        setLocalizedText(item, text, null);
    }

    /**
     * Sets the text for a menu item or other subclass of AbstractButton in given locale.
     * @param item a button whose text will be changed
     * @param text new label
     * @param locale null when use system locale.
     */
    static void setLocalizedText(AbstractButton item, String text, Locale locale) {
        setLocalizedText2(item, text, locale);
    }

    /**
     * Sets the text for the label or other subclass of JLabel.
     * For details see {@link #setLocalizedText(AbstractButton, String)}.
     * @param item a label whose text will be set
     * @param text new label
     */
    public static void setLocalizedText(JLabel item, String text) {
        setLocalizedText(item, text, null);
    }

    /**
     * Sets the text for the label or other subclass of JLabel in given locale.
     * @param item a label whose text will be set
     * @param text new label
     * @param locale null when use system locale.
     */
    static void setLocalizedText(JLabel item, String text, Locale locale) {
        setLocalizedText2(item, text, locale);
    }


   /**
    * Actual setter of the text & mnemonics for the AbstractButton/JLabel or  
    * their subclasses.
    * @param item AbstractButton/JLabel
    * @param text new label
    */
    private static void setLocalizedText2(Object item, String text, Locale locale) {
        // #17664. Handle null text also.
        if(text == null) {
            setText(item, null);
            return;
        }
        if (isMacOS()) {
            // remove shortcuts at the end because MacOS doesn't support mnemonics
            // text i.e. "Open(&O)..." will be "Open..."
            text = RE_MNEMONIC_END.matcher(text).replaceFirst("");
        }

        int i = findMnemonicAmpersand(text);

        if (i < 0) {
            // no '&' - don't set the mnemonic
            setText(item, text);
            setMnemonic(item, 0);
        }
        else {
            setText(item, text.substring(0, i) + text.substring(i + 1));
            setMnemonicAndIndex(item, text.charAt(i + 1), i, locale);
        }
    }
    
    
    /**
     * Searches for an ampersand in a string which indicates a mnemonic.
     * Recognizes the following cases:
     * <ul>
     * <li>"Drag &amp; Drop", "Ampersand ('&amp;')" - don't have mnemonic ampersand.
     *      "&amp;" is not found before " " (space), or if enclosed in "'"
     *     (single quotation marks).
     * <li>"&amp;File", "Save &amp;As..." - do have mnemonic ampersand.
     * <li>"Rock &amp; Ro&amp;ll", "Underline the '&amp;' &amp;character" - also do have
     *      mnemonic ampersand, but the second one.
     * </ul>
     * @param text text to search
     * @return the position of mnemonic ampersand in text, or -1 if there is none
     */
    public static int findMnemonicAmpersand(String text) {
        int i = -1;
        do {
            // searching for the next ampersand
            i = text.indexOf('&', i + 1);
            if (i >= 0 && (i + 1) < text.length()) {
                // before ' '
                if (text.charAt(i + 1)==' ') {
                    continue;
                // before ', and after '
                } else if (text.charAt(i + 1) == '\'' && i > 0 && text.charAt(i - 1) == '\'') {
                    continue;
                }
                // ampersand is marking mnemonics
                return i;
            }
        } while (i >= 0);
        return -1;
    }
    
    /**
     * Sets the mnemonics and the mnemonic index of the 
     * <code>AbstractButton</code> or <code>JLabel</code>.

     * @param item  AbstractButton/JLabel or subclasses
     * @param ch    Mnemonic char to set, may be a char in some locale
     * @param locale locale of the text.
     * @param index Index of the Character to underline under JDK1.4
     */
    static void setMnemonicAndIndex(Object item, char ch, int index, Locale locale) {
        
        // OmegaT tweak
        // if we're running on non-MacOSX, we don't set any mnemonics
        if (isMacOS() || ch == 0) {
            setMnemonic(item, 0);
            setMnemonicIndex(item, -1);
        } else {
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') 
                    || (ch >= '0' && ch <= '9')) {
                // it's a latin character or arabic digit,
                // setting it as mnemonics
                setMnemonic(item, ch);
                // If it's something like "Save &As", we need to set another
                // mnemonic index (at least under 1.4 or later)
                // see #29676
                setMnemonicIndex(item, index);
            } else {
                // it's non-latin, getting the latin correspondence
                int latinCode = getLatinKeycode(ch, locale);
                setMnemonic(item, latinCode);
                if (latinCode == 0) {
                    setMnemonicIndex(item, -1);
                } else {
                    setMnemonicIndex(item, index);
                }
            }
        }
    }

    /**
     * Gets the Latin symbol, which corresponds
     * to some non-Latin symbol on the localized keyboard.
     * The search is done via lookup of a Resource bundle
     * for pairs having the form (e.g.) <code>MNEMONIC_Ф=A</code>.
     *
     * @param localeChar non-Latin character or a punctuator to be used as mnemonic
     * @param locale locale of the specified character.
     * @return character on latin keyboard, corresponding to the locale character,
     *         or the appropriate VK_*** code (if there's no latin character 
     *         "under" the non-Latin one)
     */
    public static int getLatinKeycode(char localeChar, Locale locale) {
        try {
            // associated should be a latin character, arabic digit 
            // or an integer (KeyEvent.VK_***)
            String str=getBundle(locale).getString("MNEMONIC_" + localeChar);
            if( str.length()==1 )
                return str.charAt(0); 
            else
                return Integer.parseInt(str); 
        } catch (MissingResourceException x) {
            // correspondence not found, it IS an error,
            // but we eat it, and return zero - no mnemonics
            return 0;
        }
    }

    /**
     * Wrapper for the
     * <code>AbstractButton.setMnemonicIndex</code> or
     * <code>JLabel.setDisplayedMnemonicIndex</code> method.
     *  <li>Under JDK1.4 calls the method on item
     *  <li>Under JDK1.3 adds " (&lt;latin character&gt;)" (if needed)
     *      to label and sets the latin character as mnemonics.
     * @param item AbstractButton/JLabel/Action or subclasses
     * @param index Index of the Character to underline under JDK1.4
     */
    private static void setMnemonicIndex (Object item, int index) {
        if (item instanceof Action) {
            ((Action) item).putValue(Action.DISPLAYED_MNEMONIC_INDEX_KEY, index);
        } else if (item instanceof AbstractButton) {
            ((AbstractButton)item).setDisplayedMnemonicIndex(index);
        } else if (item instanceof JLabel) {
            ((JLabel)item).setDisplayedMnemonicIndex(index);
        }
    }

    /**
     * Wrapper for AbstractButton/JLabel.setText and Action.putValue.
     * @param item AbstractButton/JLabel/Action
     * @param text the text to set
     */
    private static void setText(Object item, String text) {
        if (item instanceof Action) {
            ((Action) item).putValue(Action.NAME, text);
        } else if (item instanceof AbstractButton) {
            ((AbstractButton) item).setText(text);
        } else if (item instanceof JLabel) {
            ((JLabel) item).setText(text);
        }
    }
    
    /**
     * Wrapper for AbstractButton.setMnemonic and JLabel.setDisplayedMnemonic  
     * @param item AbstractButton/JLabel
     * @param mnem Mnemonic char to set, latin [a-z,A-Z], digit [0-9], or any VK_ code
     */
    private static void setMnemonic(Object item, int mnem) {
        if (mnem>='a' && mnem<='z') {
            mnem=mnem+('A'-'a');
        }
        if (item instanceof Action) {
            ((Action) item).putValue(Action.MNEMONIC_KEY, mnem);
        } else if (item instanceof AbstractButton) {
            ((AbstractButton) item).setMnemonic(mnem);
        } else {
            ((JLabel) item).setDisplayedMnemonic(mnem);
        }
    }


    /**
     * Getter for the used Resource bundle (org.openide.awt.Mnemonics).
     * Used to avoid calling <code>ResourceBundle.getBundle(...)</code>
     * many times in different places of the code.
     * Does no caching, it's simply a utility method.
     */
    private static ResourceBundle getBundle(Locale locale) {
        if (locale == null) {
            return ResourceBundle.getBundle("org.openide.awt.Mnemonics");
        }
        return ResourceBundle.getBundle("org.openide.awt.Mnemonics", locale);
    }

    static boolean isMacOS() {
        return System.getProperty("os.name").contains("OS X");
    }
}
