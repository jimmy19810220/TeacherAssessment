package sk.upjs.ics.android.jimmy.teacherassessment.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity
public class Student implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String plneMeno;
    private String meno;
    private String priezvisko;
    private String email;
    private String znamka;

    @Ignore
    public Student() {
    }

    public Student(String plneMeno, String meno, String priezvisko, String email, String znamka) {
        this.plneMeno = plneMeno;
        this.meno = meno;
        this.priezvisko = priezvisko;
        this.email = email;
        this.znamka = znamka;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlneMeno() {
        return plneMeno;
    }

    public void setPlneMeno(String plneMeno) {
        this.plneMeno = plneMeno;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getPriezvisko() {
        return priezvisko;
    }

    public void setPriezvisko(String priezvisko) {
        this.priezvisko = priezvisko;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZnamka() {
        return znamka;
    }

    public void setZnamka(String znamka) {
        this.znamka = znamka;
    }

    @Override
    public String toString() {

        plneMeno = meno + " " + priezvisko;

        return plneMeno;
    }
}
