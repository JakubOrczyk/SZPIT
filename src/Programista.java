

import java.util.ArrayList;
import java.util.List;

public class Programista extends Osoba{

    private String skills;
    private int tasks;
    private List<Zadanie> przypisaneZadania;

    public Programista(String name, String surname, String email, String phone, String address, String skills, int tasks) {
        super(name, surname, email, phone, address);
        this.skills = skills;
        this.tasks = tasks;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public int getTasks() {
        return tasks;
    }

    public void setTasks(int tasks) {
        this.tasks = tasks;
    }

    public List<Zadanie> getPrzypisaneZadania() {
        return przypisaneZadania;
    }

    public void setPrzypisaneZadania(List<Zadanie> przypisaneZadania) {
        this.przypisaneZadania = przypisaneZadania;
    }

    public void addZadanie(Zadanie zadanie) {
        this.przypisaneZadania.add(zadanie);
    }
}
