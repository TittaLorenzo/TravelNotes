package it.unimib.travelnotes.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.roomdb.relations.UtenteConViaggi;

@Dao
public interface UtenteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long nuovoUtente(Utente utente);

    @Update
    public void aggiornaUtente(Utente utente);

    @Delete
    public void cancellaUtente(Utente utente);

    @Query("SELECT * FROM elenco_utenti WHERE utenteId = :utenteId")
    public Utente findUtenteById(String utenteId);

    @Query("SELECT * FROM elenco_utenti WHERE utenteId = :utenteId")
    public UtenteConViaggi getUtenteConViaggi(String utenteId);
}
