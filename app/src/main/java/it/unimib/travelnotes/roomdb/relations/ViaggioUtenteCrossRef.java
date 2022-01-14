package it.unimib.travelnotes.roomdb.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"viaggioId", "utenteId"})
public class ViaggioUtenteCrossRef {
    @NonNull
    public String viaggioId;
    @NonNull
    public String utenteId;
}
