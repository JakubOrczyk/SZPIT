import java.util.ArrayList;
import java.util.List;

public class Zespol {
    private String nazwa;
    private String liderName;
    private int iloscProgramistow;
    private List<Programista> czlonkowie;

    public Zespol(String nazwa, String liderName, int iloscProgramistow) {
        this.nazwa = nazwa;
        this.liderName = liderName;
        this.iloscProgramistow = iloscProgramistow;
        this.czlonkowie = new ArrayList<>();
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getLiderName() {
        return liderName;
    }

    public void setLiderName(String liderName) {
        this.liderName = liderName;
    }

    public int getIloscProgramistow() {
        return iloscProgramistow;
    }

    public void setIloscProgramistow(int iloscProgramistow) {
        this.iloscProgramistow = iloscProgramistow;
    }

    public List<Programista> getCzlonkowie() {
        return czlonkowie;
    }

    public void setCzlonkowie(List<Programista> czlonkowie) {
        this.czlonkowie = czlonkowie;
    }

    public void dodajProgramiste(Programista programista) {
        this.czlonkowie.add(programista);
    }
}
