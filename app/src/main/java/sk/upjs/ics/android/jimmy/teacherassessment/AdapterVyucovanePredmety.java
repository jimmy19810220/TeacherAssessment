package sk.upjs.ics.android.jimmy.teacherassessment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;

/**
 * Created by jimmy on 20.03.2018.
 */

public class AdapterVyucovanePredmety extends BaseAdapter {

    private List<Predmet> vyucovanePredmetyList;

    public AdapterVyucovanePredmety(List<Predmet> vyucovanePredmetyList) {
        this.vyucovanePredmetyList = vyucovanePredmetyList;
    }

    @Override
    public int getCount() {
        return vyucovanePredmetyList.size();
    }

    @Override
    public Object getItem(int position) {
        return vyucovanePredmetyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {							                                            // ak mame view, treba ho pouzit (zrecyklovat), inak musime vytvorit nanovo z xml suboru
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_vyucovany_predmet, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.textViewSkratka = (TextView) convertView.findViewById(R.id.textViewSkratka);
            holder.textViewNazov = (TextView) convertView.findViewById(R.id.textViewNazovPredmetu);
            convertView.setTag(holder);
        }

//        TextView textView = (TextView) convertView.findViewById(R.id.textViewNazovPredmetu);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.textViewSkratka.setText(vyucovanePredmetyList.get(position).getSkratka() + "");
        holder.textViewNazov.setText(vyucovanePredmetyList.get(position).getNazov() + "");


//        adjustItemView(index, view);
        return convertView;

    }

    private static class ViewHolder {
        TextView textViewSkratka;
        TextView textViewNazov;
        TextView textViewSemesterPredmetu;
    }


}
