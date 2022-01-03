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

    private long viaggioId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public MutableLiveData<ListaUtentiResponse> getmListaUtentiLiveData() {
        return mListaUtentiLiveData;
    }

    public void setmListaUtentiLiveData(MutableLiveData<ListaUtentiResponse> mListaUtentiLiveData) {
        this.mListaUtentiLiveData = mListaUtentiLiveData;
    }

    public long getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(long viaggioId) {
        this.viaggioId = viaggioId;
    }

    public int getCurrentResults() {
        return currentResults;
    }

    public void setCurrentResults(int currentResults) {
        this.currentResults = currentResults;
    }

    public int getTotalResult() {
        return totalResult;
    }

    public void setTotalResult(int totalResult) {
        this.totalResult = totalResult;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public GruppoViaggioViewModel(Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
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

}
