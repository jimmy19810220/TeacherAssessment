package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Predmet implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String nazov;
    private String skratka;
    private char semester;

    @Ignore
    public Predmet(){

    }

    public Predmet(Integer id, String nazov, String skratka, char semester) {
        this.id = id;
        this.nazov = nazov;
        this.skratka = skratka;
        this.semester = semester;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getSkratka() {
        return skratka;
    }

    public void setSkratka(String skratka) {
        this.skratka = skratka;
    }

    public char getSemester() {
        return semester;
    }

    public void setSemester(char semester) {
        this.semester = semester;
    }
}
