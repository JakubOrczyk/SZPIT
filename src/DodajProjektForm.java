import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class DodajProjektForm extends JDialog {
    private JPanel AddProjektPanel;
    private JTextField tfName;
    private JTextField tfOpis;
    private JTextField tfStatus;
    private JSpinner spDataRoz;
    private JSpinner spdataZak;
    private JButton btnBack;
    private JButton btnAdd;
    private User loggedInUser;

    public DodajProjektForm(User loggedInUser) {
        this.loggedInUser = loggedInUser;
        setTitle("Create a new account");
        setContentPane(AddProjektPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setModal(true);


        spDataRoz.setModel(new SpinnerDateModel());
        spdataZak.setModel(new SpinnerDateModel());
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashboardForm dashboardForm = new DashboardForm(loggedInUser, null);
                dashboardForm.setVisible(true);
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = tfName.getText();
                String opis = tfOpis.getText();
                String status = tfStatus.getText();
                int UserID = loggedInUser.getUserID();
                // Pobranie dat z spinnerów
                Date dataRozpoczecia = (Date) spDataRoz.getValue();
                Date dataZakonczenia = (Date) spdataZak.getValue();

                // Sprawdzenie czy pola nie są puste
                if (name.isEmpty() || opis.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(DodajProjektForm.this, "Wszystkie pola są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Dodanie projektu do bazy danych
                final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
                final String USERNAME = "root";
                final String PASSWORD = "";

                try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    String sql = "INSERT INTO Projekt (Nazwa, Opis, Status, DataRozpoczecia, DataZakonczenia, UserID) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, opis);
                    preparedStatement.setString(3, status);
                    preparedStatement.setDate(4, new java.sql.Date(dataRozpoczecia.getTime()));
                    preparedStatement.setDate(5, new java.sql.Date(dataZakonczenia.getTime()));
                    preparedStatement.setInt(6, UserID);

                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        JOptionPane.showMessageDialog(DodajProjektForm.this, "Projekt został pomyślnie dodany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(DodajProjektForm.this, "Nie udało się dodać projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DodajProjektForm.this, "Wystąpił błąd podczas dodawania projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
