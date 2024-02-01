import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class AddZespolProjektForm extends JDialog{
    private JPanel AddZespulProjektPanel;
    private JTable zespulTable;
    private JButton przypiszZespulButton;
    private JButton btnBack;

    private List<Zespol> zespoly = new ArrayList<>();
    DefaultTableModel tableModel;
    private User loggedInUser;
    public AddZespolProjektForm(Projekt selectedProject, User loggedInUser) {
        this.loggedInUser = loggedInUser;
        setTitle("Create a new account");
        setContentPane(AddZespulProjektPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setModal(true);

        // Inicjalizacja JTable z pustym modelem
        tableModel = new DefaultTableModel();
        zespulTable.setModel(tableModel);

        // Dodanie kolumn do modelu
        tableModel.addColumn("ID");
        tableModel.addColumn("Nazwa");
        tableModel.addColumn("ImieLidera");
        tableModel.addColumn("ilośćProgramistów");

        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Zespol";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int zespolID = resultSet.getInt("ZespolID");
                String nazwa = resultSet.getString("Nazwa");
                String imieLidera = resultSet.getString("LiderName");
                int iloscProgramistow = resultSet.getInt("IloscProgramistow");

                Zespol zespol = new Zespol(nazwa, imieLidera,iloscProgramistow );
                zespol.setIDzespolu(zespolID);
                zespoly.add(zespol);
                tableModel.addRow(new Object[]{zespolID, zespol.getNazwa(), zespol.getLiderName(), zespol.getIloscProgramistow()});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                EditProjektForm editProjektForm = new EditProjektForm(selectedProject, loggedInUser);
                editProjektForm.setVisible(true);
            }
        });

        przypiszZespulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


    }


}
