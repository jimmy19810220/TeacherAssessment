package sk.upjs.ics.android.jimmy.teacherassessment;

import java.io.Serializable;

public class StudentPredmetu implements Serializable {

    private Integer id;
    private String plneMeno;
    private String meno;
    private String priezvisko;
    private String email;
    private String znamka;


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
        return plneMeno;
    }
}
