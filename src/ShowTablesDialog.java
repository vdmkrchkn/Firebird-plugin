import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import java.util.Vector;

import javax.swing.*;
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
    public ShowTablesDialog(final JFrame owner, Vector<String> data, final Connection connect) {
        super(owner,"Tables",false);
        //
        final JList dataList = new JList(data);   
        initCompanents(dataList);             
        dataList.addListSelectionListener(new ListSelectionListener() {
            
            private JScrollPane fieldScrollPane;
            /**
             *  отображение списка полей выбранной таблицы БД
             */
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(fieldScrollPane != null){
                    sListPanel.remove(fieldScrollPane);
                    validate();
                    pack();
                }
                String tableName = dataList.getSelectedValue().toString();
                Vector<String> fieldNames = null;
                try {                    
                    //  подготовка предварительной команды
                    PreparedStatement pStmt = connect.prepareStatement("select * from " + tableName);
                    ResultSet rs = pStmt.executeQuery();
                    // Получение свойств таблицы 
                    ResultSetMetaData rsMeta = rs.getMetaData();                    
                    fieldNames = new Vector<String>();
                    for (int i = 0; i < rsMeta.getColumnCount(); i++){                    
                        fieldNames.add(rsMeta.getColumnName(i + 1));                            
                    }
                } catch (SQLException se) {
                    System.out.println("ListSelection: " + se.getMessage());
                    return;
                }                
                fieldScrollPane = new JScrollPane(new JList(fieldNames));
                sListPanel.add(fieldScrollPane);
                validate();
                pack();
            }
        });
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
