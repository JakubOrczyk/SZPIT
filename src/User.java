import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String login;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String password;
    private int userID;
    private List<Klient> klienci;
    public List<Programista> programisci;
    private List<Projekt> projekty;

    public User() {
        this.klienci = new ArrayList<>();
        this.programisci = new ArrayList<Programista>();
        this.projekty = new ArrayList<Projekt>();
    }
    public List<Projekt> getProjekty() {
        return this.projekty;
    }
    public List<Programista> getProgramisci() {
        return this.programisci;
    }

    public void dodajKlienta(Klient klient) {
        this.klienci.add(klient);
    }

    public void dodajProjekt(Projekt projekt) {
        this.projekty.add(projekt);
    }
    public void dodajProgramiste(Programista programista) {
        this.programisci.add(programista);
    }

    // Gettery i settery dla pól prywatnych
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}

