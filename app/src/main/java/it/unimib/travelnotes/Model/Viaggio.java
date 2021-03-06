package it.unimib.travelnotes.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity (tableName = "elenco_viaggi")
public class Viaggio {

    @PrimaryKey
    @NonNull
    private String viaggioId;

    private Date dataAndata;
    private Date dataRitorno;
    private String partenzaAndata;
    private String destinazioneAndata;
    private String partenzaRitorno;
    private String destinazioneRitorno;
    private double durataAndata;
    private double durataRitorno;

    @NonNull
    public String getViaggioId() {
        return viaggioId;
    }

    public void setViaggioId(@NonNull String viaggioId) {
        this.viaggioId = viaggioId;
    }

    public Date getDataAndata() {
        return dataAndata;
    }

    public void setDataAndata(Date dataAndata) {
        this.dataAndata = dataAndata;
    }

    public Date getDataRitorno() {
        return dataRitorno;
    }

    public void setDataRitorno(Date dataRitorno) {
        this.dataRitorno = dataRitorno;
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

    public Viaggio() {}

    public Viaggio(Date dataAndata, Date dataRitorno, String pa, String da, String pr, String dr, double durA, double durR){
        this.dataAndata = dataAndata;
        this.dataRitorno = dataRitorno;
        this.partenzaAndata = pa;
        this.destinazioneAndata = da;
        this.partenzaRitorno = pr;
        this.destinazioneRitorno = dr;
        this.durataAndata = durA;
        this.durataRitorno = durR;
    }

    /*public Viaggio(Date dataAndata, String pa, String da, double durA){
        this.dataAndata = dataAndata;
        this.partenzaAndata = pa;
        this.destinazioneAndata = da;
        this.durataAndata = durA;
    }*/

    public Viaggio(String pa, String da, String pr, String dr, double durA, double durR){
        this.partenzaAndata = pa;
        this.destinazioneAndata = da;
        this.partenzaRitorno = pr;
        this.destinazioneRitorno = dr;
        this.durataAndata = durA;
        this.durataRitorno = durR;
    }

    public Viaggio(Date dataAndata, String pa, String da, double durA){
        this.dataAndata = dataAndata;
        this.partenzaAndata = pa;
        this.destinazioneAndata = da;
        this.durataAndata = durA;

    }


}

