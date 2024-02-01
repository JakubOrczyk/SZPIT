public class Pracownik extends Osoba {
    private int idPracownika;
    private String stanowisko;
    private double pensja;

    public Pracownik(String name, String surname, String email, String phone, String address, int idPracownika, String stanowisko, double pensja) {
        super(name, surname, email, phone, address);
        this.idPracownika = idPracownika;
        this.stanowisko = stanowisko;
        this.pensja = pensja;
    }

    public int getIdPracownika() {
        return idPracownika;
    }

    public void setIdPracownika(int idPracownika) {
        this.idPracownika = idPracownika;
    }

    public String getStanowisko() {
        return stanowisko;
    }

    public void setStanowisko(String stanowisko) {
        this.stanowisko = stanowisko;
    }

    public double getPensja() {
        return pensja;
    }

    public void setPensja(double pensja) {
        this.pensja = pensja;
    }
}