package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Student;

/**
 * Created by jimmy on 20.03.2018.
 */

public class AdapterZoznamuStudentov extends BaseAdapter {

    private List<Student> studentiPredmetuList = new ArrayList<>();

    AdapterZoznamuStudentov(List<Student> studentiPredmetuList) {
        this.studentiPredmetuList = studentiPredmetuList;
    }

    void setNewData(List<Student> data) {
        this.studentiPredmetuList = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (studentiPredmetuList != null)
            return this.studentiPredmetuList.size();
        else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return this.studentiPredmetuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {							                                            // ak mame view, treba ho pouzit (zrecyklovat), inak musime vytvorit nanovo z xml suboru
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_student_predmetu, parent, false);

            ViewHolder holder = new ViewHolder();
            //TODO: ked budem mat vlastny listItem, tak to odkomentujem
            holder.textViewPlneMenoStudenta = (TextView) convertView.findViewById(R.id.textViewPlneMenoStudentaPredmetu);
            holder.textViewZnamka = (TextView) convertView.findViewById(R.id.textViewZnamka);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.textViewPlneMenoStudenta.setText(studentiPredmetuList.get(position).getMeno() + " " + studentiPredmetuList.get(position).getPriezvisko());
        holder.textViewZnamka.setText(studentiPredmetuList.get(position).getZnamka());

        return convertView;
    }

    private static class ViewHolder {
        TextView textViewPlneMenoStudenta;
        TextView textViewZnamka;
    }

}
