/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Util;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 *
 * @author mardio
 */
public class ApenasLetras extends PlainDocument {
 
    private static final long serialVersionUID = 1L;
 
    public void insertString(int offs, String str,
            javax.swing.text.AttributeSet a) throws BadLocationException {
        // normalmente apenas uma letra é inserida por vez,
        // mas fazendo assim também previne caso o usuário
        // cole algum texto
        for (int i = 0; i < str.length(); i++)
           if (!Character.isLetter(str.charAt(i)) && str.charAt(i) != ' ')
                return;
        super.insertString(offs, str, a);
 
    }
}