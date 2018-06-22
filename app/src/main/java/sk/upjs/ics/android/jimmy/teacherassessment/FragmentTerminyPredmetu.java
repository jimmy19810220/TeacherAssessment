package sk.upjs.ics.android.jimmy.teacherassessment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;
import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.Termin;
import sk.upjs.ics.android.jimmy.teacherassessment.database.TerminDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;

/**
 * Created by jimmy on 31.03.2018.
 */

public class FragmentTerminyPredmetu extends Fragment
            implements LoaderManager.LoaderCallbacks<Termin> {


    private static final String BUNDLE_KEY_TERMINY_LIST = "terminyList";
    private static final String BUNDLE_KEY_SPINNER_PREDMETY = "spinnerPredmety";
    private static final String STATE_SPINNER_POZICIA = "spinnerPozicia";


    private static ListView listViewTerminyPredmetu;
    private TextView textViewEmptyList;
    private Spinner spinnerPredmety;
    private List<Predmet> predmetyList;
    private static List<Termin> terminyPredmetuList;
    private static Predmet predmet;

    private Integer idPredmet;
    private static ActivityNavigationDrawer myActivity;

    public static final int INSERT  = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int SELECT_ALL = 5;
    private int mode;

    int currentSpinnerPosition = 0;


    @Nullable
    @Override
    public final Context getContext() {
        return super.getContext();
    }

    /**
     * premenna, v ktorej uchovavame informaciu o tom, ci si chceme ponechat idTermin kvoli nastaveniu spinnera na spravnu polozku(termin)
     */
//    private boolean keepSelectedTermin;

    private static final String STATE_ID_PREDMET = "idPredmet";
    protected static final String STATE_SKRATKA_PREDMET = "skratkaPredmet";
    private static final String STATE_FORCE_REFRESH_STUDENTI= "forceRefreshStudenti";
    private ActionBar actionBar;
//    private static final String STATE_KEEP_SELECTED_TERMIN = "keepSelectedTermin";


    @Override
    public void onCreate(Bundle data) {
        super.onCreate(data);

        myActivity = (ActivityNavigationDrawer) getActivity();

        if (data != null) {
            idPredmet = data.getInt(STATE_ID_PREDMET);
        }

        actionBar = (myActivity).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(R.string.terminy_predmetov);


//        if (myActivity.getSupportLoaderManager().getLoader(0) != null) {
//              myActivity.getSupportLoaderManager().initLoader(0, null, this);
//        }

//        new PredmetAsyncTask().execute();


    }

    @Override
    public void onSaveInstanceState(Bundle data) {
        super.onSaveInstanceState(data);

        data.putInt(STATE_ID_PREDMET, idPredmet);
        data.putInt(STATE_SPINNER_POZICIA, spinnerPredmety.getSelectedItemPosition());
        data.putSerializable(BUNDLE_KEY_SPINNER_PREDMETY, new ArrayList<>(predmetyList));
        data.putSerializable(BUNDLE_KEY_TERMINY_LIST, new ArrayList<>(terminyPredmetuList));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_terminy_predmetu, container, false);
        spinnerPredmety = rootView.findViewById(R.id.spinnerPredmety);
        final Bundle savedBundle = savedInstanceState;
        spinnerPredmety.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = savedBundle;

                if (predmetyList != null && predmetyList.size() > 0) {
                    idPredmet = predmetyList.get(position).getId();
                    predmet = predmetyList.get(position);
                }

                if (bundle == null || bundle.isEmpty()) {
                    resetLayoutTerminy();
                } else {
                    savedBundle.clear();
                    bundle.clear();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewTerminyPredmetu = rootView.findViewById(R.id.listViewTerminyPredmetu);
        listViewTerminyPredmetu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickTermin(position);
            }
        });

        listViewTerminyPredmetu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final Termin termin = (Termin) listViewTerminyPredmetu.getAdapter().getItem(position);

                Utils.showConfirmationDialog(getString(R.string.delete_termin_really),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //TODO : delete
                                new TerminAsyncTask(DELETE, getContext(), termin, termin.getIdPredmet()).execute();
//                                terminyDAO.delete(terminSkusky);
                                // priznak aby sa uloha neukladala po navrate z detail aktivity
//                                ignoreSaveOnFinish = true;
//                                ((AdapterZoznamuTerminov) listViewTerminyPredmetu.getAdapter()).notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        },
                        null,
                        myActivity);

                return true;
            }
        });

        textViewEmptyList = (TextView) rootView.findViewById(R.id.textViewEmptyListTerminyPredmetu);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

        FloatingActionButton fabPridatTermin = rootView.findViewById(R.id.fabTerminyPredmetu);
        fabPridatTermin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(myActivity.getFragmentManager(), "datePicker");

            }
        });



        return rootView;
    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true); // Indicate that this fragment would like to influence the set of actions in the action bar.

        // autorefresh
        if (savedInstanceState == null) {
            resetLayout();
        }
        else {
            predmetyList = (ArrayList) savedInstanceState.getSerializable(BUNDLE_KEY_SPINNER_PREDMETY);
            spinnerPredmety.setAdapter(new AdapterPredmetySpinner(predmetyList));
            spinnerPredmety.setSelection(savedInstanceState.getInt(STATE_SPINNER_POZICIA));
            terminyPredmetuList = (ArrayList) savedInstanceState.getSerializable(BUNDLE_KEY_TERMINY_LIST);
            listViewTerminyPredmetu.setAdapter(new AdapterZoznamuTerminov(terminyPredmetuList));
        }

    }

    protected void resetLayout() {

        //select * from predmety + naplnenie spinnera predmetmi
        new PredmetAsyncTask().execute();

        //select * from terminy
//        new TerminAsyncTask(SELECT_ALL, getActivity(), null, idPredmet).execute();


//        if (predmetyList == null) {
//            spinnerPredmety.setVisibility(View.GONE);
//            listViewTerminyPredmetu.setVisibility(View.GONE);
//            textViewEmptyList.setVisibility(View.GONE);
//        } else {
//            setUpLayout();
//        }
    }

    private void resetLayoutTerminy() {

        if (terminyPredmetuList == null) {
            listViewTerminyPredmetu.setVisibility(View.GONE);
        } else {
            setUpLayoutTerminy();
        }

        if (idPredmet != null) {
            //select * from terminy + naplnenie terminyList
            new TerminAsyncTask(SELECT_ALL, getContext(), null, idPredmet).execute();
        }

    }

    private void setUpLayout() {
        spinnerPredmety.setAdapter(new AdapterPredmetySpinner(predmetyList));
        spinnerPredmety.setVisibility(View.VISIBLE);

        new TerminAsyncTask(SELECT_ALL, getContext(), null, idPredmet);


        if (predmetyList.size() > 0) {
            textViewEmptyList.setVisibility(View.GONE);
        } else {
            textViewEmptyList.setVisibility(View.VISIBLE);
        }
    }

    private static void setUpLayoutTerminy() {
        listViewTerminyPredmetu.setAdapter(new AdapterZoznamuTerminov(terminyPredmetuList));
        listViewTerminyPredmetu.setVisibility(View.VISIBLE);
    }



    public void onClickTermin(int indexStudentaVZozname) {
//        Utils.showDialogPreVyberZnamkyStudenta(indexStudentaVZozname, getContext(),
//                studentiTerminuList, listViewStudentiTerminu);
    }


    protected void setUpActionBar() {
//        getActivity().setActionBar((Toolbar) findViewById(R.id.toolbar));
//        this.actionBar = getActivity().getActionBar();
//        if (this.actionBar == null) {
//            throw new IllegalArgumentException("ActionBar is missing!");
//        }
    }

    public void setActionBarElevationEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (enabled) {
                this.actionBar.setElevation(this.getResources().getDimension(R.dimen.elevation));
            } else {
                this.actionBar.setElevation(0.0F);
            }
        }

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private Termin termin;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            int rok = year;
            int mesiac = month + 1;
            int den = day;

            termin = new Termin();
            termin.setDatum(den + "." + mesiac + "." + rok);
            if (predmet != null)
                termin.setNazov(predmet.getNazov());
            termin.setIdPredmet(predmet.getId());

//            terminyDAO.saveOrUpdate(terminSkusky);
//            ((AdapterZoznamuTerminov) listViewTerminyPredmetu.getAdapter()).setNewData(terminyDAO.getTerminyList());

            //INSERT DO DB
            new TerminAsyncTask(INSERT, getActivity(), termin, termin.getIdPredmet()).execute();


            //TODO: refreshnut layout po zadani terminu
//            ((AdapterZoznamuTerminov) listViewTerminyPredmetu.getAdapter()).setNewData(terminyDAO.getTerminyList());

        }
    }




//================================================================================================




    public class PredmetAsyncTask extends AsyncTask<Void, Void, List<Predmet>> {

        private PredmetDao predmetDao;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            predmetDao = UcitelDatabase
                    .getInstance(myActivity)
                    .getPredmetDao();
        }

        @Override
        protected List<Predmet> doInBackground(Void... voids) {
            return predmetDao.getAllPredmety();
        }

        protected void onPostExecute(List<Predmet> predmetyOutList) {
            predmetyList = predmetyOutList;
//            spinnerPredmety.setAdapter(new AdapterPredmetySpinner(predmetyList));
            setUpLayout();
        }

    }


    public static class TerminAsyncTaskLoader extends AsyncTaskLoader<Termin> {

        private int mode;
        private TerminDao terminDao;
        private Termin termin;
        private Integer terminId;
        private String datum;
        private String nazov;
        private Long datumCas;
        private Integer idPredmet;

        public TerminAsyncTaskLoader(@NonNull Context context, TerminDao terminDao,
                                      Bundle bundle) {
            super(context);
            if (bundle != null) {
                mode = bundle.getInt("mode");
                terminId = bundle.getInt("terminId");
                datum = bundle.getString("datum");
                datumCas = bundle.getLong("datumCas");
                nazov = bundle.getString("nazov");
                idPredmet = bundle.getInt("idPredmet");
            }
            this.terminDao = terminDao;
        }

        //vykonava sa v hlavnom vlakne pred spustenim vedlajsieho(nastavenia atd.)
        @Override
        protected void onStartLoading() {

            switch (mode) {
                case INSERT:

                    termin = new Termin();
                    termin.setIdPredmet(idPredmet);
                    termin.setDatum(datum);
                    termin.setDatumCas(datumCas);
                    termin.setNazov(nazov);

                    break;

                default:
                    break;

            }

            if (isStarted())
                forceLoad();
        }

        @Nullable
        @Override
        public Termin loadInBackground() {

            long idTerminInserted = -1;

            switch (mode) {

                case INSERT:

                    idTerminInserted = terminDao.insert(termin);
                    break;

                case UPDATE:

                    termin = terminDao.getTerminById(terminId);
                    if (termin != null) {
                        termin.setDatum(datum);
                        termin.setDatumCas(datumCas);
                        termin.setNazov(nazov);

                        terminDao.update(termin);
                    }

                    break;

                case DELETE:
                    terminDao.delete(termin);
                    return null;

                default:
                    break;
            }

            return null;

        }
    }




    @NonNull
    @Override
    public Loader<Termin> onCreateLoader(int id, @Nullable Bundle args) {

        TerminDao terminDao = UcitelDatabase
                .getInstance(getContext())
                .getTerminDao();

        return null;
    }

    //tu pridu data z loadInBackground  z asyncTaskLoader
    @Override
    public void onLoadFinished(@NonNull Loader<Termin> loader, Termin data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Termin> loader) {

    }




    private static class TerminAsyncTask extends AsyncTask<Void, Void, List<Termin>> {

        private int mode;
        private Context context;
        private Termin termin;
        private TerminDao terminDao;
        private Integer idPredmet;

        public TerminAsyncTask(int mode, Context context, Termin termin, Integer idPredmet) {
            this.mode = mode;
            this.context = context;
            this.termin = termin;
            this.idPredmet = idPredmet;
        }

        @Override
        protected void onPreExecute() {
            terminDao = UcitelDatabase
                    .getInstance(context)
                    .getTerminDao();
        }

        @Override
        protected List<Termin> doInBackground(Void... params) {

            if (termin != null) {

                if (mode == INSERT) {
                    long idTermin = terminDao.insert(termin);
                }

                if (mode == DELETE) {
                    int pocetZmazanychTerminov = terminDao.delete(termin);
                    Log.d("HAHA", "pocet zmazanych terminov: " + pocetZmazanychTerminov);
                }

            }

            List<Termin> allTerminy =  terminDao.getAllTerminy();

            if (idPredmet != null) {
                return terminDao.getTerminyByPredmet(idPredmet);
            }
            else {
                return terminDao.getAllTerminy();
            }
        }

        @Override
        protected void onPostExecute(List<Termin> terminyOutList) {
            terminyPredmetuList = terminyOutList;
            //naplnime list terminov
            setUpLayoutTerminy();
//                listViewTerminyPredmetu.setAdapter(new AdapterZoznamuTerminov(context, terminyPredmetuList));
//            ((AdapterZoznamuTerminov)listViewTerminyPredmetu.getAdapter()).setNewData(terminyPredmetuList);
        }
    }


    //nechceme vidiet settings
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }


}
