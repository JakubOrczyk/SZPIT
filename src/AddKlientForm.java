import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class AddKlientForm extends JFrame{
    private JPanel AddKlientPanel;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton btnBack;
    private JButton btnUstaw;
    private User loggedInUser;
    private JFrame parentFrame;
    private int IDKlientaa;
public AddKlientForm(Projekt selectedProject, User loggedInUser, JFrame parent) {
    this.loggedInUser = loggedInUser;
    this.parentFrame = parent;
    setTitle("System zarządzania projektami IT - Ustawianie Klienta");
    setContentPane(AddKlientPanel);
    int width = 800, height = 400;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    setLocationRelativeTo(parent);
    btnBack.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            EditProjektForm editProjektForm = new EditProjektForm(selectedProject, loggedInUser, parentFrame);
            editProjektForm.setVisible(true);
        }
    });
    btnUstaw.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nazwa = textField1.getText();
            String nazwisko = textField2.getText();
            String email = textField3.getText();
            String telefon = textField4.getText();
            String adres = textField5.getText();

            if (nazwa.isEmpty() || nazwisko.isEmpty() || email.isEmpty() || telefon.isEmpty() || adres.isEmpty()) {
                JOptionPane.showMessageDialog(AddKlientForm.this, "Wszystkie pola są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(AddKlientForm.this, "Niepoprawny adres e-mail!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidPhoneNumber(telefon)) {
                JOptionPane.showMessageDialog(AddKlientForm.this, "Niepoprawny numer telefonu!\n Podaj numer w formacie XXX XXX XXX", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!isValidAddress(adres)) {
                JOptionPane.showMessageDialog(AddKlientForm.this, "Niepoprawny adres!\n Podaj miasto i numer domu.\n np:(Warszawa 00)", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Klient klient = new Klient(nazwa, nazwisko, email, telefon, adres);

            final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
            final String USERNAME = "root";
            final String PASSWORD = "";


            int klientId = -1;
            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String insertQuery = "INSERT INTO Klient (Name, Surname, Email, Phone, Address) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, klient.getName());
                preparedStatement.setString(2, klient.getSurname());
                preparedStatement.setString(3, klient.getEmail());
                preparedStatement.setString(4, klient.getPhone());
                preparedStatement.setString(5, klient.getAddress());

                int rowsInserted = preparedStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        klientId = generatedKeys.getInt(1); // Pobierz ID klienta
                    }
                    JOptionPane.showMessageDialog(AddKlientForm.this, "Klient został dodany pomyślnie do bazy danych.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AddKlientForm.this, "Nie udało się dodać klienta do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }

                preparedStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AddKlientForm.this, "Wystąpił błąd podczas dodawania klienta do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }


            if (klientId != -1) {
                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    String updateQuery = "UPDATE Projekt SET IDklienta = ? WHERE ProjektID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, klientId);
                    updateStatement.setInt(2, selectedProject.getProjectID());
                    updateStatement.executeUpdate();
                    updateStatement.close();

                    JOptionPane.showMessageDialog(AddKlientForm.this, "ID klienta zostało pomyślnie zaktualizowane w tabeli Projekt.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AddKlientForm.this, "Wystąpił błąd podczas aktualizacji ID klienta w tabeli Projekt.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private boolean isValidEmail(String email) {
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(emailPattern);
        }
        private boolean isValidPhoneNumber(String phone) {
            String phonePattern = "\\d{3} \\d{3} \\d{3}";
            return phone.matches(phonePattern);
        }

        private boolean isValidAddress(String address) {
            String addressPattern = "^[a-zA-Z]+\\s+\\d+$";
            return address.matches(addressPattern);
        }
    });
}
}
