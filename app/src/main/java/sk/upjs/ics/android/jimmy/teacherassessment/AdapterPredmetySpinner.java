package sk.upjs.ics.android.jimmy.teacherassessment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;

class AdapterPredmetySpinner extends BaseAdapter implements SpinnerAdapter {

    private List<Predmet> predmetyList;

    AdapterPredmetySpinner(List<Predmet> predmetyList) {
        this.predmetyList = predmetyList;
    }

    int getIndexOfTermin(int idPredmet) {
        for (int i = 0; i < predmetyList.size(); i++) {
            if (predmetyList.get(i).getId() == idPredmet) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return predmetyList.size();
    }

    @Override
    public Predmet getItem(int index) {
        return predmetyList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {		// parent je buduci rodic nasho view vramci celeho ListView
        if (view == null) {							// ak mame view, treba ho pouzit (zrecyklovat), inak musime vytvorit nanovo z xml suboru
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_item_predmet, parent, false);
        }

        TextView textViewPredmet = (TextView) view.findViewById(R.id.textViewPredmet);
        textViewPredmet.setText(predmetyList.get(index).getSkratka() + " - " + predmetyList.get(index).getNazov());

        return view;
    }

    @Override
    public View getDropDownView(int index, View view, ViewGroup parent) {
        return getView(index, view, parent);
    }

//    static class Termin implements Comparable<TerminSkusky> {
//        private String code;
//        private String name;
//
//        private CodeName(String code, String name) {
//            this.code = code;
//            this.name = name;
//        }
//
//        String getCode() {
//            return code;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        @Override
//        public int compareTo(@NonNull CodeName another) {
//            return this.name.compareTo(another.name);
//        }
//    }
}