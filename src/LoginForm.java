import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog  {
    private JPanel LoginPanel;
    private JButton btnLogin;
    private JButton btnRegistration;
    private JTextField tfLogin;
    private JPasswordField pfPassword;
    private JButton btnCancel;
    private JFrame parentFrame;

    public LoginForm(JFrame parent) {
        super(parent);
        this.parentFrame = parent;
        setTitle("Logowanie");
        setContentPane(LoginPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 450, height = 475;
        setMinimumSize(new Dimension(width,height));
        setModal(true);
        setLocationRelativeTo(parent);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfLogin.getText();
                String password = new String(pfPassword.getPassword());

                user = getAutenticateUser(email,password);

                if(user != null ) {
                    dispose();
                    DashboardForm dashboardForm = new DashboardForm(user, parentFrame);
                    dashboardForm.setVisible(true);

                }
                else {
                    JOptionPane.showMessageDialog(LoginPanel,
                            "Błąd podczas logowania użytkownika!",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });

        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegistrationForm registrationForm = new RegistrationForm(parentFrame);
                registrationForm.setVisible(true);

            }
        });

    }
    public User user;
    private User getAutenticateUser(String email, String password) {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM User WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,email);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user = new User();
                user.setUserID(resultSet.getInt("UserID"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setPassword(resultSet.getString("password"));
            }
            stmt.close();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
