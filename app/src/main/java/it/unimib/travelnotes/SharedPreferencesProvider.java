package it.unimib.travelnotes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesProvider {

    private final Application mApplication;
    private final SharedPreferences sharedPref;

    public SharedPreferencesProvider(Application application) {
        this.mApplication = application;
        sharedPref = mApplication.getSharedPreferences(mApplication.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
    }

    public String getSharedUserId() {
        return sharedPref.getString(mApplication.getString(R.string.shared_userid_key), null);
    }

    public void setSharedUserId(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mApplication.getString(R.string.shared_userid_key), userId);
        editor.apply();
    }

    public String getSharedUserEmail() {
        return sharedPref.getString(mApplication.getString(R.string.shared_useremail_key), null);
    }

    public void setSharedUserEmail(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mApplication.getString(R.string.shared_useremail_key), email);
        editor.apply();
    }

    public String getSelectedViaggioId() {
        return sharedPref.getString(mApplication.getString(R.string.shared_viaggio_key), null);
    }

    public void setSelectedViaggioId(String viaggioId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mApplication.getString(R.string.shared_viaggio_key), viaggioId);
        editor.apply();
    }
}
