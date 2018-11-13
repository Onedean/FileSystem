package com.cw.filesystem.util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageUtil {

    public static void showMgs(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void showErrorMgs(Component parent, String message){
        JOptionPane.showMessageDialog(parent, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static int showConfirmMgs(Component parent, String message){
        return JOptionPane.showConfirmDialog(parent, message, "确认", JOptionPane.YES_NO_OPTION);
    }
}
