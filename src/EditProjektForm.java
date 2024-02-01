import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

public class EditProjektForm extends JFrame{
    private JPanel EditPanel;
    private JButton dodajZespułButton;
    private JButton dodajKlientaButton;
    private JButton btnEdit;
    private JTextField tfName;
    private JTextField tfOpis;
    private JTextField tfStatus;
    private JSpinner spDataRoz;
    private JSpinner spDataZak;
    private JButton btnBack;
    private User loggedInUser;
    private JFrame parentFrame;

    public EditProjektForm(Projekt selectedProject, User loggedInUser, JFrame parent) {
        this.loggedInUser = loggedInUser;
        this.parentFrame = parent;
        setTitle("System zarządzania projektami IT - Edytuj Projekt");
        setContentPane(EditPanel);
        int width = 900, height = 400;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(width,height));
        setLocationRelativeTo(parent);

        spDataRoz.setModel(new SpinnerDateModel());
        spDataZak.setModel(new SpinnerDateModel());

        tfName.setText(selectedProject.getNazwa());
        tfOpis.setText(selectedProject.getOpis());
        tfStatus.setText(selectedProject.getStatus());
        spDataRoz.setValue(selectedProject.getDataRozpoczecia());
        spDataZak.setValue(selectedProject.getDataZakonczenia());

        // Sprawdzenie czy data nie jest null, jeśli jest, ustaw datę na bieżącą
        Date dataRozpoczecia = selectedProject.getDataRozpoczecia() != null ? selectedProject.getDataRozpoczecia() : new Date();
        Date dataZakonczenia = selectedProject.getDataZakonczenia() != null ? selectedProject.getDataZakonczenia() : new Date();

// Ustawienie modelu dla JSpinner z wartościami dat
        spDataRoz.setModel(new SpinnerDateModel(dataRozpoczecia, null, null, Calendar.DAY_OF_MONTH));
        spDataZak.setModel(new SpinnerDateModel(dataZakonczenia, null, null, Calendar.DAY_OF_MONTH));

        dodajZespułButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddZespolProjektForm addZespolProjektForm = new AddZespolProjektForm(selectedProject, loggedInUser, parentFrame);
                addZespolProjektForm.setVisible(true);
            }
        });
        dodajKlientaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddKlientForm addKlientForm = new AddKlientForm(selectedProject, loggedInUser, parentFrame);
                addKlientForm.setVisible(true);
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aktualizacja danych obiektu selectedProject na podstawie wprowadzonych danych w polach tekstowych i spinnerach
                selectedProject.setNazwa(tfName.getText());
                selectedProject.setOpis(tfOpis.getText());
                selectedProject.setStatus(tfStatus.getText());
                selectedProject.setDataRozpoczecia((Date) spDataRoz.getValue());
                selectedProject.setDataZakonczenia((Date) spDataZak.getValue());

                // Zapisanie zmienionego obiektu do bazy danych
                final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
                final String USERNAME = "root";
                final String PASSWORD = "";

                try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    String sql = "UPDATE Projekt SET Nazwa = ?, Opis = ?, Status = ?, DataRozpoczecia = ?, DataZakonczenia = ? WHERE ProjektID = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setString(1, selectedProject.getNazwa());
                    preparedStatement.setString(2, selectedProject.getOpis());
                    preparedStatement.setString(3, selectedProject.getStatus());
                    preparedStatement.setDate(4, new java.sql.Date(selectedProject.getDataRozpoczecia().getTime()));
                    preparedStatement.setDate(5, new java.sql.Date(selectedProject.getDataZakonczenia().getTime()));
                    preparedStatement.setInt(6, selectedProject.getProjectID());

                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(EditProjektForm.this, "Projekt został pomyślnie zaktualizowany.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(EditProjektForm.this, "Nie udało się zaktualizować projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(EditProjektForm.this, "Wystąpił błąd podczas aktualizacji projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
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
