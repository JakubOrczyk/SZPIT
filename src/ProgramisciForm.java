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
    private User loggedInUser;
    private JFrame parentFrame;
public ProgramisciForm(User loggedInUser, JFrame parent) {
    this.parentFrame = parent;
    this.loggedInUser = loggedInUser;
    setContentPane(ProgramistaPanel);
    int width = 1100, height = 400;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    setLocationRelativeTo(parent);
    setTitle("System zarządzania projektami IT - Programiści");

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
    int licznik = 0;
    // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    String query = "SELECT * FROM Programista WHERE UserID = " + loggedInUser.getUserID();

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
                licznik = programistaID+1;
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
            dispose();
            DashboardForm dashboardForm = new DashboardForm(loggedInUser, parentFrame);
            dashboardForm.setVisible(true);

        }
    });
    int finalLicznik = licznik;
    btnAddPro.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = tfName.getText();
            String surname = tfSurname.getText();
            String email = tfEmail.getText();
            String jezyk = tfJezyk.getText();
            String phone = tfPhone.getText();
            String address = tfAddress.getText();
            int idUsers = loggedInUser.getUserID();

            // Walidacja pól
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || jezyk.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wszystkie pola są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Walidacja adresu e-mail
            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny adres e-mail!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
// Walidacja numeru telefonu
            if (!isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny numer telefonu!\n Podaj numer w formacie XXX XXX XXX", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Walidacja adresu
            if (!isValidAddress(address)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny adres!\n Podaj miasto i numer domu.\n np:(Warszawa 00)", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Programista newProgramista = new Programista(name, surname, email, phone, address, jezyk, 0);

            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String insertQuery = "INSERT INTO Programista (Name, Surname, Email, Skills, Phone, Address, UserID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, jezyk);
                preparedStatement.setString(5, phone);
                preparedStatement.setString(6, address);
                preparedStatement.setInt(7, idUsers);


                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                   int liczbaa = finalLicznik;
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Programista został pomyślnie dodany do bazy danych.");
                    // Aktualizacja widoku tabeli
                    tableModel.addRow(new Object[]{ liczbaa,newProgramista.getName(), surname, email,jezyk,phone,address});
                    tfName.setText("");
                    tfSurname.setText("");
                    tfEmail.setText("");
                    tfJezyk.setText("");
                    tfPhone.setText("");
                    tfAddress.setText("");
                } else {
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Nie udało się dodać programisty do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wystąpił błąd podczas dodawania programisty do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
        private boolean isValidEmail(String email) {
            // Wzorzec do sprawdzania adresu e-mail
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            // Sprawdzenie, czy adres e-mail pasuje do wzorca
            return email.matches(emailPattern);
        }
        private boolean isValidPhoneNumber(String phone) {
            // Wzorzec do sprawdzania numeru telefonu w formacie XXX XXX XXXX
            String phonePattern = "\\d{3} \\d{3} \\d{3}";

            // Sprawdzenie, czy numer telefonu pasuje do wzorca
            return phone.matches(phonePattern);
        }

        private boolean isValidAddress(String address) {
            // Wzorzec do sprawdzania adresu (miasto i numer domu)
            String addressPattern = "^[a-zA-Z]+\\s+\\d+$";

            // Sprawdzenie, czy adres pasuje do wzorca
            return address.matches(addressPattern);
        }
    });

    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = ProgramistaTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wybierz programistę do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Pobierz ID wybranego programisty z tabeli
            int programistaID = (int) ProgramistaTable.getValueAt(selectedRow, 0);

            // Usuń programistę z bazy danych
            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM Programista WHERE ProgramistaID = ?")) {
                statement.setInt(1, programistaID);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // Usunięto programistę z bazy danych, więc usuń także wiersz z tabeli
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Programista został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Nie udało się usunąć programisty.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wystąpił błąd podczas usuwania programisty.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
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
