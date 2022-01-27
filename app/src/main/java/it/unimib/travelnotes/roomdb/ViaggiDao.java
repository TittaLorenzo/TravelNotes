package it.unimib.travelnotes.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.travelnotes.Model.Viaggio;

@Dao
public interface ViaggiDao {
    @Insert
    public Long nuovoViaggio(Viaggio viaggio);

    @Update
    public void aggiornaViaggio(Viaggio viaggio);

    @Delete
    public void cancellaViaggio(Viaggio viaggio);

    //@Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :idViaggio");

    public Viaggio findViaggioById(Long idViaggio);
}
