import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class AddZespolyForm extends JFrame{
    private JPanel AddZespolPanel;
    private JButton btnAdd;
    private JTextField tfName;
    private JTextField tfRodzaj;
    private JTable zespolyTable;
    private JButton przypiszProgramisteButton;
    private JButton wróćButton;
    private JButton btnDelete;
    private JFrame parentFrame;
    private User loggedInUser;
    DefaultTableModel tableModel;
    java.util.List<Zespol> zespuls = new ArrayList<Zespol>();
public AddZespolyForm(User loggedInUser, JFrame parent) {
    this.loggedInUser = loggedInUser;
    this.parentFrame = parent;
    setContentPane(AddZespolPanel);
    int width = 800, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    setLocationRelativeTo(parent);
    setTitle("System zarządzania projektami IT - Zespoły");

    tableModel = new DefaultTableModel();
    zespolyTable.setModel(tableModel);

    tableModel.addColumn("ID");
    tableModel.addColumn("Nazwa");
    tableModel.addColumn("Rodzaj");
    tableModel.addColumn("IlośćProgramistów");

    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";
    String query = "SELECT * FROM Zespol WHERE UserID = " + loggedInUser.getUserID();

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            int zespolID = resultSet.getInt("ZespolID");
            String nazwa = resultSet.getString("Nazwa");
            String liderName = resultSet.getString("LiderName");
            int iloscProgramistow = resultSet.getInt("IloscProgramistow");
            String query2 = "SELECT COUNT(*) AS RecordCount FROM Programista WHERE IDzespolu = "+zespolID;
            int liczba = 0;
            try {
                Statement statementt = connection.createStatement();
                ResultSet resultSett = statementt.executeQuery(query2);
                while (resultSett.next()) {
                    liczba = resultSett.getInt("RecordCount");
                }
                resultSett.close();
                statementt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
            Zespol zespol = new Zespol(nazwa, liderName, liczba);
            zespol.setIDzespolu(zespolID);
            zespuls.add(zespol);
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
            DashboardForm dashboardForm = new DashboardForm(loggedInUser, parentFrame);
            dashboardForm.setVisible(true);
        }
    });
    przypiszProgramisteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            int selectedRow = zespolyTable.getSelectedRow();
            if (selectedRow != -1) {
                int selectedZespolID = (int) tableModel.getValueAt(selectedRow, 0);
                dispose();
                PrzypiszProgramisteForm przypiszProgramisteForm = new PrzypiszProgramisteForm(loggedInUser, selectedZespolID, parentFrame);
                przypiszProgramisteForm.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(AddZespolyForm.this, "Wybierz zespół, aby kontynuować.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    btnAdd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nazwa = tfName.getText();
            String rodzaj = tfRodzaj.getText();
            Zespol newZespol = new Zespol(nazwa, rodzaj, 0);
            zespuls.add(newZespol);

            try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                String insertQuery = "INSERT INTO Zespol (Nazwa, LiderName, IloscProgramistow, UserID) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, newZespol.getNazwa());
                insertStatement.setString(2, newZespol.getLiderName());
                insertStatement.setInt(3, 0);
                insertStatement.setInt(4, loggedInUser.getUserID());
                insertStatement.executeUpdate();
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int zespolID = generatedKeys.getInt(1);
                    newZespol.setIDzespolu(zespolID);
                    tableModel.addRow(new Object[]{zespolID, newZespol.getNazwa(), newZespol.getLiderName(), 0});
                }
                generatedKeys.close();
                insertStatement.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(AddZespolyForm.this, "Wystąpił błąd podczas dodawania zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = zespolyTable.getSelectedRow();
            if (selectedRow != -1) {
                int selectedZespolID = (int) tableModel.getValueAt(selectedRow, 0);
                try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    String deleteQuery = "DELETE FROM Zespol WHERE ZespolID = ?";
                    PreparedStatement deleteStatement = conn.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, selectedZespolID);
                    int rowsAffected = deleteStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(AddZespolyForm.this, "Zespół został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(AddZespolyForm.this, "Nie udało się usunąć zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(AddZespolyForm.this, "Wystąpił błąd podczas usuwania zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(AddZespolyForm.this, "Wybierz zespół do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
}
}
