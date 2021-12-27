package it.unimib.travelnotes.repository;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;

public interface AResponseCallback {
    void onResponse(Attivita attivita, long lastUpdate);
    void onFaliure(String errorMessage);
}
