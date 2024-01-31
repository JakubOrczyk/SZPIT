public class Klient extends Osoba{
    private String numerTelefonu;

    public Klient(String name, String surname, String email, String phone, String address, String numerTelefonu) {
        super(name, surname, email, phone, address);
        this.numerTelefonu = numerTelefonu;
    }

    // Gettery i settery dla wszystkich pól
    public String getNumerTelefonu() {
        return numerTelefonu;
    }

    public void setNumerTelefonu(String numerTelefonu) {
        this.numerTelefonu = numerTelefonu;
    }

}
