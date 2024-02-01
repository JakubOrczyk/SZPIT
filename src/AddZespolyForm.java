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

public class AddZespolyForm extends JFrame{
    private JPanel AddZespolPanel;
    private JButton btnAdd;
    private JTextField tfName;
    private JTextField tfRodzaj;
    private JTable zespolyTable;
    private JButton przypiszProgramisteButton;
    private JButton wróćButton;
    DefaultTableModel tableModel;
    java.util.List<Zespol> zespuls = new ArrayList<Zespol>();
    private User loggedInUser;
public AddZespolyForm(User loggedInUser) {
    this.loggedInUser = loggedInUser;
    setContentPane(AddZespolPanel);
    int width = 800, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));

    // Inicjalizacja JTable z pustym modelem
    tableModel = new DefaultTableModel();
    zespolyTable.setModel(tableModel);

    // Dodanie kolumn do modelu
    tableModel.addColumn("ID");
    tableModel.addColumn("Nazwa");
    tableModel.addColumn("Rodzaj");
    tableModel.addColumn("IlośćProgramistów");

    // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";

    String query = "SELECT * FROM Zespol ";

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int zespolID = resultSet.getInt("ZespolID");
            String nazwa = resultSet.getString("Nazwa");
            String liderName = resultSet.getString("LiderName");
            int iloscProgramistow = resultSet.getInt("IloscProgramistow");


            // Tworzenie obiektu klasy Projekt
            Zespol zespol = new Zespol(nazwa, liderName, iloscProgramistow);
            zespol.setIDzespolu(zespolID);

            zespuls.add(zespol);

            // Dodanie danych do modelu
            tableModel.addRow(new Object[]{zespolID, zespol.getNazwa(), zespol.getLiderName(), zespol.getIloscProgramistow()});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    wróćButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            DashboardForm dashboardForm = new DashboardForm(loggedInUser, null);
            dashboardForm.setVisible(true);
        }
    });
    przypiszProgramisteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Sprawdzenie czy został zaznaczony wiersz w tabeli
            int selectedRow = zespolyTable.getSelectedRow();
            if (selectedRow != -1) { // -1 oznacza brak zaznaczenia
                // Pobranie ID zaznaczonego zespołu z modelu tabeli
                int selectedZespolID = (int) tableModel.getValueAt(selectedRow, 0); // Zakładam, że ID zespołu znajduje się w pierwszej kolumnie tabeli

                // Zamknięcie bieżącego okna
                dispose();

                // Przekazanie ID zespołu do nowego okna i otwarcie go
                PrzypiszProgramisteForm przypiszProgramisteForm = new PrzypiszProgramisteForm(loggedInUser, selectedZespolID);
                przypiszProgramisteForm.setVisible(true);
            } else {
                // Wyświetlenie komunikatu o konieczności wyboru zespołu
                JOptionPane.showMessageDialog(AddZespolyForm.this, "Wybierz zespół, aby kontynuować.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    btnAdd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Pobieranie wartości z pól tekstowych
            String nazwa = tfName.getText();
            String rodzaj = tfRodzaj.getText();

            // Tworzenie nowego obiektu zespołu
            Zespol newZespol = new Zespol(nazwa, rodzaj, 0);

            // Dodanie zespołu do listy
            zespuls.add(newZespol);

            // Dodanie zespołu do tabeli i bazy danych
            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                // Wstawianie nowego zespołu do bazy danych
                String insertQuery = "INSERT INTO Zespol (Nazwa, LiderName) VALUES (?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, newZespol.getNazwa());
                insertStatement.setString(2, newZespol.getLiderName());
                insertStatement.executeUpdate();

                // Pobranie automatycznie generowanego ID dla nowego zespołu
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int zespolID = generatedKeys.getInt(1);
                    newZespol.setIDzespolu(zespolID);
                    // Aktualizacja wiersza w tabeli
                    tableModel.addRow(new Object[]{zespolID, newZespol.getNazwa(), newZespol.getLiderName(), 0});
                }

                // Zamykanie zasobów
                generatedKeys.close();
                insertStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AddZespolyForm.this, "Wystąpił błąd podczas dodawania zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

}
}
