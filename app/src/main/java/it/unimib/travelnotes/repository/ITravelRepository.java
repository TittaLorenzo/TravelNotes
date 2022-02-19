package it.unimib.travelnotes.repository;

import androidx.lifecycle.MutableLiveData;



import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public interface ITravelRepository {

    //Fetch
    MutableLiveData<ListaAttivitaResponse> fetchListaAttivita(String viaggioId);

    MutableLiveData<ListaUtentiResponse> fetchGruppoViaggio(String viaggioId);

    MutableLiveData<ListaViaggiResponse> fetchListaViaggi(String userId);

    MutableLiveData<ViaggioResponse> fetchViaggio(String viaggioId);

    void loadUtente(String utenteId);


    // Push
    void pushNuovoViaggio(Viaggio viaggio, boolean esiste);

    void pushNuovaAttivita(Attivita attivita, boolean esiste);

    void pushAggiungiAlGruppo(String email, String viaggioId);

    void pushNuovoUtente(Utente utente);


    //Delete
    void deleteViaggio(String viaggioId);

    void deleteViaggioUtente(String viaggioId, String utenteId);

    void deleteAttivita(String attivitaId, String viaggioId);

    void deleteUtente(String utenteId);

    void rimuoviDalGruppo(String viaggioId, String utenteId);

    void deleteAllLocal();

}
