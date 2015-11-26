/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Vadim
 */
@SuppressWarnings("serial")
public class DataBaseFrame extends JFrame {    
    public DataBaseFrame() {
        initCompanents();
        sShowTablesMenuItem.addActionListener(new ActionListener() {
            /**
             * создание диалогового окна для отображения списка таблиц БД
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (sTablesInfoDialog == null) {
                    Vector<String> tableNames;
                    // подключение к БД
                    try {
                        connect = getConnection();
                        // Получение имен таблиц
                        DatabaseMetaData meta = connect.getMetaData();
                        ResultSet rs = meta.getTables(null, null, "%",
                                new String[] { "TABLE" });
                        tableNames = new Vector<String>();
                        while (rs.next()) {
                            tableNames.add(rs.getString("TABLE_NAME"));
                        }
                    } catch (NullPointerException ne) {
                        System.out.println("Showing tables listener: "
                                + ne.getMessage());
                        return;
                    } catch (ClassNotFoundException ex) {
                        System.out.println("Firebird JDBC driver not found");
                        return;
                    } catch (SQLException se) {
                        System.out.println("No connection! " + se.getMessage());
                        return;
                    }
                    sTablesInfoDialog = new ShowTablesDialog(DataBaseFrame.this, tableNames, connect);
                }
                sTablesInfoDialog.setVisible(true);
            }
        });
        sExecMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {                 
                // подключение к БД
                try {
                    connect = getConnection();
                } catch (NullPointerException ne) {
                    System.out.println("Execute listener: " + ne.getMessage());
                    return;
                } catch (ClassNotFoundException ex) {
                    System.out.println("Firebird JDBC driver not found");
                    return;
                } catch (SQLException se) {
                    System.out.println("No connection! " + se.getMessage());
                    return;
                }            
                // получение запроса с окна ввода
                String query = sQueryTextArea.getText();
                //
                if(sTableScrollPane != null)
                    remove(sTableScrollPane);
                // определение вида запроса
                if(query.substring(0, 6).compareToIgnoreCase("select") == 0)
                {
                    try{
                        //устанивливаем возможность прокрутки результатов выполнения запроса
                        //без возможности редактирования данных
                        stat = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        rs = stat.executeQuery(query);
                        model = new ResultSetTableModel(rs);
    
                        sResultTable = new JTable(model);
                        sTableScrollPane = new JScrollPane(sResultTable);                    
                        add(sTableScrollPane,new GBC(0, 3, 1, 3).setFill(GridBagConstraints.SOUTH).setWeight(100, 100));                    
                        pack();
                    }catch(SQLException se){
                        System.out.println(se.getMessage());
                        JOptionPane.showMessageDialog(DataBaseFrame.this, "Invalid database request", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                else
                {
                    try{
                        stat = connect.createStatement();
                        int nUpdRec = stat.executeUpdate(query);
                        System.out.println(nUpdRec);
                    }catch(SQLException se){
                       System.out.println(se.getMessage());
                       JOptionPane.showMessageDialog(DataBaseFrame.this, "Invalid database request", "Error", JOptionPane.ERROR_MESSAGE);
                       return;
                    }
                    
                }
                validate();               
        }
    });
           
    }


    /**
     * Начальная инициализация компонентов окна 
     */
    private void initCompanents(){
       // установка ширины и высоты фрейма
        setSize(DEFAULT_W, DEFAULT_H);
        //установка заголовка фрейма
        setTitle("FireBird plugin");
        //начальная позиция окна определяется системой по-умолчанию
        this.setLocationByPlatform(true);
        //Размеры окна нельзя изменять
        this.setResizable(false);
        //создание диспетчера компоновки
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);        
        //создание строки меню
        JMenuBar menuBar = new JMenuBar();
        //поместим её в верхнюю часть текущего фрейма
        this.setJMenuBar(menuBar);
        //создание пунктов меню
        JMenu runMenu = new JMenu("Run");
        menuBar.add(runMenu);
        sExecMenuItem = new JMenuItem("Execute");
        runMenu.add(sExecMenuItem);
        JMenu showMenu = new JMenu("Show");
        menuBar.add(showMenu);
        sShowTablesMenuItem = new JMenuItem("Tables");
        showMenu.add(sShowTablesMenuItem);                           
        //добавление текстовой области к фрейму
        sQueryTextArea = new JTextArea(5,40);
        sQueryTextArea.setText("select full_name, salary, hire_date  from employee where salary<50000");
        sQueryScrollPane = new JScrollPane(sQueryTextArea);                
        add(sQueryScrollPane,new GBC(0, 0, 1, 3).setFill(GridBagConstraints.NORTH).setWeight(100, 100));
        pack();
    }
    /**
     * Установление соединения на основе свойств, заданных в файле database.properties
     * @return Соединение с БД
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static Connection getConnection()
            throws SQLException, ClassNotFoundException{
        Properties pr = new Properties();
        FileInputStream inp = null;
        try {
            try {
                inp = new FileInputStream("database.prop");
                pr.load(inp);
            } finally {
                if(inp != null)
                    inp.close();
            }
        } catch (FileNotFoundException e) {
            throw new NullPointerException("Properties file not found");        
        } catch (IOException e) {
            throw new NullPointerException("Invalid format of properties file");
        }     
        String databaseURL = pr.getProperty("dbURL");
        String user = pr.getProperty("user");
        String password = pr.getProperty("password");
        String driverName = pr.getProperty("driver");
        Class.forName(driverName);
        return DriverManager.getConnection(databaseURL, user, password);
    }

    public static final int DEFAULT_H = 600;
    public static final int DEFAULT_W = 800;    
    private static JMenuItem sExecMenuItem;
    private static JMenuItem sShowTablesMenuItem;
    private static JTextArea sQueryTextArea;
    private static JScrollPane sQueryScrollPane;
    private static JScrollPane sTableScrollPane;
    private static JTable sResultTable;
    private static JDialog sTablesInfoDialog;
    
    private ResultSet rs;
    private ResultSetTableModel model;
    private Connection connect;
    private Statement stat;
}

@SuppressWarnings("serial")
class ResultSetTableModel extends AbstractTableModel {
    public ResultSetTableModel(ResultSet aResultSet){
        rs = aResultSet;
        try{
            rsmd = rs.getMetaData();
        }
        catch(SQLException e){
            System.out.println("Couldn't get metadata" + e.getMessage());
        }
    }
    @Override
    public String getColumnName(int c){
        try{
            return rsmd.getColumnName(c+1);
        }
        catch(SQLException e){
            System.out.println("getColumnName(" + (c+1) + ")" + e.getMessage());
            return "";
        }
    }
    @Override
    public int getColumnCount(){
        try{
            return rsmd.getColumnCount();
        }
        catch(SQLException e){
            System.out.println("getColumnCount()" + e.getMessage());
            return 0;
        }
    }
    @Override
    public int getRowCount(){
        try{
            rs.last();
            return rs.getRow();
        }
        catch(SQLException e){            
            System.out.println("getRowCount()" + e.getMessage());
            return 0;
        }
    }
    @Override
    public Object getValueAt(int r,int c){
        try{
            rs.absolute(r + 1);
            return rs.getObject(c + 1);
        }
        catch(SQLException e){
            System.out.println("getValueAt(" + (r+1) + "," + (c+1) + ")" + e.getMessage());
            return null;
        }
    }

    private ResultSet rs;
    private ResultSetMetaData rsmd;
}