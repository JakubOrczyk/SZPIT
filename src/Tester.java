public class Tester extends Pracownik {
    private String typTestow;
    private int idZespolu;

    public Tester(String name, String surname, String email, String phone, String address, int idPracownika, String stanowisko, double pensja, String typTestow, int idZespolu) {
        super(name, surname, email, phone, address, idPracownika, stanowisko, pensja);
        this.typTestow = typTestow;
        this.idZespolu = idZespolu;
    }

    public String getTypTestow() {
        return typTestow;
    }

    public void setTypTestow(String typTestow) {
        this.typTestow = typTestow;
    }

    public int getidZespolu() {
        return idZespolu;
    }

    public void setidZespolu(int narzedziaTestowe) {
        this.idZespolu = narzedziaTestowe;
    }
}