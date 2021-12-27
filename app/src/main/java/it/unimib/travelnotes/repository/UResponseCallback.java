package it.unimib.travelnotes.repository;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public interface UResponseCallback {
    void onResponse(Utente utente, long lastUpdate);
    void onFaliure(String errorMessage);
}
