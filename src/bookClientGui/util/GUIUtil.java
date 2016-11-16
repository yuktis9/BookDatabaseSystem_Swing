/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookClientGui.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author dheeraj
 */
public class GUIUtil {
    
    public void showWarningDialoge(String title, String warningMessage, Component parentComponent){
        
        JOptionPane.showMessageDialog(parentComponent,
                warningMessage,
                title,
                JOptionPane.WARNING_MESSAGE);
    }
    
    public void showPlainDialog(String successMessage, Component parentComponent){
        
        JOptionPane.showMessageDialog(parentComponent,
                 successMessage
                 );
    }
    
    public int showConfirmDialogue(String confirmMessage, String title, Component parentComponent){
        
        return JOptionPane.showConfirmDialog(
                parentComponent,
                confirmMessage,
                title,
            JOptionPane.YES_NO_OPTION);
    }
    
}
