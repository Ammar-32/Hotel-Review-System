import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;

public class Deliverable3 extends JFrame{
    private JComboBox city_combo_box;
    private JButton search_button;
    private JComboBox overall_rating_combo_box;
    private JComboBox service_combo_box;
    private JComboBox location_combo_box;
    private JComboBox room_combo_box;
    private JComboBox cleanliness_combo_box;
    private JComboBox value_combo_box;
    private JTextArea hotel_names_text_area;
    private JPanel mainPane;
    Connection con;

    public Deliverable3(){
        setTitle("Hotels");
        setContentPane(mainPane);
        setSize(500,450);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        try {
            con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/hotels","root","root");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        search_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("select hotel_name from hotels where"+
                            " city=\'"+city_combo_box.getSelectedItem()+"\'"+
                            " and value_rating"+value_combo_box.getSelectedItem()+
                            " and cleanliness_rating"+cleanliness_combo_box.getSelectedItem()+
                            " and room_rating"+room_combo_box.getSelectedItem()+
                            " and location_rating"+location_combo_box.getSelectedItem()+
                            " and service_rating"+service_combo_box.getSelectedItem()+
                            " and overall_rating"+overall_rating_combo_box.getSelectedItem());
                    hotel_names_text_area.setText("");
                    while (rs.next()) {
                        hotel_names_text_area.append(rs.getString(1)+"\n");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
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

    public static void main(String[] args) {
        Deliverable3 newFrame= new Deliverable3();
        newFrame.getCities();
        newFrame.setVisible(true);
    }

}
