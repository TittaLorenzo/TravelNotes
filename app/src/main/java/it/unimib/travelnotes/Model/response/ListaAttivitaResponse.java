package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;

public class ListaAttivitaResponse {
    private List<Attivita> elencoAttivita;
    private Viaggio viaggio;
    private boolean isError;

    public ListaAttivitaResponse(List<Attivita> elencoAttivita, Viaggio viaggio) {

        this.elencoAttivita = elencoAttivita;
        this.viaggio = viaggio;
    }
    public ListaAttivitaResponse() {
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
}
