package it.unimib.travelnotes.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public interface ITravelRepository {

    void fetchViaggio(Long viaggioId, long lastUpdate);

    void pushViaggio(Viaggio viaggio);

    void fetchAttivita(Long attivita, long lastUpdate);

    void pushAttivita(Attivita attivita);

    void fetchUtente(Long utenteId, long lastUpdate);

    void pushUtente(Utente utente);

}
