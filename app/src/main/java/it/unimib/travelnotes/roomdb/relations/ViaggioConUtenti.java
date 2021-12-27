package it.unimib.travelnotes.roomdb.relations;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

public class ViaggioConUtenti {
    @Embedded public Viaggio viaggio;
    @Relation(
            parentColumn = "viaggioId",
            entityColumn = "utenteId",
            associateBy = @Junction(ViaggioUtenteCrossRef.class)
    )
    public List<Utente> gruppo;

}
