package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaUtentiResponse {
    private List<Utente> elencoUtenti;
    private Viaggio viaggio;
    private boolean isError;

    public ListaUtentiResponse(List<Utente> elencoUtenti, Viaggio viaggio) {
        this.elencoUtenti = elencoUtenti;
        this.viaggio = viaggio;
    }
    public ListaUtentiResponse() {
    }

    public List<Utente> getElencoUtenti() {
        return elencoUtenti;
    }

    public void setElencoUtenti(List<Utente> elencoUtenti) {
        this.elencoUtenti = elencoUtenti;
    }

    public Viaggio getViaggio() {
        return viaggio;
    }

    public void setViaggio(Viaggio viaggio) {
        this.viaggio = viaggio;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
