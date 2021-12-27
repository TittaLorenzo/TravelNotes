package it.unimib.travelnotes.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.roomdb.relations.ViaggioConUtenti;
import it.unimib.travelnotes.roomdb.relations.ViaggioUtenteCrossRef;

@Dao
public interface ViaggiDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long nuovoViaggio(Viaggio viaggio);

    @Update
    public void aggiornaViaggio(Viaggio viaggio);

    @Delete
    public void cancellaViaggio(Viaggio viaggio);

    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :idViaggio")
    public Viaggio findViaggioById(Long idViaggio);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public Long insertViaggioUtenteCrossRef(ViaggioUtenteCrossRef crossRef);

    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :idViaggio")
    public List<ViaggioConUtenti> getViaggioConUtenti(Long idViaggio);
}
