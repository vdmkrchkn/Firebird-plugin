import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author Vadim
 *
 */
@SuppressWarnings("serial")
public class ShowTablesDialog extends JDialog {

    private static JPanel sListPanel;
    private static JButton sOkBtn;

    /**
     * @param owner - родительский фрейм
     * @param data - отображаемые данные 
     */
    public ShowTablesDialog(final JFrame owner, Vector<String> data) {
        super(owner,"Tables",false);
        //
        final JList dataList = new JList(data);   
        initCompanents(dataList);             
        dataList.addListSelectionListener(new ListSelectionListener() {
            
            private JScrollPane fieldScrollPane;
            
            @Override
            public void valueChanged(ListSelectionEvent e) { 
                String tableName = dataList.getSelectedValue().toString();
                // TODO добавить отображение полей tableName в JList
                Vector<String> tableNames;
                // подключение к БД
                try {
                    Connection connect = DataBaseFrame.getConnection();
                    // Получение имен таблиц
                    DatabaseMetaData meta = connect.getMetaData();                    
                    tableNames = new Vector<String>();
//                    for (int i = 0; i < meta.getColumnCount(); i++){                    
//                        tableNames.add(meta.getCol);                            
//                    }
                } catch (ClassNotFoundException ex) {
                    System.out.println("Firebird JDBC driver not found");
                    return;
                } catch (SQLException se) {
                    System.out.println("No connection! " + se.getMessage());
                    return;
                }
                if(fieldScrollPane != null)
                    sListPanel.remove(fieldScrollPane);
                fieldScrollPane = new JScrollPane(new JList(new String[]{tableName}));
                sListPanel.add(fieldScrollPane);
                validate();
                pack();
            }
        });               
        //        
        sOkBtn.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        });        
    }

    /**
     * Иициализация компонентов окна 
     */
    private void initCompanents(final JList dataList){
        this.setResizable(false);
        this.setLocationByPlatform(true);
        //  инициализация отображаемого списка
        sListPanel = new JPanel();
        add(sListPanel,BorderLayout.CENTER);
        sListPanel.add(new JScrollPane(dataList));        
        //  инициализация кнопки OK
        JPanel btnPanel = new JPanel();
        add(btnPanel,BorderLayout.SOUTH);
        sOkBtn = new JButton("OK");
        btnPanel.add(sOkBtn);
        pack();
    }
    
}
