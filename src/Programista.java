

import java.util.ArrayList;
import java.util.List;

public class Programista extends Pracownik{

    private String skills;
    private int idZespolu;


    public Programista(String name, String surname, String email, String phone, String address, int idPracownika, String stanowisko, double pensja, String skills, int idZespolu) {
        super(name, surname, email, phone, address, idPracownika, stanowisko, pensja);
        this.skills = skills;
        this.idZespolu = idZespolu;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getTasks() {
        return idZespolu;
    }

    public void setTasks(int idZespolu) {
        this.idZespolu = idZespolu;
    }


}
