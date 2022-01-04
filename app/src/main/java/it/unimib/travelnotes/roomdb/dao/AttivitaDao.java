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
    public long nuovaAttivita(Attivita attivita);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addAllAttivita(List<Attivita> listaAttivita);

    @Update
    public void aggiornaAttivita(Attivita attivita);

    @Delete
    public void cancellaAttivita(Attivita attivita);

    @Query("SELECT * FROM elenco_attivita WHERE attivitaId = :idAttivita")
    public Attivita findAttivitaById(long idAttivita);

    @Query("SELECT * FROM elenco_viaggi WHERE viaggioId = :viaggioId")
    public ViaggioConAttivita getViaggioConAttivita(long viaggioId);
}
