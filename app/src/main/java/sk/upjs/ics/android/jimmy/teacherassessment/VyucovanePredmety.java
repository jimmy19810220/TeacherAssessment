//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package sk.upjs.ics.android.jimmy.teacherassessment;

import java.io.Serializable;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;

public class VyucovanePredmety implements Serializable {
    private static final long serialVersionUID = 1L;
    private String akRok;
    private List<Predmet> predmetyList;

    public VyucovanePredmety() {
    }

    public String getAkRok() {
        return this.akRok;
    }

    public void setAkRok(String akRok) {
        this.akRok = akRok;
    }

    public List<Predmet> getPredmetyList() {
        return this.predmetyList;
    }

    public void setPredmetyList(List<Predmet> predmetyList) {
        this.predmetyList = predmetyList;
    }
}
