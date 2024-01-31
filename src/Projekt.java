

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Projekt {
    private String nazwa;
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

    public void dodajZadanie(Zadanie zadanie) {
        this.zadania.add(zadanie);
    }
    public void usunZadanie(Zadanie zadanie) {
        this.zadania.remove(zadanie);
    }

    public void dodajProgramisteDoZadania(Programista programista, Zadanie zadanie) {
        zadanie.setPrzypisanyProgramista(programista);
    }

    public void usunProgramisteZadania(Programista programista, Zadanie zadanie) {
        zadanie.usunProgramiste(programista);
    }
    public void wyswietlZadania() {
        System.out.println("Zadania w projekcie " + nazwa + ":");
        for (Zadanie zadanie : zadania) {
            System.out.println("- " + zadanie.getOpis());
        }
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
    public void usunProgramisteZadania(String imie, String nazwisko) {
        // Iterujemy przez listę zadań w poszukiwaniu danego programisty
        for (Zadanie zadanie : this.zadania) {
            Programista programista = zadanie.getPrzypisanyProgramista();
            if (programista != null && programista.getName().equals(imie) && programista.getSurname().equals(nazwisko)) {
                zadanie.setPrzypisanyProgramista(null); // Usunięcie programisty z zadania
                System.out.println("Usunięto programistę " + imie + " " + nazwisko + " z zadania.");
                return; // Przerwij pętlę, gdy programista zostanie usunięty
            }
        }
        // Komunikat jeśli programista nie był przypisany do żadnego zadania w projekcie
        System.out.println("Podany programista nie jest przypisany do żadnego zadania w tym projekcie.");
    }
    public void dodajProgramisteDoZadania(Programista programista) {
        // Przyjmujemy, że istnieje metoda dodająca nowe zadanie do projektu, np. dodajZadanie(Zadanie zadanie)
        // W tej metodzie będziemy dodawać nowe zadanie i przypisywać do niego programistę

        // Tworzymy nowe zadanie
        Zadanie noweZadanie = new Zadanie("Nowe zadanie", this); // Załóżmy, że nazwa zadania to "Nowe zadanie"

        // Przypisujemy programistę do nowego zadania
        noweZadanie.setPrzypisanyProgramista(programista);

        // Dodajemy nowe zadanie do listy zadań projektu
        this.dodajZadanie(noweZadanie);

        System.out.println("Dodano nowe zadanie dla programisty " + programista.getName() + " " + programista.getSurname() + ".");
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
}
