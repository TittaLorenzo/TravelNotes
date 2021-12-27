package it.unimib.travelnotes.repository;

import java.util.List;

import it.unimib.travelnotes.Model.Viaggio;

public interface ResponseCallback {
    void onResponse(Viaggio Viaggio, long lastUpdate);
    void onFaliure(String errorMessage);
}
