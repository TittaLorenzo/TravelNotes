package it.unimib.travelnotes.repository;

import androidx.lifecycle.MutableLiveData;



import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.response.AttivitaResponse;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public interface ITravelRepository {

    void pushNuovoViaggio(Viaggio viaggio, boolean esiste);

    void pushNuovaAttivita(Attivita attivita, boolean esiste);

    void pushAggiungiAlGruppo(String email, String viaggioId);

    void pushNuovoUtente(Utente utente);

    void loadUtente(String utenteId);

    MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(String viaggioId);

    MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(String viaggioId);

    MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String userId);

    MutableLiveData<ViaggioResponse> fetchViaggio(String viaggioId);

    MutableLiveData<AttivitaResponse> fetchAttivita(String attivitaId, String viaggioId);

    void addListaViaggiListener(String utenteId);

    void addListaAttivitaListener(String viaggioId);

    void addListaUtentiListener(String viaggioId);

    void addViaggioListener(String viaggioId);

}
