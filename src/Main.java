/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Vadim
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                DataBaseFrame frame = new DataBaseFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
