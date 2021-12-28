package it.unimib.travelnotes.Model;
import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity (tableName = "elenco_attivita")
public class Attivita {

    @PrimaryKey(autoGenerate = true)
    private long attivitaId;

    @ColumnInfo(name = "id_viaggio")
    private long idViaggio;

    private Date dataInizio;
    private Date dataFine;
    private String posizione;
    private String nome;
    private String descrizione;

    public long getAttivitaId() {
        return attivitaId;
    }

    public void setAttivitaId(long attivitaId) {
        this.attivitaId = attivitaId;
    }

    public long getIdViaggio() {
        return idViaggio;
    }

    public void setIdViaggio(long idViaggio) {
        this.idViaggio = idViaggio;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataI) {
        this.dataInizio = dataI;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataF) {
        this.dataFine = dataF;
    }

    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Attivita() {}

    public Attivita(String nomeI, String descrizioneI,String posizione, Date di, Date df, long IDu){
        this.nome= nomeI;
        this.descrizione = descrizioneI;
        this.posizione = posizione;
        this.dataInizio = di;
        this.dataFine = df;
        this.idViaggio = IDu;
    }

    public Attivita(String nomeI, String descrizioneI){
        this.nome= nomeI;
        this.descrizione = descrizioneI;

    }
}