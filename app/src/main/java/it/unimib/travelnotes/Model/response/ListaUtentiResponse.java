package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaUtentiResponse {
    private String status;
    private int totalResults;
    private List<Utente> elencoUtenti;
    private Viaggio viaggio;
    private boolean isError;
    private boolean isLoading;


    public ListaUtentiResponse(String status, int totalResults, List<Utente> elencoUtenti, Viaggio viaggio, boolean isLoading) {
        this.status = status;
        this.totalResults = totalResults;
        this.elencoUtenti = elencoUtenti;
        this.viaggio = viaggio;
        this.isLoading = isLoading;
    }
    public ListaUtentiResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
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

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
