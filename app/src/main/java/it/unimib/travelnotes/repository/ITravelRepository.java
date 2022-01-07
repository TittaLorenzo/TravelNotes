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

    void pushAggiungiAlGruppo(String email, long viaggioId);

    void pushNuovoUtente(Utente utente);

    void loadUtente(String utenteId);

    MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(long viaggioId, boolean refresh);

    MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(long viaggioId, boolean refresh);

    MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String userId, boolean refresh);

    MutableLiveData<ViaggioResponse> fetchViaggio(long viaggioId, boolean refresh);

    MutableLiveData<AttivitaResponse> fetchAttivita(long attivitaId, boolean refresh);

}
