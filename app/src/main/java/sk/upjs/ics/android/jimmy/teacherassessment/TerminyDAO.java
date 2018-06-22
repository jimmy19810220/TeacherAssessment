package sk.upjs.ics.android.jimmy.teacherassessment;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public enum TerminyDAO {
    INSTANCE;

    private List<TerminSkusky> terminyList = new ArrayList<TerminSkusky>();
    private String kodJazyk = "SK";
    private Integer idGenerated;

    TerminyDAO() {

        List<TerminSkusky> terminySkusokList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        cal.add( Calendar.DAY_OF_YEAR, 5);

        terminySkusokList = new ArrayList<>();

        //Termin 1
        TerminSkusky termin1 = new TerminSkusky();
        termin1.setId(1);
        termin1.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin1.setCas("08:00");
        termin1.setDatumcas(cal.getTimeInMillis());
        termin1.setNazov("Databázové systémy");
        termin1.setSkratkaPredmetu("DBS1a");
        termin1.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        //Termin 2
        cal.add(Calendar.DAY_OF_YEAR, 7);

        TerminSkusky termin2 = new TerminSkusky();
        termin2.setId(2);
        termin2.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin2.setCas("10:00");
        termin2.setDatumcas(cal.getTimeInMillis());
        termin2.setNazov("Databázové systémy");
        termin2.setSkratkaPredmetu("DBS1a");
        termin2.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        TerminSkusky termin3 = new TerminSkusky();
        termin3.setId(3);
        termin3.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin3.setCas("09:00");
        termin3.setDatumcas(cal.getTimeInMillis());
        termin3.setNazov("Procesne modelovanie");
        termin3.setSkratkaPredmetu("PMO");
        termin3.setSkratkaPredmetuPlna("UINF/PMO/16");


        saveOrUpdate(termin1);
        saveOrUpdate(termin2);
        saveOrUpdate(termin3);



    };


    public List<TerminSkusky> getTerminySkusok(String kodJazyk) {

        List<TerminSkusky> terminySkusokList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        cal.add( Calendar.DAY_OF_YEAR, 5);

        terminySkusokList = new ArrayList<>();

        //Termin 1
        TerminSkusky termin1 = new TerminSkusky();
        termin1.setId(1);
        termin1.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin1.setCas("08:00");
        termin1.setDatumcas(cal.getTimeInMillis());
        termin1.setNazov("Databázové systémy");
        termin1.setSkratkaPredmetu("DBS1a");
        termin1.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        terminySkusokList.add(termin1);

        //Termin 2
        cal.add(Calendar.DAY_OF_YEAR, 7);

        TerminSkusky termin2 = new TerminSkusky();
        termin2.setId(2);
        termin2.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin2.setCas("10:00");
        termin2.setDatumcas(cal.getTimeInMillis());
        termin2.setNazov("Databázové systémy");
        termin2.setSkratkaPredmetu("DBS1a");
        termin2.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        terminySkusokList.add(termin2);

        TerminSkusky termin3 = new TerminSkusky();
        termin3.setId(3);
        termin3.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin3.setCas("09:00");
        termin3.setDatumcas(cal.getTimeInMillis());
        termin3.setNazov("Procesne modelovanie");
        termin3.setSkratkaPredmetu("PMO");
        termin3.setSkratkaPredmetuPlna("UINF/PMO/16");

        terminySkusokList.add(termin3);


        return terminySkusokList;
    }

    public void saveOrUpdate(TerminSkusky termin) {
        if (termin.getId() == null) {
            termin.setId(getGeneratedId());
            this.terminyList.add(termin);
        } else {
            Iterator<TerminSkusky> iterator = this.terminyList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                TerminSkusky ts = iterator.next();
                if (ts.getId().equals(termin.getId())) {
                    iterator.remove();
                    break;
                }
                index++;
            }
            this.terminyList.add(index, termin);
        }
    }

    public void delete(TerminSkusky terminSkusky) {
        Iterator<TerminSkusky> iterator = this.terminyList.iterator();
        while(iterator.hasNext()) {
            TerminSkusky ts = iterator.next();
            if(ts.getId() == terminSkusky.getId()) {
                iterator.remove();
            }
        }
    }


    private int getGeneratedId() {
        idGenerated = this.terminyList.size() + 1;
        return idGenerated;
    }


    public List<TerminSkusky> getTerminyPredmetuList(Integer idPredmet) {

        List<TerminSkusky> terminyTerminyPredmetuList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();

        cal.add( Calendar.DAY_OF_YEAR, 5);

        //Termin 1
        TerminSkusky termin1 = new TerminSkusky();
        termin1.setId(1);
        termin1.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin1.setCas("08:00");
        termin1.setDatumcas(cal.getTimeInMillis());
        termin1.setNazov("Databázové systémy");
        termin1.setSkratkaPredmetu("DBS1a");
        termin1.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        terminyTerminyPredmetuList.add(termin1);

        //Termin 2
        cal.add(Calendar.DAY_OF_YEAR, 7);

        TerminSkusky termin2 = new TerminSkusky();
        termin2.setId(2);
        termin2.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin2.setCas("10:00");
        termin2.setDatumcas(cal.getTimeInMillis());
        termin2.setNazov("Databázové systémy");
        termin2.setSkratkaPredmetu("DBS1a");
        termin2.setSkratkaPredmetuPlna("UINF/DBS1a/15");

        terminyTerminyPredmetuList.add(termin2);

        TerminSkusky termin3 = new TerminSkusky();
        termin3.setId(3);
        termin3.setDatum(Utils.getFormatedDate(new Date(cal.getTimeInMillis()), kodJazyk));
        termin3.setCas("09:00");
        termin3.setDatumcas(cal.getTimeInMillis());
        termin3.setNazov("Procesne modelovanie");
        termin3.setSkratkaPredmetu("PMO");
        termin3.setSkratkaPredmetuPlna("UINF/PMO/16");

        terminyTerminyPredmetuList.add(termin3);


        return terminyTerminyPredmetuList;
    }

    public List<TerminSkusky> getTerminyList() {
        return this.terminyList;
    }

}
