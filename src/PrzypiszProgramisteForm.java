import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class PrzypiszProgramisteForm extends JFrame{
    private JPanel PrzypiszProgramistePanel;
    private JTable zespolPTable;
    private JTable WszyscyTable;
    private JButton btnAdd;
    private JButton btnDelete;
    private JButton btnBack;
    private User loggedInUser;
    private int idZespolu;
    DefaultTableModel tableModel;
    DefaultTableModel table2Model;
    java.util.List<Programista> programisci = new ArrayList<Programista>();
    java.util.List<Programista> programisciZespol = new ArrayList<Programista>();
    private JFrame parentFrame;
public PrzypiszProgramisteForm(User loggedInUser,int selectedZespolID, JFrame parent) {
    this.loggedInUser = loggedInUser;
    this.parentFrame = parent;
    this.idZespolu = selectedZespolID;
    setContentPane(PrzypiszProgramistePanel);
    int width = 1200, height = 600;
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(width,height));
    System.out.println(selectedZespolID);
    setLocationRelativeTo(parent);
    setTitle("System zarządzania projektami IT - Przypisz Programiste");
    // Inicjalizacja JTable z pustym modelem
    tableModel = new DefaultTableModel();
    WszyscyTable.setModel(tableModel);

    table2Model = new DefaultTableModel();
    zespolPTable.setModel(table2Model);

    // Dodanie kolumn do modelu
    tableModel.addColumn("ID");
    tableModel.addColumn("Imie");
    tableModel.addColumn("Nazwisko");
    tableModel.addColumn("Email");
    tableModel.addColumn("Phone");
    tableModel.addColumn("Address");
    tableModel.addColumn("Skills");
    tableModel.addColumn("IDzespolu");

    table2Model.addColumn("ID");
    table2Model.addColumn("Imie");
    table2Model.addColumn("Nazwisko");
    table2Model.addColumn("Email");
    table2Model.addColumn("Phone");
    table2Model.addColumn("Address");
    table2Model.addColumn("Skills");
    table2Model.addColumn("IDzespolu");

    // Pobranie danych z bazy i wyświetlenie w panelu scrollowalnym
    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";

    String query = "SELECT * FROM Programista " +
            "WHERE (IDzespolu IS NULL OR IDzespolu != " + selectedZespolID + ") " +
            "AND UserID = " + loggedInUser.getUserID();

    String query2 = "SELECT * FROM Programista " +
            "WHERE IDzespolu = " + selectedZespolID + " " +
            "AND UserID = " + loggedInUser.getUserID();



    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query2);

        while (resultSet.next()) {
            int programistaaID = resultSet.getInt("ProgramistaID");
            String nazwa = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            String skills = resultSet.getString("Skills");
            int idzespolu = resultSet.getInt("IDzespolu");


            // Tworzenie obiektu klasy Projekt
            Programista programista = new Programista(nazwa, surname, email,phone,address,skills, idzespolu);


            programisciZespol.add(programista);

            // Dodanie danych do modelu
            table2Model.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills()});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int programistaaID = resultSet.getInt("ProgramistaID");
            String nazwa = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            String skills = resultSet.getString("Skills");
            int idzespolu = resultSet.getInt("IDzespolu");


            // Tworzenie obiektu klasy Projekt
            Programista programista = new Programista(nazwa, surname, email,phone,address,skills, idzespolu);


            programisci.add(programista);

            // Dodanie danych do modelu
            tableModel.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills()});
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
            AddZespolyForm addZespolyForm = new AddZespolyForm(loggedInUser, parentFrame);
            addZespolyForm.setVisible(true);
        }
    });
    btnAdd.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = WszyscyTable.getSelectedRow();
            if (selectedRow != -1) {
                int selectedProgramistaID = (int) tableModel.getValueAt(selectedRow, 0);
                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    // Aktualizacja pola IDZespolu dla wybranego programisty
                    String updateQuery = "UPDATE Programista SET IDZespolu = ? WHERE ProgramistaID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, selectedZespolID);
                    updateStatement.setInt(2, selectedProgramistaID);
                    updateStatement.executeUpdate();
                    updateStatement.close();

                    // Dodanie programisty do drugiej listy (zespołu)
                    Programista selectedProgramista = programisci.get(selectedRow);
                    table2Model.addRow(new Object[]{selectedProgramistaID, selectedProgramista.getName(), selectedProgramista.getSurname(), selectedProgramista.getEmail(), selectedProgramista.getPhone(), selectedProgramista.getAddress(), selectedProgramista.getSkills()});

                    // Usunięcie dodanego programisty z pierwszej listy (wszyscy)
                    tableModel.removeRow(selectedRow);
                    programisci.remove(selectedRow);

                    // Zwiększenie liczby programistów w zespole o 1
                    String incrementQuery = "UPDATE Zespol SET IloscProgramistow = IloscProgramistow + 1 WHERE ZespolID = ?";
                    PreparedStatement incrementStatement = connection.prepareStatement(incrementQuery);
                    incrementStatement.setInt(1, idZespolu);
                    incrementStatement.executeUpdate();
                    incrementStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas przypisywania programisty do zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Proszę wybrać programistę z listy.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
    btnDelete.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = zespolPTable.getSelectedRow();
            if (selectedRow != -1) {
                int selectedProgramistaID = (int) table2Model.getValueAt(selectedRow, 0);
                try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                    // Aktualizacja pola IDzespolu na null w bazie danych dla wybranego programisty
                    String updateQuery = "UPDATE Programista SET IDZespolu = NULL WHERE ProgramistaID = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setInt(1, selectedProgramistaID);
                    updateStatement.executeUpdate();
                    updateStatement.close();

                    // Dodanie usuniętego programisty do tabeli wszystkich programistów
                    Programista removedProgramista = programisciZespol.get(selectedRow);
                    tableModel.addRow(new Object[]{selectedProgramistaID, removedProgramista.getName(), removedProgramista.getSurname(), removedProgramista.getEmail(), removedProgramista.getPhone(), removedProgramista.getAddress(), removedProgramista.getSkills(), null});

                    // Usuwanie programisty z tabeli programistów w zespole
                    table2Model.removeRow(selectedRow);
                    programisciZespol.remove(selectedRow);

                    // Zmniejszenie liczby programistów w zespole o 1
                    String decrementQuery = "UPDATE Zespol SET IloscProgramistow = IloscProgramistow - 1 WHERE ZespolID = ?";
                    PreparedStatement decrementStatement = connection.prepareStatement(decrementQuery);
                    decrementStatement.setInt(1, idZespolu);
                    decrementStatement.executeUpdate();
                    decrementStatement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas usuwania programisty z zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Proszę wybrać programistę z zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });



}
}
