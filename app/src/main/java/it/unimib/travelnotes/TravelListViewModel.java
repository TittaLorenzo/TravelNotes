package it.unimib.travelnotes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.response.ListaAttivitaResponse;
import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class TravelListViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData;

    private String utenteId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public MutableLiveData<ListaViaggiResponse> getmListaViaggiLiveData() {
        return mListaViaggiLiveData;
    }

    public void setmListaViaggiLiveData(MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData) {
        this.mListaViaggiLiveData = mListaViaggiLiveData;
    }

    public String getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(String utenteId) {
        this.utenteId = utenteId;
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

    public TravelListViewModel(Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
    }

    public MutableLiveData<ListaViaggiResponse> getlistaViaggi() {
        if (mListaViaggiLiveData == null) {
            mListaViaggiLiveData = new MutableLiveData<ListaViaggiResponse>();
        } else {
            mListaViaggiLiveData.getValue().setError(false);
        }
        fetchListaViaggiViewModel();

        return mListaViaggiLiveData;
    }

    private void fetchListaViaggiViewModel() {
        mListaViaggiLiveData = mITravelRepository.fetchListaViaggi(utenteId);
    }

    public MutableLiveData<ListaViaggiResponse> getListaViaggiResponse() {
        return mListaViaggiLiveData;
    }
}
