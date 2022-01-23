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
import it.unimib.travelnotes.roomdb.relations.ViaggioConUtenti;
import it.unimib.travelnotes.roomdb.relations.ViaggioUtenteCrossRef;

@Dao
public interface ViaggiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void nuovoViaggio(Viaggio viaggio);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAllViaggi(List<Viaggio> listViaggio);

    @Update
    void aggiornaViaggio(Viaggio viaggio);

    @Delete
    void cancellaViaggio(Viaggio viaggio);

    @Query("DELETE FROM elenco_viaggi WHERE viaggioId = :viaggioId")
    void deleteViaggioById(String viaggioId);

    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :viaggioId")
    Viaggio findViaggioById(String viaggioId);


    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :viaggioId")
    ViaggioConUtenti getViaggioConUtenti(String viaggioId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertViaggioUtenteCrossRef(ViaggioUtenteCrossRef crossRef);

    @Query("DELETE FROM ViaggioUtenteCrossRef WHERE viaggioId = :viaggioId AND utenteId = :utenteId")
    void togliDalGruppo(String viaggioId, String utenteId);

}
