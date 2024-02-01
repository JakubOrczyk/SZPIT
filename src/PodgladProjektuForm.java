import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PodgladProjektuForm extends JFrame{
    private JPanel podgladProjektuPanel;
    private JButton btnBack;
    private JButton btnKlient;
    private JTable zespolTable;
    private JTable ProgramistaTable;
    private JTable projektTable;
    private User loggedInUser;
    DefaultTableModel tableModel;

    java.util.List<Projekt> projects = new ArrayList<Projekt>();
public PodgladProjektuForm(Projekt selectedProject, User loggedInUser) {
    this.loggedInUser = loggedInUser;
    setContentPane(podgladProjektuPanel);
    int width = 1200, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));

    // Inicjalizacja JTable z pustym modelem
            tableModel = new DefaultTableModel();
    projektTable.setModel(tableModel);

    // Dodanie kolumn do modelu
    tableModel.addColumn("Nazwa");
    tableModel.addColumn("Opis");
    tableModel.addColumn("Status");
    tableModel.addColumn("DataRozoczecia");
    tableModel.addColumn("DataZakonczenia");
    tableModel.addColumn("Klient");

        int projektID = selectedProject.getProjectID();
    // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    int loggedInUserID = loggedInUser.getUserID();
    String query = "SELECT * FROM Projekt WHERE UserID = " + loggedInUserID + " AND ProjektID = " + projektID;
    String query1 = "SELECT * FROM Klient JOIN Projekt ON Projekt.IDklienta = Klient.KlientID";


    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        Statement statementt = connection.createStatement();
        ResultSet resultSett = statementt.executeQuery(query1);

        while (resultSet.next()) {
            String Klient;
            int projectID = resultSet.getInt("ProjektID");
            String name = resultSet.getString("Nazwa");
            String description = resultSet.getString("Opis");
            String status = resultSet.getString("Status");
            Date dataR = resultSet.getDate("DataRozpoczecia");
            Date dataZ = resultSet.getDate("DataZakonczenia");
            if (resultSet.next()) {
                String nameK = resultSett.getString("Name");
                String surnameK = resultSett.getString("Surname");
                String email = resultSett.getString("Email");
                String tel = resultSett.getString("Phone");
                String adres = resultSett.getString("Address");
                Klient = nameK + " " + surnameK;
                Klient klient = new Klient(nameK, surnameK,email, tel, adres );
            } else {
                Klient = "BRAK";
            }
            // Tworzenie obiektu klasy Projekt
            Projekt projekt = new Projekt(name, description, dataR, dataZ);
            projekt.setStatus(status);

            projects.add(projekt);

            // Dodanie danych do modelu
            tableModel.addRow(new Object[]{projekt.getNazwa(), projekt.getOpis(), projekt.getStatus(), projekt.getDataRozpoczecia(), projekt.getDataZakonczenia(), Klient});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    btnBack.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            DashboardForm dashboardForm = new DashboardForm(loggedInUser, null);
            dashboardForm.setVisible(true);
        }
    });
    btnKlient.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
}
}
