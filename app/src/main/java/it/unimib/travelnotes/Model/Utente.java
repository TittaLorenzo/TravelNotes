package it.unimib.travelnotes.Model;
import java.util.ArrayList;
import java.util.Date;

public class Utente {

    private String nome;
    private String cognome;

    //TODO: private String email; da capire come gestire eamil e psw e da inserire nel costruttore
    //private String psw;

    private Date nascita;
    //liste
    private ArrayList<Viaggio> listaViaggi = new ArrayList<Viaggio>();


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getNascita() {
        return nascita;
    }

    public void setNascita(Date nascita) {
        this.nascita = nascita;
    }

    public ArrayList<Viaggio> getListaViaggi() {
        return listaViaggi;
    }

    public void addListaViaggi(Viaggio nuovoViaggio) {
        this.listaViaggi.add(nuovoViaggio);
    }

    public void setViaggioIndex(int index, Viaggio modificaViaggio) {
        this.listaViaggi.set(index, modificaViaggio);
    }

    public void removeViaggio(int index) {
        this.listaViaggi.remove(index);
    }

    public void clearViaggio() {
        this.listaViaggi.clear();
    }


    public Utente(String nomeI, String cognomeI, Date nascitaI){
        this.nome= nomeI;
        this.cognome = cognomeI;
        this.nascita = nascitaI;
    }
}
