package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;
import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;


/**
 * Created by jimmy on 14.03.2018.
 */

public class FragmentVyucovanePredmety extends Fragment {

    private static final String BUNDLE_KEY = "predmety";

    private ListView listViewPredmety;
    private TextView textViewEmptyList;

    VyucovanePredmetyDAO vyucovanePredmetyDAO = VyucovanePredmetyDAO.INSTANCE;

    public static final String PREDMET_ID_EXTRA = "predmetId";


    private ActivityNavigationDrawer myActivity;

//    private VyucovanePredmety vyucovanePredmety;
    private List<Predmet> vyucovanePredmetyList = new ArrayList<>();

    @Override
    public void onCreate(Bundle data) {
        super.onCreate(data);

        myActivity = (ActivityNavigationDrawer) getActivity();
//        vyucovanePredmety = vyucovanePredmetyDAO.getVyucovanePredmety();
//        vyucovanePredmetyList = vyucovanePredmetyDAO.getVyucovanePredmetyList();

        //vykona sa "select * from predmet" a zaroven sa naplni "vyucovanePredmetyList"
        new PredmetAsyncTask().execute();

        getActivity().setTitle(R.string.vyucovane_predmety);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.vyucovane_predmety, container, false);

        listViewPredmety = (ListView) rootView.findViewById(R.id.listViewPredmety);
        listViewPredmety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getContext(), "Klik na predmet", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(myActivity, ActivityZoznamStudentovPredmetu.class);
                Predmet p = ((Predmet) ((AdapterVyucovanePredmety) listViewPredmety.getAdapter()).getItem(position));
                intent.putExtra(ActivityZoznamStudentovPredmetu.ID_PREDMET, p.getId());
                intent.putExtra(ActivityZoznamStudentovPredmetu.SKRATKA_PREDMET, p.getSkratka());
                //TODO:
//                intent.putExtra(ActivityZoznamStudentovPredmetu.EXTRA_PARAM_TERMIN_POSITION, position);
//                intent.putExtra(ActivityZoznamStudentovPredmetu.EXTRA_PARAM_PREDMET_POSITION, FragmentTerminyPredmetu.this.position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        listViewPredmety.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return onLongClickStudent(position);
            }
        });


//        String[] predmetyPole = new String[]{"Programovanie", "Procesné modelovanie", "Testovanie a verifikácia programov"};

//        listViewPredmety.setAdapter(new AdapterTerminy(getContext(), new ArrayList<Termin>(0)));               // pripravime zoznamu adapter a podhodime mu len pomocne pole
        textViewEmptyList = rootView.findViewById(R.id.textViewEmptyListVyucovanePredmety);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        FloatingActionButton fab2 = rootView.findViewById(R.id.fabPredmety);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent intent = new Intent(myActivity, ActivityPredmetDetail.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });


        return rootView;

    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        myActivity.getMyActionBar().setTitle(getString(R.string.title_activity_hodnotenia));
//        myActivity.getMyActionBar().setSubtitle(null);
//        myActivity.setActionBarElevationEnabled(false);

//        setHasOptionsMenu(true);                                    // Indicate that this fragment would like to influence the set of actions in the action bar.

//        registerReceiver(new ListenerReceiverVyucovanePredmety(), new IntentFilter(ServiceAisVyucovanePredmety.ACTION + "." + myActivity.getClass().getName()));

//        resetLayout();

        //TODO:
        if (savedInstanceState == null) {
//            Log.d("savedInstance", " savedInstance is null ");
//            vyucovanePredmetyList = vyucovanePredmetyDAO.getVyucovanePredmetyList();
        }
        else {
            vyucovanePredmetyList =  (ArrayList<Predmet>) savedInstanceState.getSerializable(BUNDLE_KEY);
//            Log.d("studentPRedmetuLIst", " studentiPredmetu velkost:  " + studentiPredmetuList.size());
        }


    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(BUNDLE_KEY, new ArrayList<>(vyucovanePredmetyList));
    }


    public boolean onLongClickStudent(int indexPredmetuVZozname) {
        Intent intent = new Intent(myActivity, ActivityPredmetDetail.class);
//        Predmet predmet = vyucovanePredmetyDAO.getVyucovanePredmetyList().get(indexPredmetuVZozname);
        Predmet predmet = (Predmet) ((AdapterVyucovanePredmety) listViewPredmety.getAdapter()).getItem(indexPredmetuVZozname);
        intent.putExtra(PREDMET_ID_EXTRA, predmet.getId());
        startActivity(intent);

        return true;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onResume() {
        super.onResume();
        resetLayout();
    }

    protected void resetLayout() {
        //select * from predmet + naplnenie "vyucovanePredmetyList"
        new PredmetAsyncTask().execute();
    }

    private void setUpLayout() {
        listViewPredmety.setAdapter(new AdapterVyucovanePredmety(vyucovanePredmetyList));
        listViewPredmety.setVisibility(View.VISIBLE);

        if (vyucovanePredmetyList.size() > 0) {
            textViewEmptyList.setVisibility(View.GONE);
        } else {
            textViewEmptyList.setVisibility(View.VISIBLE);
        }
    }


    public class PredmetAsyncTask extends AsyncTask<Void, Void, List<Predmet>> {

        @Override
        protected List<Predmet> doInBackground(Void... voids) {

            PredmetDao predmetDao = UcitelDatabase
                    .getInstance(myActivity)
                    .getPredmetDao();

            return predmetDao.getAllPredmety();

        }

        protected void onPostExecute(List<Predmet> predmetyList) {
            vyucovanePredmetyList = predmetyList;
            setUpLayout();
        }

    }



}
