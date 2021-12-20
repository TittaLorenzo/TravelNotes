package it.unimib.travelnotes.Model;
import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;


@Entity (tableName = "elenco_viaggi",
        indices = @Index("id_utente"),
        foreignKeys = {
                @ForeignKey(
                        entity = Utente.class,
                        parentColumns = "email",
                        childColumns = "id_utente",
                        onDelete = CASCADE,
                        onUpdate = CASCADE
                )
        })
public class Viaggio {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "id_utente")
    private Long idUtente;

    private String partenzaAndata;
    private String destinazioneAndata;
    private String partenzaRitorno;
    private String destinazioneRitorno;
    private double durataAndata;
    private double durataRitorno;

    //liste
    @Ignore
    private ArrayList<Attivita> listaAttivita = new ArrayList<Attivita>();
   //private ArrayList<Note> listaNote = new ArrayList<Note>();
    //private ArrayList<Consigli> listaConsigli = new ArrayList<Consigli>();
    @Ignore
    private ArrayList<Note> listaNote = new ArrayList<Note>();
    @Ignore
    private ArrayList<Consigli> listaConsigli = new ArrayList<Consigli>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

    public String getPartenzaAndata() {
        return partenzaAndata;
    }

    public void setPartenzaAndata(String partenzaAndata) {
        this.partenzaAndata = partenzaAndata;
    }

    public String getDestinazioneAndata() {
        return destinazioneAndata;
    }

    public void setDestinazioneAndata(String destinazioneAndata) {
        this.destinazioneAndata = destinazioneAndata;
    }

    public String getPartenzaRitorno() {
        return partenzaRitorno;
    }

    public void setPartenzaRitorno(String partenzaRitorno) {
        this.partenzaRitorno = partenzaRitorno;
    }

    public String getDestinazioneRitorno() {
        return destinazioneRitorno;
    }

    public void setDestinazioneRitorno(String destinazioneRitorno) {
        this.destinazioneRitorno = destinazioneRitorno;
    }

    public double getDurataAndata() {
        return durataAndata;
    }

    public void setDurataAndata(double durataAndata) {
        this.durataAndata = durataAndata;
    }

    public double getDurataRitorno() {
        return durataRitorno;
    }

    public void setDurataRitorno(double durataRitorno) {
        this.durataRitorno = durataRitorno;
    }

    public ArrayList<Attivita> getListaAttivita() {
        return listaAttivita;
    }

    public void addListaAttivita(Attivita nuovaAttivita) {
        this.listaAttivita.add(nuovaAttivita);
    }

    public void setAttivitaIndex(int index, Attivita modificaAttivita) {
        this.listaAttivita.set(index, modificaAttivita);
    }

    public void removeAttivita(int index) {
        this.listaAttivita.remove(index);
    }

    public void clearAttivita() {
        this.listaAttivita.clear();
    }

    public ArrayList<Note> getListaNote() {
        return listaNote;
    }

    public void addListaNote(Note nuovaNota) {
        this.listaNote.add(nuovaNota);
    }

    public void setNoteIndex(int index, Note modificaNota) {
        this.listaNote.set(index, modificaNota);
    }

    public void removeNota(int index) {
        this.listaNote.remove(index);
    }

    public void clearNote() {
        this.listaNote.clear();
    }

    public ArrayList<Consigli> getListaConsigli() {
        return listaConsigli;
    }

    public void addListaConsigli(Consigli nuovoConsiglio) {
        this.listaConsigli.add(nuovoConsiglio);
    }

    public void setConsigliIndex(int index, Consigli modificaConsigli) {
        this.listaConsigli.set(index, modificaConsigli);
    }

    public void removeConsigli(int index) {
        this.listaConsigli.remove(index);
    }

    public void clearConsigli() {
        this.listaConsigli.clear();
    }


    public Viaggio() {}

    public Viaggio(String pa, String da, String pr, String dr, double durA, double durR){
        this.partenzaAndata = pa;
        this.destinazioneAndata = da;
        this.partenzaRitorno = pr;
        this.destinazioneRitorno = dr;
        this.durataAndata = durA;
        this.durataRitorno = durR;
    }


}
