package it.unimib.travelnotes.Model.response;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;

public class AttivitaResponse {
    private String status;
    private int totalResults;
    private Attivita attivita;
    private boolean isError;
    private boolean isLoading;

    public AttivitaResponse(String status, int totalResults, Attivita attivita, boolean isLoading) {
        this.status = status;
        this.totalResults = totalResults;
        this.attivita = attivita;
        this.isLoading = isLoading;
    }
    public AttivitaResponse() {
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

    public Attivita getAttivita() {
        return attivita;
    }

    public void setAttivita(Attivita attivita) {
        this.attivita = attivita;
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
