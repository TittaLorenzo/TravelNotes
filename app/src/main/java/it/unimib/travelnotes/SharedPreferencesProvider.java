package it.unimib.travelnotes;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesProvider {

    private final Application mApplication;
    private final SharedPreferences sharedPref;
    int i;

    public SharedPreferencesProvider(Application application) {
        this.mApplication = application;
        sharedPref = mApplication.getSharedPreferences(mApplication.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        this.i = 2;
    }

    public String getSharedUserId() {
        return sharedPref.getString(mApplication.getString(R.string.shared_userid_key), null);
    }

    public void setSharedUserId(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(mApplication.getString(R.string.shared_userid_key), userId);
        editor.apply();
    }

    public long getLastUpdateViaggio() {
        return sharedPref.getLong(mApplication.getString(R.string.shared_lastupdate_viaggio_key), 0);
    }

    public void setLastUpdateViaggio(long lastUpdate) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(mApplication.getString(R.string.shared_lastupdate_viaggio_key), lastUpdate);
        editor.apply();
    }

    public long getLastUpdateListaAttivita() {
        return sharedPref.getLong(mApplication.getString(R.string.shared_lastupdate_listaattivita_key), 0);
    }

    public void setLastUpdateListaAttivita(long lastUpdate) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(mApplication.getString(R.string.shared_lastupdate_listaattivita_key), lastUpdate);
        editor.apply();
    }

    public long getLastUpdateGruppoViaggio() {
        return sharedPref.getLong(mApplication.getString(R.string.shared_lastupdate_gruppoviaggio_key), 0);
    }

    public void setLastUpdateGruppoViaggio(long lastUpdate) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(mApplication.getString(R.string.shared_lastupdate_gruppoviaggio_key), lastUpdate);
        editor.apply();
    }

    public long getLastUpdateListaViaggi() {
        return sharedPref.getLong(mApplication.getString(R.string.shared_lastupdate_listaviaggi_key), 0);
    }

    public void setLastUpdateListaViaggi(long lastUpdate) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(mApplication.getString(R.string.shared_lastupdate_listaviaggi_key), lastUpdate);
        editor.apply();
    }


}
