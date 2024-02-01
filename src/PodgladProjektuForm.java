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
    private JTable zespolTable;
    private JTable ProgramistaTable;
    private JTable projektTable;
    private User loggedInUser;
    private JFrame parentFrame;
    DefaultTableModel tableModel;
    DefaultTableModel tableModel2;
    DefaultTableModel tableModel3;
    java.util.List<Projekt> projects = new ArrayList<Projekt>();
    java.util.List<Programista> programisci = new ArrayList<Programista>();
    java.util.List<Zespol> zespoly = new ArrayList<Zespol>();
public PodgladProjektuForm(Projekt selectedProject, User loggedInUser, JFrame parent) {
    this.parentFrame = parent;
    this.loggedInUser = loggedInUser;
    setContentPane(podgladProjektuPanel);
    int width = 1200, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    setLocationRelativeTo(parent);
    setTitle("System zarządzania projektami IT - Podgląd Projektu");

    int IDuser = loggedInUser.getUserID();
            tableModel = new DefaultTableModel();
            projektTable.setModel(tableModel);
    tableModel2 = new DefaultTableModel();
    zespolTable.setModel(tableModel2);
    tableModel3 = new DefaultTableModel();
    ProgramistaTable.setModel(tableModel3);

    tableModel.addColumn("Nazwa");
    tableModel.addColumn("Opis");
    tableModel.addColumn("Status");
    tableModel.addColumn("DataRozoczecia");
    tableModel.addColumn("DataZakonczenia");
    tableModel.addColumn("Klient");

    tableModel2.addColumn("Nazwa");
    tableModel2.addColumn("Rodzaj");
    tableModel2.addColumn("IlośćProgramistów");

    tableModel3.addColumn("Imię");
    tableModel3.addColumn("Nazwisko");
    tableModel3.addColumn("Email");
    tableModel3.addColumn("Umiejętność");
    tableModel3.addColumn("Telefon");
    tableModel3.addColumn("Adres");

        int projektID = selectedProject.getProjectID();
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    int loggedInUserID = loggedInUser.getUserID();
    String query = "SELECT * FROM Projekt WHERE UserID = " + loggedInUserID + " AND ProjektID = " + selectedProject.getProjectID();
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
            Projekt projekt = new Projekt(name, description, dataR, dataZ);
            projekt.setStatus(status);
            projects.add(projekt);
            tableModel.addRow(new Object[]{projekt.getNazwa(), projekt.getOpis(), projekt.getStatus(), projekt.getDataRozpoczecia(), projekt.getDataZakonczenia(), Klient});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }

    String query2 = "SELECT * FROM Programista " + "WHERE UserID = " + IDuser + " AND IDzespolu = (SELECT ZespolID FROM Zespol WHERE ZespolID = (SELECT ZespolID FROM Projekt WHERE ProjektID = " + selectedProject.getProjectID() + "))";

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query2);


        while (resultSet.next()) {

            int progID = resultSet.getInt("ProgramistaID");
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            String skilss = resultSet.getString("Skills");
            int idZespolu = resultSet.getInt("IDzespolu");
            Programista programista = new Programista(name, surname, email,phone,address,progID,"Programista",2000,skilss, idZespolu);

            programisci.add(programista);

            tableModel3.addRow(new Object[]{programista.getName(), programista.getSurname(), programista.getEmail(), programista.getSkills(), programista.getPhone(), programista.getAddress()});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    String query3 = "SELECT * FROM Zespol " + "WHERE UserID = " + loggedInUserID + " AND ZespolID = (SELECT ZespolID FROM Projekt WHERE ProjektID = " + selectedProject.getProjectID() + ")";

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query3);

        while (resultSet.next()) {

            int zespollID = resultSet.getInt("ZespolID");
            String name = resultSet.getString("Nazwa");
            String lider = resultSet.getString("LiderName");
            int ilosc = resultSet.getInt("IloscProgramistow");
            Zespol zespol = new Zespol(name, lider, ilosc);
            zespoly.add(zespol);
            tableModel2.addRow(new Object[]{zespol.getNazwa(), zespol.getLiderName(), zespol.getIloscProgramistow()});
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
            DashboardForm dashboardForm = new DashboardForm(loggedInUser, parentFrame);
            dashboardForm.setVisible(true);
        }
    });
}
}
