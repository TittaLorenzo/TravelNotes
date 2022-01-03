package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaViaggiResponse {
    private String status;
    private int totalResults;
    private List<Viaggio> elencoViaggi;
    private Utente utente;
    private boolean isError;
    private boolean isLoading;


    public ListaViaggiResponse(String status, int totalResults, List<Viaggio> elencoViaggi, Utente utente, boolean isLoading) {
        this.status = status;
        this.totalResults = totalResults;
        this.elencoViaggi = elencoViaggi;
        this.utente = utente;
        this.isLoading = isLoading;
    }
    public ListaViaggiResponse() {
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

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }
}
