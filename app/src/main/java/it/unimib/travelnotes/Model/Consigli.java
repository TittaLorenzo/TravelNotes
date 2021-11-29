package it.unimib.travelnotes.Model;

public class Consigli {
    private String titolo;
    private String descrizione;


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


    public Consigli(String titoloI, String descrizioneI){
        this.titolo= titoloI;
        this.descrizione = descrizioneI;
    }
}
