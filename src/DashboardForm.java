import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
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

    public DashboardForm() {

    }

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

                // Dodanie danych do modelu
                tableModel.addRow(new Object[]{projectID, name, description, status});
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
                DodajProjektForm dodajProjektForm = new DodajProjektForm(loggedInUser); // Otwórz nowy formularz "DodajProjektForm"
                dodajProjektForm.setVisible(true);
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
    }
}
