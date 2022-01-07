package it.unimib.travelnotes.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import it.unimib.travelnotes.Model.Attivita;

@Dao
public interface AttivitaDao {

    @Insert
    public Long nuovaAttivita(Attivita attivita);

    @Update
    public void aggiornaAttivita(Attivita attivita);

    @Delete
    public void cancellaAttivita(Attivita attivita);

    @Query("SELECT * FROM elenco_attivita WHERE attivitaId = :idAttivita")
    public Attivita findAttivitaById(Long idAttivita);


    //public List<Attivita> findAttivitaByIdViaggio(Long idViaggio);
}
