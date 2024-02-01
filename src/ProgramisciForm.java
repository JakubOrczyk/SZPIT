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
    private JComboBox comboBox1;
    DefaultTableModel tableModel;
    private User loggedInUser;
    private JFrame parentFrame;
public ProgramisciForm(User loggedInUser, JFrame parent) {
    this.parentFrame = parent;
    this.loggedInUser = loggedInUser;
    setContentPane(ProgramistaPanel);
    int width = 1200, height = 400;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    setLocationRelativeTo(parent);
    setTitle("System zarządzania projektami IT - Pracownicy");


    tableModel = new DefaultTableModel();
    ProgramistaTable.setModel(tableModel);


    tableModel.addColumn("ID");
    tableModel.addColumn("Imię");
    tableModel.addColumn("Nazwisko");
    tableModel.addColumn("Email");
    tableModel.addColumn("Umiejętności");
    tableModel.addColumn("Telefon");
    tableModel.addColumn("Adres");
    tableModel.addColumn("Stanowisko");
    int licznik = 0;
    int licznik1 = 0;
    int idZespolu = 0;
    int programistaID = 0;
    int testerID = 0;
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    String query = "SELECT * FROM Programista WHERE UserID = " + loggedInUser.getUserID();
    String query1 = "SELECT * FROM Tester WHERE UserID = " + loggedInUser.getUserID();
    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        Statement statementt = connection.createStatement();
        ResultSet resultSett = statementt.executeQuery(query1);

        while (resultSet.next()) {
            programistaID = resultSet.getInt("ProgramistaID");
            String name = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String skills = resultSet.getString("Skills");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            idZespolu = resultSet.getInt("IDzespolu");
            String stanowisko = resultSet.getString("Staowisko");

            Programista programista = new Programista(name, surname, email, phone, address, programistaID, "Programista",2000,skills,idZespolu);
                licznik = programistaID+1;
            tableModel.addRow(new Object[]{programistaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getSkills(), programista.getPhone(), programista.getAddress(), stanowisko});
        }

        while (resultSett.next()) {
            testerID = resultSett.getInt("TesterID");
            String name = resultSett.getString("Name");
            String surname = resultSett.getString("Surname");
            String email = resultSett.getString("Email");
            String skills = resultSett.getString("Skills");
            String phone = resultSett.getString("Phone");
            String address = resultSett.getString("Address");
            idZespolu = resultSett.getInt("IDzespolu");
            String stanowisko = resultSett.getString("Stanowisko");

            Tester tester = new Tester(name, surname, email, phone, address, testerID, "Programista",2000,skills,idZespolu);
            licznik1 = testerID+1;
            tableModel.addRow(new Object[]{testerID, tester.getName(), tester.getSurname(), tester.getEmail(), tester.getTypTestow(), tester.getPhone(), tester.getAddress(), stanowisko});
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
    int finalLicznik1 = licznik1;
    int finalIdZespolu = idZespolu;
    int finalProgramistaID = programistaID;
    int finalTesterID = testerID;
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


            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || jezyk.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wszystkie pola są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny adres e-mail!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidPhoneNumber(phone)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny numer telefonu!\n Podaj numer w formacie XXX XXX XXX", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }


            if (!isValidAddress(address)) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Niepoprawny adres!\n Podaj miasto i numer domu.\n np:(Warszawa 00)", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedOption = (String) comboBox1.getSelectedItem();
            if (selectedOption.equals("Programista")) {

                Programista newProgramista = new Programista(name, surname, email, phone, address, finalProgramistaID+1, "Programista", 2000 , jezyk , finalIdZespolu);

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

                        tableModel.addRow(new Object[]{ liczbaa,newProgramista.getName(), surname, email,jezyk,phone,address,newProgramista.getStanowisko()});
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
            } else if (selectedOption.equals("Tester")) {

                Tester newProgramista = new Tester(name, surname, email, phone, address, finalTesterID+1, "Tester", 2000 , jezyk , finalIdZespolu);

                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    String insertQuery = "INSERT INTO Tester (Name, Surname, Email, Skills, Phone, Address, UserID) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
                        int liczbaa1 = finalLicznik1;
                        JOptionPane.showMessageDialog(ProgramistaPanel, "tester został pomyślnie dodany do bazy danych.");

                        tableModel.addRow(new Object[]{ liczbaa1,newProgramista.getName(), surname, email,jezyk,phone,address,newProgramista.getStanowisko()});
                        tfName.setText("");
                        tfSurname.setText("");
                        tfEmail.setText("");
                        tfJezyk.setText("");
                        tfPhone.setText("");
                        tfAddress.setText("");
                    } else {
                        JOptionPane.showMessageDialog(ProgramistaPanel, "Nie udało się dodać testera do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Wystąpił błąd podczas dodawania testera do bazy danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
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

    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = ProgramistaTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ProgramistaPanel, "Wybierz pracownika do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String stanowisko = (String) ProgramistaTable.getValueAt(selectedRow, 7);
            int employeeID = (int) ProgramistaTable.getValueAt(selectedRow, 0);

            if("Programista".equals(stanowisko))
            {
                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM Programista WHERE ProgramistaID = ?")) {
                    statement.setInt(1, employeeID);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {

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
            else if ("Tester".equals(stanowisko)){
                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM Tester WHERE TesterID = ?")) {
                    statement.setInt(1, employeeID);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {

                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(ProgramistaPanel, "Tester został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(ProgramistaPanel, "Nie udało się usunąć testera.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(ProgramistaPanel, "Wystąpił błąd podczas usuwania testera.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }



        }
    });

}

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
