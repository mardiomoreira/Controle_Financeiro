/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JDialog;

/**
 *
 * @author mardio
 */
public class Utils {

    public static void maximizar(JDialog dialog) {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen = new Dimension((int) screen.getWidth(), (int) screen.getHeight() - 40);
        dialog.setSize(screen);
        dialog.setLocationRelativeTo(null);
    }

    public static String convertDouble(double valor) {
        return new DecimalFormat("#0.00").format(valor);
    }

    public static String convertData(Date date) {
        return new SimpleDateFormat("dd/MM/YYYY").format(date);
    }

    public static String convertDatainv(Date date) {
        return new SimpleDateFormat("YYYY-MM-dd").format(date);
    }

}
