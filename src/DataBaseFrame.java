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
        execItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // подключение к БД
                try{
                    connect = getConnection();
                    //DatabaseMetaData meta = connect.getMetaData();
                }catch (ClassNotFoundException ex) {
                    System.out.println("Firebird JDBC driver not found");
                }
                catch(SQLException se){
                    System.out.println("No connection! "+se.getMessage());
                }
                // получение запроса с окна ввода
                String query = JTextArea1.getText();
                //
                if(JScrollPane2 != null)
                    remove(JScrollPane2);
                // определение вида запроса
                if(query.substring(0, 6).compareToIgnoreCase("select") == 0)
                {
                    try{
                        //устанивливаем возможность прокрутки результатов выполнения запроса
                        //без возможности редактирования данных
                        stat = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        rs = stat.executeQuery(query);
                        model = new ResultSetTableModel(rs);
    
                        JTable1 = new JTable(model);
                        JScrollPane2 = new JScrollPane(JTable1);                    
                        add(JScrollPane2,new GBC(0, 3, 1, 3).setFill(GridBagConstraints.SOUTH).setWeight(100, 100));                    
                        pack();
                    }catch(SQLException se){
                        System.out.println(se.getMessage());
                        JOptionPane.showMessageDialog(null, "Invalid database request", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                else
                {
                    try{
                        stat = connect.createStatement();
                        stat.executeUpdate(query);                          
                    }catch(SQLException se){
                       System.out.println(se.getMessage());
                       JOptionPane.showMessageDialog(null, "Invalid database request", "Error", JOptionPane.ERROR_MESSAGE);
                       return;
                       //System.out.println(r);
                    }
                    
                }
                validate();               
        }
    });
    }



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
        //создание объекта меню
        JMenu runMenu = new JMenu("Run");
        menuBar.add(runMenu);
        execItem = new JMenuItem("Execute");
        runMenu.add(execItem);
        
        //создание компонентов
                     
        //добавление текстовой области к фрейму
        JTextArea1 = new JTextArea(5,40);
        JTextArea1.setText("select full_name, salary, hire_date  from employee where salary<50000");
        JScrollPane1 = new JScrollPane(JTextArea1);
                
        add(JScrollPane1,new GBC(0, 0, 1, 3).setFill(GridBagConstraints.NORTH).setWeight(100, 100));
        pack();
    }

    public static Connection getConnection() throws SQLException, ClassNotFoundException{
        Properties pr = new Properties();
        try {
            FileInputStream inp = new FileInputStream("database.prop");
            pr.load(inp);
            inp.close();
        } catch (IOException e) {
            return null;
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
    private static JMenuItem execItem;
    private static JTextArea JTextArea1;
    private static JScrollPane JScrollPane1;
    private static JScrollPane JScrollPane2;
    private static JTable JTable1;

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
            System.out.println("Couldn't get metadata"+e.getMessage());
        }
    }
    @Override
    public String getColumnName(int c){
        try{
            return rsmd.getColumnName(c+1);
        }
        catch(SQLException e){
            System.out.println("SQLException"+e.getMessage());
            return "";
        }
    }
    @Override
    public int getColumnCount(){
        try{
            return rsmd.getColumnCount();
        }
        catch(SQLException e){
            System.out.println("getColumnCount"+e.getMessage());
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
            System.out.println("getRowCount"+e.getMessage());
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
            System.out.println("getValueAt"+e.getMessage());
            return null;
        }
    }

    private ResultSet rs;
    private ResultSetMetaData rsmd;
}