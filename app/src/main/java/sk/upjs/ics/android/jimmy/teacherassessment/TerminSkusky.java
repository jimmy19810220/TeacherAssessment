package sk.upjs.ics.android.jimmy.teacherassessment;

import java.io.Serializable;

public class TerminSkusky implements Comparable<TerminSkusky>, Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private long datumcas;
    private String cas;
    private String datum;
    private String nazov;
    private String skratkaPredmetu;
    private String skratkaPredmetuPlna;

    public TerminSkusky() {
    }

    public int compareTo(TerminSkusky other) {
        if (this.datumcas > other.datumcas) {
            return 1;
        } else {
            return this.datumcas < other.datumcas ? -1 : 0;
        }
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof TerminSkusky) {
            return this.id == ((TerminSkusky)other).id;
        } else {
            return false;
        }
    }
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSkratkaPredmetuPlna() {
        return this.skratkaPredmetuPlna == null ? this.skratkaPredmetu : this.skratkaPredmetuPlna;
    }

    public void setSkratkaPredmetuPlna(String skratkaPredmetuPlna) {
        this.skratkaPredmetuPlna = skratkaPredmetuPlna;
    }

    public String getCas() {
        return this.cas;
    }

    public long getDatumcas() {
        return this.datumcas;
    }

    public void setDatumcas(long datumcas) {
        this.datumcas = datumcas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getDatum() {
        return this.datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }


    public String getNazov() {
        return this.nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getSkratkaPredmetu() {
        return this.skratkaPredmetu;
    }

    public void setSkratkaPredmetu(String skratkaPredmetu) {
        this.skratkaPredmetu = skratkaPredmetu;
    }
}
