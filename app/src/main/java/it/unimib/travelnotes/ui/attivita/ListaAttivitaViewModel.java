package it.unimib.travelnotes.ui.attivita;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class ListaAttivitaViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<ListaAttivitaResponse> mListaAttivitaLiveData;

    private long viaggioId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public long getVisggioId() {
        return viaggioId;
    }

    public void setVisggioId(long visggioId) {
        this.viaggioId = visggioId;
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

    public MutableLiveData<ListaAttivitaResponse> getListaAttivitaLiveData() {
        return mListaAttivitaLiveData;
    }

    public void setmListaAttivitaLiveData(MutableLiveData<ListaAttivitaResponse> mListaAttivitaLiveData) {
        this.mListaAttivitaLiveData = mListaAttivitaLiveData;
    }

    public ListaAttivitaViewModel (Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
    }

    public MutableLiveData<ListaAttivitaResponse> getlistaAttivita() {
        if (mListaAttivitaLiveData == null) {
            // mListaAttivitaLiveData = new MutableLiveData<ListaAttivitaResponse>();
            fetchListaAttivitaViewModel();
        } else {
            mListaAttivitaLiveData.getValue().setError(false);
        }
        return mListaAttivitaLiveData;
    }

    private void fetchListaAttivitaViewModel() {
        mListaAttivitaLiveData = mITravelRepository.fetchListaAttivita(viaggioId, false);
    }
}