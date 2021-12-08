package it.unimib.travelnotes.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.Model.Viaggio;

@Database(entities = {Utente.class, Viaggio.class, Attivita.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class TravelDatabase extends RoomDatabase {

    public abstract UtenteDao getUtenteDao();
    public abstract ViaggiDao getViaggioDao();
    public abstract AttivitaDao getAttivitaDao();

    private static volatile TravelDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static TravelDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (TravelDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            TravelDatabase.class,
                            "database-travel.db"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }
}
