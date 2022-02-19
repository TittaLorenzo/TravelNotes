package it.unimib.travelnotes;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.travelnotes.Model.response.ListaViaggiResponse;
import it.unimib.travelnotes.repository.ITravelRepository;
import it.unimib.travelnotes.repository.TravelRepository;

public class TravelListViewModel extends AndroidViewModel {

    private final ITravelRepository mITravelRepository;
    private MutableLiveData<ListaViaggiResponse> mListaViaggiLiveData;

    private String utenteId;

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

    public void deleteViaggioViewModel(String viaggioId,String utenteId) {
        mITravelRepository.deleteViaggioUtente(viaggioId, utenteId);
    }

    public void delateAll() {
        mITravelRepository.deleteAllLocal();
    }
}
