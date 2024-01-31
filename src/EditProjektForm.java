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

public class EditProjektForm extends JFrame{
    private JPanel EditPanel;
    private JButton dodajZespułButton;
    private JButton dodajKlientaButton;
    private JButton btnEdit;

    public EditProjektForm(Projekt selectedProject) {
        setContentPane(EditPanel);
        int width = 800, height = 600;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(width,height));

    }
}
