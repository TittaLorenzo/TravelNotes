package it.unimib.travelnotes.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.travelnotes.Model.Utente;

@Dao
public interface UtenteDao {

    @Insert
    public Long nuovoUtente(Utente utente);

    @Update
    public void aggiornaUtente(Utente utente);

    @Delete
    public void cancellaUtente(Utente utente);

    @Query("SELECT * FROM elenco_utenti WHERE email = :idEmailUtente")
    public Utente findUtenteById(String idEmailUtente);
}
