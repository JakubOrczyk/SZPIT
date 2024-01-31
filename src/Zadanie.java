

import java.util.Date;

public class Zadanie {
    private String opis;
    private boolean wykonane;
    private Programista przypisanyProgramista;
    private Projekt projekt;

    public Zadanie(String opis, Projekt projekt) {
        this.opis = opis;
        this.projekt = projekt;
        this.wykonane = false;
    }

    public void usunProgramiste(Programista programista) {
        if (this.przypisanyProgramista != null && this.przypisanyProgramista.equals(programista)) {
            this.przypisanyProgramista = null;
            System.out.println("Usunięto programistę z zadania.");
        } else {
            System.out.println("Podany programista nie jest przypisany do tego zadania.");
        }
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public boolean isWykonane() {
        return wykonane;
    }

    public void setWykonane(boolean wykonane) {
        this.wykonane = wykonane;
    }

    public Programista getPrzypisanyProgramista() {
        return przypisanyProgramista;
    }

    public void setPrzypisanyProgramista(Programista przypisanyProgramista) {
        this.przypisanyProgramista = przypisanyProgramista;
    }

    public String getNazwaProjektu() {
        return projekt.getNazwa();
    }

    public void setProjekt(Projekt projekt) {
        this.projekt = projekt;
    }

    public Projekt getProjekt() {
        return projekt;
    }
}
