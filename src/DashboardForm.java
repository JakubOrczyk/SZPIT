import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class DashboardForm extends JFrame{
    private JPanel dashboardPanel;
    private JLabel IbAdmin;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnClear;
    private JTable ClientsTable;
    private JButton btnClose;
    private JButton btnZespoly;
    private JButton btnProg;
    private User loggedInUser;

    List<Projekt> projects = new ArrayList<Projekt>();

//    public static void main(String[] args) {
//        DashboardForm dashboardForm = new DashboardForm();
//        dashboardForm.setVisible(true);
//    }
    // Tworzenie modelu dla JTable
    DefaultTableModel tableModel;
    public DashboardForm(User loggedInUser, JFrame parent) {
        super("DashboardForm");
        this.loggedInUser = loggedInUser;
        setContentPane(dashboardPanel);
        int width = 800, height = 600;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(width,height));

// Inicjalizacja JTable z pustym modelem
        tableModel = new DefaultTableModel();
        ClientsTable.setModel(tableModel);

        // Dodanie kolumn do modelu
        tableModel.addColumn("ID");
        tableModel.addColumn("Nazwa");
        tableModel.addColumn("Opis");
        tableModel.addColumn("Status");

        // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";
        int loggedInUserID = loggedInUser.getUserID();
        String query = "SELECT * FROM Projekt WHERE UserID = " + loggedInUserID;

        try{
            Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int projectID = resultSet.getInt("ProjektID");
                String name = resultSet.getString("Nazwa");
                String description = resultSet.getString("Opis");
                String status = resultSet.getString("Status");

                // Tworzenie obiektu klasy Projekt
                Projekt projekt = new Projekt(name, description, null, null);
                projekt.setStatus(status);

                projects.add(projekt);

                // Dodanie danych do modelu
                tableModel.addRow(new Object[]{projectID, projekt.getNazwa(), projekt.getOpis(), projekt.getStatus()});
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }



        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(null);
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DodajProjektForm dodajProjektForm = new DodajProjektForm(loggedInUser, projects); // Otwórz nowy formularz "DodajProjektForm"
                dodajProjektForm.setVisible(true);
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
// Sprawdź, czy wybrano wiersz w tabeli
                int selectedRow = ClientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Wybierz projekt do edycji.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Pobierz nazwę i opis wybranego projektu z tabeli
                String projectName = (String) ClientsTable.getValueAt(selectedRow, 1);
                String projectDescription = (String) ClientsTable.getValueAt(selectedRow, 2);

                // Znajdź wybrany projekt w liście projects na podstawie nazwy i opisu
                Projekt selectedProject = null;
                for (Projekt project : projects) {
                    if (project.getNazwa().equals(projectName) && project.getOpis().equals(projectDescription)) {
                        selectedProject = project;
                        break;
                    }
                }

                if (selectedProject == null) {
                    JOptionPane.showMessageDialog(null, "Nie można znaleźć wybranego projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Otwórz formularz edycji projektu i przekaż wybrany projekt jako argument konstruktora
                EditProjektForm editProjektForm = new EditProjektForm(selectedProject);
                editProjektForm.setVisible(true);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Sprawdź, czy wybrano wiersz w tabeli
                int selectedRow = ClientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Wybierz projekt do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Pobierz ID wybranego projektu z tabeli
                int projectID = (int) ClientsTable.getValueAt(selectedRow, 0);

                // Usuń projekt z bazy danych
                final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
                final String USERNAME = "root";
                final String PASSWORD = "";

                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM Projekt WHERE ProjektID = ?")) {
                    statement.setInt(1, projectID);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        // Usunięto projekt z bazy danych, więc usuń także wiersz z tabeli
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Projekt został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(null, "Nie udało się usunąć projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas usuwania projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnZespoly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        btnProg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ProgramisciForm programisciForm = new ProgramisciForm(); // Otwórz nowy formularz "DodajProjektForm"
                programisciForm.setVisible(true);
            }
        });
    }
}
