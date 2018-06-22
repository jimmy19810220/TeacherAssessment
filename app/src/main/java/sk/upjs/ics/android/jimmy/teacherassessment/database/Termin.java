package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Predmet.class,
                                    parentColumns = "id",
                                    childColumns = "idPredmet",
                                    onDelete = CASCADE))
public class Termin implements Comparable<Termin>, Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private Integer idPredmet;
    private String datum;
    private long datumCas;
    private String nazov;

    @Ignore
    public Termin() {
    }

    public Termin( Integer idPredmet, String datum, long datumCas, String nazov) {
        this.idPredmet = idPredmet;
        this.datum = datum;
        this.datumCas = datumCas;
        this.nazov = nazov;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPredmet() {
        return idPredmet;
    }

    public void setIdPredmet(Integer idPredmet) {
        this.idPredmet = idPredmet;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public long getDatumCas() {
        return this.datumCas;
    }

    public void setDatumCas(long datumCas) {
        this.datumCas = datumCas;
    }

    public String getNazov() {
        return this.nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public int compareTo(Termin other) {
        if (this.datumCas > other.datumCas) {
            return 1;
        } else {
            return this.datumCas < other.datumCas ? -1 : 0;
        }
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof Termin) {
            return this.id == ((Termin)other).id;
        } else {
            return false;
        }
    }



}
