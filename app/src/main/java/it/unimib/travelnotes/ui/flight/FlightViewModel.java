package it.unimib.travelnotes.ui.flight;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.response.ViaggioResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class FlightViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<ViaggioResponse> mViaggioLiveData;

    private String viaggioId;

    private int currentResults;
    private int totalResult;
    private boolean isLoading;

    public String getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(String viaggioId) {
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

    public FlightViewModel(Application application) {
        super(application);

        mITravelRepository = new TravelRepository(application);
    }

    public MutableLiveData<ViaggioResponse> getViaggio() {
        if (mViaggioLiveData == null) {
            // mViaggioLiveData = new MutableLiveData<ViaggioResponse>();
            fetchViaggioViewModel();
        } else {
            mViaggioLiveData.getValue().setError(false);
        }
        return mViaggioLiveData;
    }

    private void fetchViaggioViewModel() {
        mViaggioLiveData = mITravelRepository.fetchViaggio(viaggioId);
    }

}
