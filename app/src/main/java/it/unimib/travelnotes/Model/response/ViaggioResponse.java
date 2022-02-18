package it.unimib.travelnotes.Model.response;

import java.util.List;

import it.unimib.travelnotes.Model.Viaggio;

public class ViaggioResponse {
    private Viaggio viaggio;
    private boolean isError;

    public ViaggioResponse(Viaggio viaggio) {
        this.viaggio = viaggio;
    }
    public ViaggioResponse() {
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
