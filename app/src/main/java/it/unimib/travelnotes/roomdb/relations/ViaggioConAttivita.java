package it.unimib.travelnotes.roomdb.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;

public class ViaggioConAttivita {
    @Embedded public Viaggio viaggio;
    @Relation(
            parentColumn = "viaggioId",
            entityColumn = "viaggioId"
    )
    public List<Attivita> activities;

}
