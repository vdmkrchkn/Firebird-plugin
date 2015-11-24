import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author Vadim
 *
 */
@SuppressWarnings("serial")
public class ShowTablesDialog extends JDialog {

    /**
     * @param owner - ������������ �����
     */
    public ShowTablesDialog(JFrame owner, Vector<String> data) {
        super(owner,"Tables",false);
        this.setResizable(false);
        //  ������������� ������������� ������
        JPanel listPanel = new JPanel();
        add(listPanel,BorderLayout.CENTER);
        listPanel.add(new JScrollPane(new JList(data)));
        //  ������������� ������ OK
        JPanel btnPanel = new JPanel();
        add(btnPanel,BorderLayout.SOUTH);
        JButton okBtn = new JButton("OK");
        okBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });
        btnPanel.add(okBtn);
        pack();
    }

    
}
