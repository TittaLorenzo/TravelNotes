package it.unimib.travelnotes.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.roomdb.relations.ViaggioConAttivita;

@Dao
public interface AttivitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long nuovaAttivita(Attivita attivita);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllAttivita(List<Attivita> listaAttivita);

    @Update
    void aggiornaAttivita(Attivita attivita);

    @Delete
    void cancellaAttivita(Attivita attivita);

    @Query("DELETE FROM elenco_attivita WHERE attivitaId = :attivitaId")
    void deleteAttivitaById(String attivitaId);

    @Query("SELECT * FROM elenco_attivita WHERE attivitaId = :attivitaId")
    Attivita findAttivitaById(String attivitaId);

    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :viaggioId")
    ViaggioConAttivita getViaggioConAttivita(String viaggioId);
}
