package it.unimib.travelnotes.ui.newactivityevent;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.response.AttivitaResponse;
import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class NewActivityEventViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<AttivitaResponse> mAttivitaLiveData;

    private long attivitaId;
    private long viaggioId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public MutableLiveData<AttivitaResponse> getmAttivitaLiveData() {
        return mAttivitaLiveData;
    }

    public void setmAttivitaLiveData(MutableLiveData<AttivitaResponse> mAttivitaLiveData) {
        this.mAttivitaLiveData = mAttivitaLiveData;
    }

    public long getAttivitaId() {
        return attivitaId;
    }

    public void setAttivitaId(long attivitaId) {
        this.attivitaId = attivitaId;
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

    public NewActivityEventViewModel (Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
    }

    public MutableLiveData<AttivitaResponse> getAttivita() {
        if (mAttivitaLiveData == null) {
            // mAttivitaLiveData = new MutableLiveData<AttivitaResponse>();
            fetchAttivitaViewModel();
        } else {
            mAttivitaLiveData.getValue().setError(false);
        }
        return mAttivitaLiveData;
    }

    private void fetchAttivitaViewModel() {
        mAttivitaLiveData = mITravelRepository.fetchAttivita(attivitaId, false);
    }

}
