package it.unimib.travelnotes.roomdb.relations;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"viaggioId", "utenteId"})
public class ViaggioUtenteCrossRef {
    @NonNull
    public Long viaggioId;
    @NonNull
    public Long utenteId;
}
