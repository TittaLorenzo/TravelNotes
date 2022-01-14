package it.unimib.travelnotes.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.roomdb.relations.UtenteConViaggi;

@Dao
public interface UtenteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long nuovoUtente(Utente utente);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllUtenti(List<Utente> listaUtenti);

    @Update
    void aggiornaUtente(Utente utente);

    @Delete
    void cancellaUtente(Utente utente);

    @Query("DELETE FROM elenco_utenti WHERE utenteId = :utenteId")
    void deleteUtenteById(String utenteId);

    @Query("SELECT * FROM elenco_utenti WHERE utenteId = :utenteId")
    Utente findUtenteById(String utenteId);

    @Query("SELECT * FROM elenco_utenti WHERE utenteId = :utenteId")
    UtenteConViaggi getUtenteConViaggi(String utenteId);
}
