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

    private String viaggioId;

    public String getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(String viaggioId) {
        this.viaggioId = viaggioId;
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
            mListaAttivitaLiveData = new MutableLiveData<ListaAttivitaResponse>();
        } else {
            mListaAttivitaLiveData.getValue().setError(false);
        }
        fetchListaAttivitaViewModel();

        return mListaAttivitaLiveData;
    }

    public void deleteAttivitaViewModel(String attivitaId) {
        mITravelRepository.deleteAttivita(attivitaId, viaggioId);
    }

    private void fetchListaAttivitaViewModel() {
        mListaAttivitaLiveData = mITravelRepository.fetchListaAttivita(viaggioId);
    }
}
