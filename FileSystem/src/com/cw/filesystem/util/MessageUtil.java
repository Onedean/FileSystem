package com.cw.filesystem.util;

import javax.swing.*;
import java.awt.*;

public class MessageUtil {
    //弹出"文件正在打开着，不能删除"的提示错误窗口
    public static void showErrorMessages(Component parent,String message){
        JOptionPane.showMessageDialog(parent,message,"错误",JOptionPane.ERROR_MESSAGE);
    }
}
