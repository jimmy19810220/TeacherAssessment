package sk.upjs.ics.android.jimmy.teacherassessment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;

/**
 * Created by jimmy on 20.03.2018.
 */

public enum VyucovanePredmetyDAO {

    INSTANCE;

    private VyucovanePredmety vyucovanePredmety = new VyucovanePredmety();
    private List<Predmet> predmetyVyucujucehoList = new ArrayList<>();
    private Integer idGenerator = 0;

    VyucovanePredmetyDAO() {

//        vyucovanePredmety = new VyucovanePredmety();
//        vyucovanePredmety = new VyucovanePredmety();

//        if (predmetyVyucujucehoList == null) {
//            predmetyVyucujucehoList = new ArrayList<>();

            Predmet predmet1 = new Predmet();
//            predmet1.setId(1);
            predmet1.setSkratka("DBS1a");
            predmet1.setNazov("Databázové systémy");
            predmet1.setSemester('Z');

            Predmet predmet2 = new Predmet();
//            predmet2.setId(2);
            predmet2.setSkratka("PAZ1a");
            predmet2.setNazov("Programovanie, algoritmy, zložitosť");
            predmet2.setSemester('Z');

            Predmet predmet3 = new Predmet();
//            predmet3.setId(3);
            predmet3.setSkratka("POM");
            predmet3.setNazov("Procesné modelovanie");
            predmet3.setSemester('L');

//            predmetyVyucujucehoList.add(predmet1);
//            predmetyVyucujucehoList.add(predmet2);
//            predmetyVyucujucehoList.add(predmet3);

            saveOrUpdate(predmet1);
            saveOrUpdate(predmet2);
            saveOrUpdate(predmet3);

//        }

        vyucovanePredmety.setPredmetyList(predmetyVyucujucehoList);


    }


    //TODO: vyberie sa zoznam studentov podla idcka predmetu vyucujuceho (zatial data natvrdo)
    public VyucovanePredmety getVyucovanePredmety() {
        return vyucovanePredmety;
    }

    public List<Predmet> getVyucovanePredmetyList() {
        return predmetyVyucujucehoList;
    }


    public Predmet getPredmet(Integer idPredmet) {

        if (this.predmetyVyucujucehoList != null) {
            for (Predmet predmet : this.predmetyVyucujucehoList) {
                if (predmet.getId().equals(idPredmet)) {
                    return predmet;
                }
            }
        }

        return null;
    }

    public void saveOrUpdate(Predmet predmet) {
        if (predmet.getId() == null) {
            predmet.setId(getGeneratedId());
            this.predmetyVyucujucehoList.add(predmet);
        } else {
            Iterator<Predmet> iterator = this.predmetyVyucujucehoList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                Predmet p = iterator.next();
                if (p.getId().equals(predmet.getId())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            this.predmetyVyucujucehoList.add(index, predmet);
        }
    }

    private Integer getGeneratedId() {
        idGenerator = this.predmetyVyucujucehoList.size() + 1;
        return idGenerator;
    }

    public void delete(Predmet predmet) {
        Iterator<Predmet> iterator = this.predmetyVyucujucehoList.iterator();
        while(iterator.hasNext()) {
            Predmet p = iterator.next();
            if(p.getId().equals(predmet.getId())) {
                iterator.remove();
            }
        }
//        vyucovanePredmety.setPredmetyList(predmetyVyucujucehoList);
//        this.predmetyVyucujucehoList.clear();

//        return this.predmetyVyucujucehoList;
    }

//    public List<TerminSkusky> getTerminyPredmetuList(int idPredmet) {
//
//
//        return
//
//    }







}
