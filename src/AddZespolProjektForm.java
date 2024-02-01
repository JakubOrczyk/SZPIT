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
    private JFrame parentFrame;
    private User loggedInUser;
    public AddZespolProjektForm(Projekt selectedProject, User loggedInUser, JFrame parent) {
        this.parentFrame = parent;
        this.loggedInUser = loggedInUser;
        setTitle("System zarządzania projektami IT - Dodaj zespół");
        setContentPane(AddZespulProjektPanel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        int width = 800, height = 600;
        setMinimumSize(new Dimension(width, height));
        setModal(true);
        setLocationRelativeTo(parent);


        tableModel = new DefaultTableModel();
        zespulTable.setModel(tableModel);


        tableModel.addColumn("ID");
        tableModel.addColumn("Nazwa");
        tableModel.addColumn("Rodzaj");
        tableModel.addColumn("ilośćProgramistów");

        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM Zespol WHERE UserID = " + loggedInUser.getUserID();
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
                EditProjektForm editProjektForm = new EditProjektForm(selectedProject, loggedInUser, parentFrame);
                editProjektForm.setVisible(true);
            }
        });

        przypiszZespulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = zespulTable.getSelectedRow();
                if (selectedRow != -1) {
                    int selectedZespolID = (int) tableModel.getValueAt(selectedRow, 0);
                    try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        String updateQuery = "UPDATE Projekt SET ZespolID = ? WHERE UserID = ? AND ProjektID = ?";
                        PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);
                        preparedStatement.setInt(1, selectedZespolID);
                        preparedStatement.setInt(2, loggedInUser.getUserID());
                        preparedStatement.setInt(3, selectedProject.getProjectID());
                        int rowsAffected = preparedStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(AddZespolProjektForm.this, "Zespół został przypisany do projektu.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(AddZespolProjektForm.this, "Nie udało się przypisać zespołu do projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(AddZespolProjektForm.this, "Wystąpił błąd podczas przypisywania zespołu do projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AddZespolProjektForm.this, "Proszę wybrać zespół.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



    }


}
