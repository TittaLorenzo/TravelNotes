package it.unimib.travelnotes.ui.group;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

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

    public void createDynamicLink() {

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.travelnotes.com/?invitoviaggio=" + viaggioId))
                .setDomainUriPrefix("https://travelnotes.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("it.unimib.travelnotes").build())
                /*.setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters().Builder()
                        .setTitle("Invito per un nuovo viaggio!!")
                        .setDescription("Sei stato invitato a unirti a un nuovo viaggio su TravelNotes")
                        .build())*/
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(dynamicLinkUri.toString());
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", dynamicLinkUri.toString());
            clipboard.setPrimaryClip(clip);
        }
    }

}
