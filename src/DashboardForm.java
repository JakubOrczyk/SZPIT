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


public class DashboardForm extends JFrame{
    private JPanel dashboardPanel;
    private JLabel IbAdmin;
    private JButton btnAdd;
    private JButton btnEdit;
    private JButton btnDelete;
    private JButton btnPodglad;
    private JTable ClientsTable;
    private JButton btnClose;
    private JButton btnZespoly;
    private JButton btnProg;
    private User loggedInUser;
    private JFrame parentFrame;
    List<Projekt> projects = new ArrayList<Projekt>();
    DefaultTableModel tableModel;
    public DashboardForm(User loggedInUser, JFrame parent) {
        super("System zarządzania projektami IT");
        this.loggedInUser = loggedInUser;
        this.parentFrame = parent;
        setContentPane(dashboardPanel);
        int width = 800, height = 600;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(width,height));
        setLocationRelativeTo(parent);

        tableModel = new DefaultTableModel();
        ClientsTable.setModel(tableModel);

        tableModel.addColumn("ID");
        tableModel.addColumn("Nazwa");
        tableModel.addColumn("Opis");
        tableModel.addColumn("Status");

        final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";
        int loggedInUserID = loggedInUser.getUserID();
        String query = "SELECT * FROM Projekt WHERE UserID = " + loggedInUserID;

        try{
            Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int projectID = resultSet.getInt("ProjektID");
                String name = resultSet.getString("Nazwa");
                String description = resultSet.getString("Opis");
                String status = resultSet.getString("Status");
                Date dataR = resultSet.getDate("DataRozpoczecia");
                Date dataZ = resultSet.getDate("DataZakonczenia");

                Projekt projekt = new Projekt(name, description, dataR, dataZ);
                projekt.setStatus(status);
                projects.add(projekt);

                tableModel.addRow(new Object[]{projectID, projekt.getNazwa(), projekt.getOpis(), projekt.getStatus()});
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }



        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(null);
            }
        });
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DodajProjektForm dodajProjektForm = new DodajProjektForm(loggedInUser, projects, parentFrame);
                dodajProjektForm.setVisible(true);
            }
        });
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ClientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Wybierz projekt do edycji.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int projectID = (int) ClientsTable.getValueAt(selectedRow, 0);
                String projectName = (String) ClientsTable.getValueAt(selectedRow, 1);
                String projectDescription = (String) ClientsTable.getValueAt(selectedRow, 2);

                Projekt selectedProject = null;
                for (Projekt project : projects) {
                    if (project.getNazwa().equals(projectName) && project.getOpis().equals(projectDescription)) {
                        selectedProject = project;
                        break;
                    }
                }
                selectedProject.setID(projectID);
                if (selectedProject == null) {
                    JOptionPane.showMessageDialog(null, "Nie można znaleźć wybranego projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dispose();
                EditProjektForm editProjektForm = new EditProjektForm(selectedProject, loggedInUser,parentFrame);
                editProjektForm.setVisible(true);
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ClientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Wybierz projekt do usunięcia.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int projectID = (int) ClientsTable.getValueAt(selectedRow, 0);

                final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
                final String USERNAME = "root";
                final String PASSWORD = "";

                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                     PreparedStatement statement = connection.prepareStatement("DELETE FROM Projekt WHERE ProjektID = ?")) {
                    statement.setInt(1, projectID);
                    int rowsAffected = statement.executeUpdate();

                    if (rowsAffected > 0) {
                        tableModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Projekt został pomyślnie usunięty.", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie udało się usunąć projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas usuwania projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnPodglad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = ClientsTable.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Wybierz projekt do podglądu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int projectID = (int) ClientsTable.getValueAt(selectedRow, 0);
                String projectName = (String) ClientsTable.getValueAt(selectedRow, 1);
                String projectDescription = (String) ClientsTable.getValueAt(selectedRow, 2);

                Projekt selectedProject = null;
                for (Projekt project : projects) {
                    if (project.getNazwa().equals(projectName) && project.getOpis().equals(projectDescription)) {
                        selectedProject = project;
                        break;
                    }
                }
                selectedProject.setID(projectID);
                if (selectedProject == null) {
                    JOptionPane.showMessageDialog(null, "Nie można znaleźć wybranego projektu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dispose();
                PodgladProjektuForm podgladProjektuForm = new PodgladProjektuForm(selectedProject, loggedInUser, parentFrame);
                podgladProjektuForm.setVisible(true);
            }
        });
        btnZespoly.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AddZespolyForm addZespolyForm = new AddZespolyForm(loggedInUser, parentFrame);
                addZespolyForm.setVisible(true);
            }
        });
        btnProg.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                ProgramisciForm programisciForm = new ProgramisciForm(loggedInUser, parentFrame);
                programisciForm.setVisible(true);
            }
        });
    }
}
