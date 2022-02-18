package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaViaggiResponse {
    private List<Viaggio> elencoViaggi;
    private Utente utente;
    private boolean isError;

    public ListaViaggiResponse(List<Viaggio> elencoViaggi, Utente utente) {
        this.elencoViaggi = elencoViaggi;
        this.utente = utente;
    }
    public ListaViaggiResponse() {
    }

    public List<Viaggio> getElencoViaggi() {
        return elencoViaggi;
    }

    public void setElencoViaggi(List<Viaggio> elencoViaggi) {
        this.elencoViaggi = elencoViaggi;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
