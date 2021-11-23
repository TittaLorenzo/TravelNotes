package com.unimib.pdm.travelnotes;
import java.util.Date;

public class Attivita {
    private String nome;
    private Date giorno;
    private String descrizione;
    /*private Date orarioInizio;
    private Date orarioFine;*/


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getGiorno() {
        return giorno;
    }

    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }


    public Attivita(String nomeI, String descrizioneI, Date giornoI){
        this.nome= nomeI;
        this.descrizione = descrizioneI;
        this.giorno = giornoI;
    }
}
