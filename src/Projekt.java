

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Projekt {
    private String nazwa;

    private int idProjektu;
    private String opis;
    private Date dataRozpoczecia;
    private Date dataZakonczenia;
    private String status;
    private List<Zadanie> zadania;
    private Zespol zespol;
    private Programista przypisanyProgramista;

    public Projekt(String nazwa, String opis, Date dataRozpoczecia, Date dataZakonczenia) {
        this.nazwa = nazwa;
        this.opis = opis;
        this.dataRozpoczecia = dataRozpoczecia;
        this.dataZakonczenia = dataZakonczenia;
        this.zadania = new ArrayList<>();
    }
    public void przypiszProgramiste(Programista programista) {
        this.przypisanyProgramista = programista;
    }
    public void przypiszZespol(Zespol zespol) {
        this.zespol = zespol;
    }

    public void wyswietlProgramistow() {
        System.out.println("Programiści przypisani do projektu " + nazwa + ":");
        if (przypisanyProgramista != null) {
            System.out.println("- " + przypisanyProgramista.getName() + " " + przypisanyProgramista.getSurname());
        } else {
            System.out.println("Brak przypisanego programisty.");
        }
        for (Zadanie zadanie : zadania) {
            List<Programista> programisci = (List<Programista>) zadanie.getPrzypisanyProgramista();
            for (Programista programista : programisci) {
                System.out.println("- " + programista.getName() + " " + programista.getSurname() + " (zadanie: " + zadanie.getOpis() + ")");
            }
        }
    }

    // Gettery i settery
    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Date getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(Date dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public Date getDataZakonczenia() {
        return dataZakonczenia;
    }

    public void setDataZakonczenia(Date dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
    }

    public List<Zadanie> getZadania() {
        return zadania;
    }

    public void setZadania(List<Zadanie> zadania) {
        this.zadania = zadania;
    }

    public Zespol getZespol() {
        return zespol;
    }

    public void setZespol(Zespol zespol) {
        this.zespol = zespol;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Metoda zwracająca status projektu
    public String getStatus() {
        return status;
    }

    public void setID(int projectID) {
        this.idProjektu = projectID;
    }

    public int getProjectID() {
        return idProjektu;
    }
}
