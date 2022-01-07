package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaAttivitaResponse {
    private String status;
    private int totalResults;
    private List<Attivita> elencoAttivita;
    private Viaggio viaggio;
    private boolean isError;
    private boolean isLoading;


    public ListaAttivitaResponse(String status, int totalResults, List<Attivita> elencoAttivita, Viaggio viaggio, boolean isLoading) {
        this.status = status;
        this.totalResults = totalResults;
        this.elencoAttivita = elencoAttivita;
        this.viaggio = viaggio;
        this.isLoading = isLoading;
    }
    public ListaAttivitaResponse() {
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

    public List<Attivita> getElencoAttivita() {
        return elencoAttivita;
    }

    public void setElencoAttivita(List<Attivita> elencoAttivita) {
        this.elencoAttivita = elencoAttivita;
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
