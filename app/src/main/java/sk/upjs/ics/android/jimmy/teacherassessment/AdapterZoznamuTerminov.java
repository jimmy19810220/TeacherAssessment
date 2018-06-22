package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Termin;

/**
 * Created by jimmy on 20.03.2018.
 */

public class AdapterZoznamuTerminov extends BaseAdapter {

    private List<Termin> terminyPredmetuList;

    AdapterZoznamuTerminov(List<Termin> terminyPredmetuList) {
        this.terminyPredmetuList = terminyPredmetuList;
    }

    void setNewData(List<Termin> data) {
        this.terminyPredmetuList = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.terminyPredmetuList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.terminyPredmetuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {							                                            // ak mame view, treba ho pouzit (zrecyklovat), inak musime vytvorit nanovo z xml suboru
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_termin_predmetu, parent, false);

            ViewHolder holder = new ViewHolder();
            //TODO: ked budem mat vlastny listItem, tak to odkomentujem
            holder.textViewNazovTerminuPredmetu = (TextView) convertView.findViewById(R.id.textViewNazovTerminuPredmetu);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.textViewNazovTerminuPredmetu.setText(terminyPredmetuList.get(position).getNazov() + " - " + terminyPredmetuList.get(position).getDatum());
//        holder.textViewNazovTerminuPredmetu.setText(terminyPredmetuList.get(position).getIdPredmet() + " - " + terminyPredmetuList.get(position).getDatum());
//        holder.textViewZnamka.setText(studentiPredmetuList.get(position).getZnamka());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewNazovTerminuPredmetu;
    }

}
