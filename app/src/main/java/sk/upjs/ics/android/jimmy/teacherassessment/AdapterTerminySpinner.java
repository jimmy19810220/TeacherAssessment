package sk.upjs.ics.android.jimmy.teacherassessment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Termin;

class AdapterTerminySpinner extends BaseAdapter implements SpinnerAdapter {

    private List<Termin> terminySkusokList;

    AdapterTerminySpinner(List<Termin> terminySkusokList) {
        this.terminySkusokList = terminySkusokList;
    }

    int getIndexOfTermin(int idTermin) {
        for (int i = 0; i < terminySkusokList.size(); i++) {
            if (terminySkusokList.get(i).getId() == idTermin) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return terminySkusokList.size();
    }

    @Override
    public Termin getItem(int index) {
        return terminySkusokList.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {		// parent je buduci rodic nasho view vramci celeho ListView
        if (view == null) {							// ak mame view, treba ho pouzit (zrecyklovat), inak musime vytvorit nanovo z xml suboru
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.list_item_termin_skusky, parent, false);
        }

        TextView textViewTermin = (TextView) view.findViewById(R.id.textViewTerminSkusky);
        textViewTermin.setText(terminySkusokList.get(index).getNazov() + " - " + terminySkusokList.get(index).getDatum());

        return view;
    }

    @Override
    public View getDropDownView(int index, View view, ViewGroup parent) {
        return getView(index, view, parent);
    }

}