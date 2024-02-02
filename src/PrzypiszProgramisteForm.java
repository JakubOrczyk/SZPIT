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
    private JFrame parentFrame;
    DefaultTableModel tableModel;
    DefaultTableModel table2Model;
    java.util.List<Programista> programisci = new ArrayList<Programista>();
    java.util.List<Programista> programisciZespol = new ArrayList<Programista>();
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
    setTitle("System zarządzania projektami IT - Przypisz Pracownika");
    tableModel = new DefaultTableModel();
    WszyscyTable.setModel(tableModel);

    table2Model = new DefaultTableModel();
    zespolPTable.setModel(table2Model);

    tableModel.addColumn("ID");
    tableModel.addColumn("Imie");
    tableModel.addColumn("Nazwisko");
    tableModel.addColumn("Email");
    tableModel.addColumn("Phone");
    tableModel.addColumn("Address");
    tableModel.addColumn("Skills");
    tableModel.addColumn("Stanowisko");

    table2Model.addColumn("ID");
    table2Model.addColumn("Imie");
    table2Model.addColumn("Nazwisko");
    table2Model.addColumn("Email");
    table2Model.addColumn("Phone");
    table2Model.addColumn("Address");
    table2Model.addColumn("Skills");
    table2Model.addColumn("Stanowisko");

    final String DB_URL = "jdbc:mysql://localhost/SZPIT?serverTimezone=UTC";
    final String USERNAME = "root";
    final String PASSWORD = "";

    String query = "SELECT * FROM Programista " + "WHERE (IDzespolu IS NULL OR IDzespolu != " + selectedZespolID + ") " + "AND UserID = " + loggedInUser.getUserID();
    String query2 = "SELECT * FROM Programista " + "WHERE IDzespolu = " + selectedZespolID + " " + "AND UserID = " + loggedInUser.getUserID();

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
            String stanowisko = resultSet.getString("Staowisko");
            int idzespolu = resultSet.getInt("IDzespolu");

            Programista programista = new Programista(nazwa, surname, email,phone,address,programistaaID,"Programista",2000,skills, idzespolu);
            programisciZespol.add(programista);
            table2Model.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills(), stanowisko});
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
            String stanowisko = resultSet.getString("Staowisko");
            int idzespolu = resultSet.getInt("IDzespolu");
            Programista programista = new Programista(nazwa, surname, email,phone,address,programistaaID,"Programista",2000,skills, idzespolu);
            programisci.add(programista);
            tableModel.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills(),stanowisko});
        }

        resultSet.close();
        statement.close();
        connection.close();

    } catch (SQLException e) {
        e.printStackTrace();
    }
    String query3 = "SELECT * FROM Tester " + "WHERE (IDzespolu IS NULL OR IDzespolu != " + selectedZespolID + ") " + "AND UserID = " + loggedInUser.getUserID();
    String query4 = "SELECT * FROM Tester " + "WHERE IDzespolu = " + selectedZespolID + " " + "AND UserID = " + loggedInUser.getUserID();

    try{
        Connection connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query4);

        while (resultSet.next()) {
            int programistaaID = resultSet.getInt("TesterID");
            String nazwa = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            String skills = resultSet.getString("Skills");
            String stanowisko = resultSet.getString("Stanowisko");
            int idzespolu = resultSet.getInt("IDzespolu");

            Programista programista = new Programista(nazwa, surname, email,phone,address,programistaaID,"Tester",2000,skills, idzespolu);
            programisciZespol.add(programista);
            table2Model.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills(), stanowisko});
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
        ResultSet resultSet = statement.executeQuery(query3);

        while (resultSet.next()) {
            int programistaaID = resultSet.getInt("TesterID");
            String nazwa = resultSet.getString("Name");
            String surname = resultSet.getString("Surname");
            String email = resultSet.getString("Email");
            String phone = resultSet.getString("Phone");
            String address = resultSet.getString("Address");
            String skills = resultSet.getString("Skills");
            String stanowisko = resultSet.getString("Stanowisko");
            int idzespolu = resultSet.getInt("IDzespolu");
            Programista programista = new Programista(nazwa, surname, email,phone,address,programistaaID,"Tester",2000,skills, idzespolu);
            programisci.add(programista);
            tableModel.addRow(new Object[]{programistaaID, programista.getName(), programista.getSurname(), programista.getEmail(), programista.getPhone(), programista.getAddress(), programista.getSkills(),stanowisko});
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
            String stanowisko = (String) WszyscyTable.getValueAt(selectedRow, 7);

            if (selectedRow != -1) {
                if("Programista".equals(stanowisko)){
                    int selectedProgramistaID = (int) tableModel.getValueAt(selectedRow, 0);
                    try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        String updateQuery = "UPDATE Programista SET IDZespolu = ? WHERE ProgramistaID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, selectedZespolID);
                        updateStatement.setInt(2, selectedProgramistaID);
                        updateStatement.executeUpdate();
                        updateStatement.close();

                        Programista selectedProgramista = programisci.get(selectedRow);
                        table2Model.addRow(new Object[]{selectedProgramistaID, selectedProgramista.getName(), selectedProgramista.getSurname(), selectedProgramista.getEmail(), selectedProgramista.getPhone(), selectedProgramista.getAddress(), selectedProgramista.getSkills(),stanowisko});

                        tableModel.removeRow(selectedRow);
                        programisci.remove(selectedRow);

                        String incrementQuery = "UPDATE Zespol SET IloscProgramistow = IloscProgramistow + 1 WHERE ZespolID = ?";
                        PreparedStatement incrementStatement = connection.prepareStatement(incrementQuery);
                        incrementStatement.setInt(1, idZespolu);
                        incrementStatement.executeUpdate();
                        incrementStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas przypisywania programisty do zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }

                } else if ("Tester".equals(stanowisko))
                {
                    int selectedProgramistaID = (int) tableModel.getValueAt(selectedRow, 0);
                    try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        String updateQuery = "UPDATE Tester SET IDZespolu = ? WHERE TesterID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, selectedZespolID);
                        updateStatement.setInt(2, selectedProgramistaID);
                        updateStatement.executeUpdate();
                        updateStatement.close();

                        Programista selectedProgramista = programisci.get(selectedRow);
                        table2Model.addRow(new Object[]{selectedProgramistaID, selectedProgramista.getName(), selectedProgramista.getSurname(), selectedProgramista.getEmail(), selectedProgramista.getPhone(), selectedProgramista.getAddress(), selectedProgramista.getSkills(),stanowisko});

                        tableModel.removeRow(selectedRow);
                        programisci.remove(selectedRow);


                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas przypisywania testera do zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
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
            String stanowisko = (String) zespolPTable.getValueAt(selectedRow, 7);

            if (selectedRow != -1) {
                if("Programista".equals(stanowisko)){
                    int selectedProgramistaID = (int) table2Model.getValueAt(selectedRow, 0);
                    try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        String updateQuery = "UPDATE Programista SET IDZespolu = NULL WHERE ProgramistaID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, selectedProgramistaID);
                        updateStatement.executeUpdate();
                        updateStatement.close();

                        Programista removedProgramista = programisciZespol.get(selectedRow);
                        tableModel.addRow(new Object[]{selectedProgramistaID, removedProgramista.getName(), removedProgramista.getSurname(), removedProgramista.getEmail(), removedProgramista.getPhone(), removedProgramista.getAddress(), removedProgramista.getSkills(), stanowisko});
                        table2Model.removeRow(selectedRow);
                        programisciZespol.remove(selectedRow);

                        String decrementQuery = "UPDATE Zespol SET IloscProgramistow = IloscProgramistow - 1 WHERE ZespolID = ?";
                        PreparedStatement decrementStatement = connection.prepareStatement(decrementQuery);
                        decrementStatement.setInt(1, idZespolu);
                        decrementStatement.executeUpdate();
                        decrementStatement.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas usuwania programisty z zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                } else if ("Tester".equals(stanowisko)){
                    int selectedProgramistaID = (int) table2Model.getValueAt(selectedRow, 0);
                    try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD)) {
                        String updateQuery = "UPDATE Tester SET IDZespolu = NULL WHERE TesterID = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, selectedProgramistaID);
                        updateStatement.executeUpdate();
                        updateStatement.close();

                        Programista removedProgramista = programisciZespol.get(selectedRow);
                        tableModel.addRow(new Object[]{selectedProgramistaID, removedProgramista.getName(), removedProgramista.getSurname(), removedProgramista.getEmail(), removedProgramista.getPhone(), removedProgramista.getAddress(), removedProgramista.getSkills(), stanowisko});
                        table2Model.removeRow(selectedRow);
                        programisciZespol.remove(selectedRow);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Wystąpił błąd podczas usuwania testera z zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
                    }
                }


            } else {
                JOptionPane.showMessageDialog(PrzypiszProgramisteForm.this, "Proszę wybrać programistę z zespołu.", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    });



}
}
