package it.unimib.travelnotes.ui.group;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.response.ListaUtentiResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class GruppoViaggioViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData;

    private String viaggioId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public GruppoViaggioViewModel(Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
    }

    public MutableLiveData<ListaUtentiResponse> getmListaUtentiLiveData() {
        return mListaUtentiLiveData;
    }

    public void setmListaUtentiLiveData(MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData) {
        this.mListaUtentiLiveData = mListaUtentiLiveData;
    }

    public String getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(String viaggioId) {
        this.viaggioId = viaggioId;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public MutableLiveData<ListaUtentiResponse> getListaUtenti() {
        if (mListaUtentiLiveData == null) {
            // mListaAttivitaLiveData = new MutableLiveData<ListaAttivitaResponse>();
            fetchListaUtentiViewModel();
        } else {
            mListaUtentiLiveData.getValue().setError(false);
        }
        return mListaUtentiLiveData;
    }

    private void fetchListaUtentiViewModel() {
        mListaUtentiLiveData = mITravelRepository.fetchGruppoViaggio(viaggioId);
    }

    public void aggiungiAlGruppo(String email) {
        mITravelRepository.pushAggiungiAlGruppo(email, viaggioId);
    }

}
