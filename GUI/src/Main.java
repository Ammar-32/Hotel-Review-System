import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main extends JFrame{
    private JTextField hotel_name_text_field;
    private JButton hotel_search_button;
    private JComboBox city_combo_box;
    private JPanel mainPane;
    private JList<String> hotel_names_list;
    private DefaultListModel<String> model;
    private JLabel overall_rating_label;
    private JLabel room_rating_label;
    private JLabel service_rating_label;
    private JLabel cleanliness_rating_label;
    private JLabel location_rating_label;
    private JLabel value_label;
    private JList hotel_name_list;
    Connection con;
    private Map<String,Map<String,double[]>> city_hotels_ratings;
    public Main(){
        setTitle("Hotels");
        setContentPane(mainPane);
        setSize(500,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        model=new DefaultListModel<>();
        city_hotels_ratings=new HashMap<>();
        hotel_name_list=new JList();
        city_combo_box.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Statement stmt=null;
                try {
                    stmt=con.createStatement();
                    ResultSet rs= stmt.executeQuery("select * from hotels where city=\'"+city_combo_box.getSelectedItem()+"\'");
                    ArrayList<String> names=new ArrayList<>();
                    DefaultListModel dm = new DefaultListModel();
                    while(rs.next()){
                        names.add(rs.getString(2));
                        dm.addElement(rs.getString(2));
                    }
                    String abc[]=new String[names.size()];
                    abc=names.toArray(abc);
                    hotel_names_list.setListData(abc);
                    hotel_name_list.setModel(dm);
                    System.out.println("size: "+hotel_name_list.getModel().getSize());
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
        try {
            con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hotels","root","root");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        hotel_names_list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Statement stmt=null;
                try {
                    stmt=con.createStatement();
                    ResultSet rs= stmt.executeQuery("select * from hotels where hotel_name=\'"+hotel_names_list.getSelectedValue()+"\'");
                    while(rs.next()){
                        value_label.setText(rs.getString(3));
                        cleanliness_rating_label.setText(rs.getString(4));
                        room_rating_label.setText(rs.getString(5));
                        location_rating_label.setText(rs.getString(6));
                        service_rating_label.setText(rs.getString(7));
                        overall_rating_label.setText(rs.getString(8));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        hotel_search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = hotel_name_text_field.getText();
                Statement stmt=null;
                try {
                    stmt=con.createStatement();
                    ResultSet rs= stmt.executeQuery("select * from hotels where hotel_name=\'"+name+"\'");
                    if(!rs.next()){
                        JOptionPane.showMessageDialog(getParent(),"No such hotel exists in the database");
                    }
                    while(rs.next()){
                        value_label.setText(rs.getString(3));
                        cleanliness_rating_label.setText(rs.getString(4));
                        room_rating_label.setText(rs.getString(5));
                        location_rating_label.setText(rs.getString(6));
                        service_rating_label.setText(rs.getString(7));
                        overall_rating_label.setText(rs.getString(8));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }
    public void getCities(){
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select city from hotels group by city");
            while (rs.next()) {
               city_combo_box.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeVisible(){
        setVisible(true);
    }

    public static void main(String[] args) {
        Main m = new Main();
        m.getCities();
        m.makeVisible();
    }

}
