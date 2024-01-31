import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class ProgramisciForm extends JFrame{
    private JPanel ProgramistaPanel;
    private JTextField tfName;
    private JTextField tfSurname;
    private JTextField tfEmail;
    private JTextField tfJezyk;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JButton btnAddPro;
    private JTable ProgramistaTable;
    private JButton btnDelete;
    private JButton btnBack;
    DefaultTableModel tableModel;
public ProgramisciForm() {
    super();
    setContentPane(ProgramistaPanel);
    int width = 1200, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));

    // Inicjalizacja JTable z pustym modelem
    tableModel = new DefaultTableModel();
    ProgramistaTable.setModel(tableModel);

    // Dodanie kolumn do modelu
    tableModel.addColumn("ID");
    tableModel.addColumn("Imię");
    tableModel.addColumn("Nazwisko");
    tableModel.addColumn("Email");
    tableModel.addColumn("Umiejętności");
    tableModel.addColumn("Telefon");
    tableModel.addColumn("Adres");

    // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    String query = "SELECT * FROM Programista";

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int programistaID = resultSet.getInt("ProgramistaID");
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String skills = resultSet.getString("Skills");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");

            Programista programista = new Programista(name, surname, email, phone, address, skills, 0);

            // Dodanie danych do modelu
            tableModel.addRow(new Object[]{programistaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getSkills(), programista.getPhone(), programista.getAddress()});
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

        }
    });
    btnAddPro.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = tfName.getText();
            String surname = tfSurname.getText();
            String email = tfEmail.getText();
            String jezyk = tfJezyk.getText();
            String phone = tfPhone.getText();
            String address = tfAddress.getText();

            Programista newProgramista = new Programista(name, surname, email, phone, address, jezyk, 0);

            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String insertQuery = "INSERT INTO Programista (Name, Surname, Email, Skills, Phone, Address) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, jezyk);
                preparedStatement.setString(5, phone);
                preparedStatement.setString(6, address);

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Programista został pomyślnie dodany do bazy danych.");
                    // Aktualizacja widoku tabeli
                    tableModel.addRow(new Object[]{getLastProgramistaID(connection), name, surname, email});
                } else {
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Nie udało się dodać programisty do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wystąpił błąd podczas dodawania programisty do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    });
}
    // Metoda pomocnicza do pobrania ostatniego ID programisty dodanego do bazy
    private int getLastProgramistaID(Connection connection) throws SQLException {
        String query = "SELECT MAX(ProgramistaID) AS LastID FROM Programista";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return resultSet.getInt("LastID");
        }
        return 0;
    }
}
