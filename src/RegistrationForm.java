import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegistrationForm extends JDialog{
    private JPanel RegistrationPanel;
    private JLabel lbName;
    private JLabel lbSurname;
    private JLabel lbEmail;
    private JLabel lbPassword;
    private JTextField tfName;
    private JTextField tfSurname;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnRegister;
    private JButton btnBack;
public RegistrationForm(JFrame parent) {
    super(parent);
    setTitle("Create a new account");
    setContentPane(RegistrationPanel);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    int width = 450, height = 475;
    setMinimumSize(new Dimension(width,height));
    setModal(true);
    setLocationRelativeTo(parent);

    btnBack.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            LoginForm loginForm = new LoginForm(null);
            loginForm.setVisible(true);
        }
    });
    btnRegister.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            String name = tfName.getText();
            String surname = tfSurname.getText();
            String email = tfEmail.getText();
            String login = tfEmail.getText();
            String password = new String(pfPassword.getPassword());

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(RegistrationPanel,
                        "Wszystkie pola są wymagane!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(RegistrationPanel,
                        "Niepoprawny adres e-mail!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidPassword(password)) {
                JOptionPane.showMessageDialog(RegistrationPanel,
                        "Niepoprawne hasło!\n Hasło musi zawierać:\n 1 dużą literę,\n 1 cyfrę,\n 1 znak specjalny,\n i mieć długość co najmniej 6 znaków.",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(name, login, email, password)) {
                JOptionPane.showMessageDialog(RegistrationPanel,
                        "Pomyślnie zarejestrowano nowego użytkownika!",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                LoginForm loginForm = new LoginForm(null);
                loginForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(RegistrationPanel,
                        "Wystąpił błąd podczas rejestracji użytkownika.",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
        private boolean isValidEmail(String email) {
            String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
            return email.matches(emailPattern);
        }
        private boolean isValidPassword(String password) {
            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
            return password.matches(passwordPattern);
        }
    });
}

    private boolean registerUser(String name, String login, String email, String password) {
        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";
        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            String sql = "INSERT INTO User (login, name, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password);

            int rowsInserted = preparedStatement.executeUpdate();
            conn.close();

            return rowsInserted > 0;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
