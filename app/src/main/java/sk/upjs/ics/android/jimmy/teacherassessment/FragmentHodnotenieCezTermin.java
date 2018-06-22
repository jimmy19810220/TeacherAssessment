package sk.upjs.ics.android.jimmy.teacherassessment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetStudentJoin;
import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetStudentJoinDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.Student;
import sk.upjs.ics.android.jimmy.teacherassessment.database.StudentDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.Termin;
import sk.upjs.ics.android.jimmy.teacherassessment.database.TerminDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.TerminStudentJoin;
import sk.upjs.ics.android.jimmy.teacherassessment.database.TerminStudentJoinDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;

import static sk.upjs.ics.android.jimmy.teacherassessment.ActivityZoznamStudentovPredmetu.UPDATE_ALL;

/**
 * Created by jimmy on 31.03.2018.
 */

public class FragmentHodnotenieCezTermin extends Fragment {

    private ListView listViewStudentiTerminu;
    private TextView textViewEmptyList;
    private Spinner spinnerTerminy;
    private static List<Termin> terminySkusokList;
    private static List<Student> studentiTerminuList;
    private ActivityNavigationDrawer myActivity;
    private Integer idTermin;
    private String skratkaPredmet;
    private boolean forceRefreshStudenti;
    /**
     * premenna, v ktorej uchovavame informaciu o tom, ci si chceme ponechat idTermin kvoli nastaveniu spinnera na spravnu polozku(termin)
     */
//    private boolean keepSelectedTermin;

    private static final String BUNDLE_KEY_STUDENTI = "studenti";
    private static final String BUNDLE_KEY_SPINNER_TERMINY = "spinnerTerminy";

    private static final String STATE_SPINNER_POZICIA = "spinnerPozicia";
    private static final String STATE_ID_TERMIN = "idTermin";
    protected static final String STATE_SKRATKA_PREDMET = "skratkaPredmet";
    private static final String STATE_FORCE_REFRESH_STUDENTI= "forceRefreshStudenti";
    private ActionBar actionBar;
//    private StudentPredmetu studentOutFromSpinnerDialog;
    private Student studentOutFromSpinnerDialog;
//    private static final String STATE_KEEP_SELECTED_TERMIN = "keepSelectedTermin";

    public static final int INSERT  = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    public static final int SELECT = 4;
    public static final int SELECT_ALL = 5;
    private int mode;

    private boolean bolaZmenenaZnamka;
    private int checknutaZnamkaIndex;

    private int spinnerPozicia;
    private Bundle state;


    @Override
    public void onCreate(Bundle data) {
        super.onCreate(data);

        myActivity = (ActivityNavigationDrawer) getActivity();
        actionBar = (myActivity).getSupportActionBar();

        setActionBarElevationEnabled(true);


        if (data != null) {
            spinnerPozicia = data.getInt(STATE_SPINNER_POZICIA);
            idTermin = data.getInt(STATE_ID_TERMIN);
            skratkaPredmet = data.getString(STATE_SKRATKA_PREDMET);
        }

        myActivity.setTitle(R.string.studenti_terminu);

//        actionBar.setTitle(R.string.terminy_predmetov);


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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_prihlasit_studenta_na_termin, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_prihlasit_na_termin) {
            getStudentiNeprihlaseniNaTerminPredmetu();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //metoda zaroven naplni instancnu premennu "studentOutFromSpinnerDialog"

    private void getStudentiNeprihlaseniNaTerminPredmetu() {

        Termin termin = (Termin) spinnerTerminy.getSelectedItem();

        //AsyncTask
        //z DB dotiahnuti studenti este neprihlaseni na termin predmetu
        new StudentiNeprihlaseniAsyncTask(getContext(), termin).execute();

    }

    //metoda volana zo StudentiNeprihlaseniAsyncTask
    public void showDialog(List<Student> studentiNeprihlaseniNaTermin) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(myActivity);
        View view = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        mBuilder.setTitle(R.string.adding_student_to_exam_date);
        final Spinner spinner = view.findViewById(R.id.spinnerStudenti);

        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(myActivity,
                android.R.layout.simple_spinner_item,
                studentiNeprihlaseniNaTermin);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                studentOutFromSpinnerDialog = (Student) spinner.getSelectedItem();

                new StudentAsyncTask(INSERT, getContext(), studentOutFromSpinnerDialog, idTermin).execute();

                dialog.dismiss();
            }
        });

        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mBuilder.setView(view);
        AlertDialog dialog = mBuilder.create();
        mBuilder.show();

    }

    //nechceme vidiet settings
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem settingsMenuItem = menu.findItem(R.id.action_settings);
        settingsMenuItem.setVisible(false);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onSaveInstanceState(Bundle data) {
        super.onSaveInstanceState(data);

        if (idTermin != null)
            data.putInt(STATE_ID_TERMIN, idTermin);
        data.putBoolean(STATE_FORCE_REFRESH_STUDENTI, forceRefreshStudenti);
        data.putInt(STATE_SPINNER_POZICIA, spinnerTerminy.getSelectedItemPosition());
//        data.putBoolean(STATE_KEEP_SELECTED_TERMIN, keepSelectedTermin);
        data.putSerializable(BUNDLE_KEY_SPINNER_TERMINY, new ArrayList<>(terminySkusokList));
        data.putSerializable(BUNDLE_KEY_STUDENTI, new ArrayList<>(studentiTerminuList));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_studenti_terminu, container, false);
        spinnerTerminy = rootView.findViewById(R.id.spinnerTerminy);
        final Bundle savedBundle = savedInstanceState;
        spinnerTerminy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = savedBundle;
                idTermin = terminySkusokList.get(position).getId();

                // ak mame bundle, netreba nam nastavovat comonenty - tie sa nastavia z bundle v onActivityCreate
                if (bundle == null || bundle.isEmpty()) {
                    resetLayoutStudenti();
                } else {
                    savedBundle.clear();
                    bundle.clear();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewStudentiTerminu = rootView.findViewById(R.id.listViewStudentiTerminu);
        listViewStudentiTerminu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickStudent(position);
            }
        });

        textViewEmptyList = rootView.findViewById(R.id.textViewEmptyListStudentiTerminu);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);

//        FloatingActionButton fab2 = (FloatingActionButton) rootView.findViewById(R.id.fabUlozitStudentovNaTermine);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//
//
//                //TODO: posielanie emailu - dorobit email studentovi + vediet poslat mail individualne
//                //posielanie emailu
//                Utils.sendMail((AppCompatActivity) getActivity());
//
//
//            }
//        });

        //TODO: analogicky ako na ActivityZoznamStudentovPredmetu - ulozia sa znamky studentom na klik na fab2
        FloatingActionButton fab3 = rootView.findViewById(R.id.fabUlozitStudentovNaTermine);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View viewNew = view;

                if (bolaZmenenaZnamka) {
                    Utils.showConfirmationDialog(getResources().getText(R.string.zmenena_znamka_otazka_na_ulozenie).toString(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new StudentAsyncTask(UPDATE_ALL, getContext(), null, idTermin).execute();

                                    Snackbar.make(viewNew, "Dialog saved!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            },
                            null,
                            myActivity);
                }

            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        state = savedInstanceState;

        setHasOptionsMenu(true); // Indicate that this fragment would like to influence the set of actions in the action bar.


        // autorefresh
        if (savedInstanceState == null) {
            new TerminAsyncTask(SELECT_ALL, getContext(), null, null).execute();
        }
        else {
            terminySkusokList = (ArrayList) savedInstanceState.getSerializable(BUNDLE_KEY_SPINNER_TERMINY);
            spinnerTerminy.setAdapter(new AdapterTerminySpinner(terminySkusokList));
            spinnerTerminy.setSelection(savedInstanceState.getInt(STATE_SPINNER_POZICIA));
            studentiTerminuList = (ArrayList) savedInstanceState.getSerializable(BUNDLE_KEY_STUDENTI);
            listViewStudentiTerminu.setAdapter(new AdapterZoznamuStudentov(studentiTerminuList));
        }

    }

    private void setUpSpinnerTerminy() {
        spinnerTerminy.setAdapter(new AdapterTerminySpinner(terminySkusokList));
        spinnerTerminy.setVisibility(View.VISIBLE);
    }

    private void setUpLayout() {

        setUpSpinnerTerminy();

        Integer idTermin = null;
        if (spinnerTerminy.isSelected()) {
                idTermin = ((Termin) spinnerTerminy.getSelectedItem()).getId();
        }
        else
            if (spinnerTerminy.getAdapter().getCount() > 0) {
                idTermin = ((Termin) spinnerTerminy.getAdapter().getItem(0)).getId();
            }

        new StudentAsyncTask(SELECT, getContext(), null, idTermin).execute();


        if (terminySkusokList.size() > 0) {
            textViewEmptyList.setVisibility(View.GONE);
        } else {
            textViewEmptyList.setVisibility(View.VISIBLE);
        }
    }

    private void resetLayoutStudenti() {
        if (studentiTerminuList == null) {
            listViewStudentiTerminu.setVisibility(View.GONE);
        } else {
            setUpLayoutStudenti();
        }

        if (idTermin != null) {
            new StudentAsyncTask(SELECT, getContext(), null, idTermin).execute();
        }
    }

    private void setUpLayoutStudenti() {
        listViewStudentiTerminu.setAdapter(new AdapterZoznamuStudentov(studentiTerminuList));
        listViewStudentiTerminu.setVisibility(View.VISIBLE);
    }


    public void onClickStudent(int indexStudentaVZozname) {
//        Utils.showDialogPreVyberZnamkyStudenta(indexStudentaVZozname, getContext(),
//                studentiTerminuList, listViewStudentiTerminu);
        showDialogPreVyberZnamky(indexStudentaVZozname);
    }


    private void showDialogPreVyberZnamky(final int indexStudent) {
        final CharSequence[] items = getContext().getResources().getTextArray(R.array.grades_array);

        AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
        builder.setTitle(R.string.znamka);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checknutaZnamkaIndex = i;
            }
        });
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                studentiTerminuList.get(indexStudent).setZnamka(items[checknutaZnamkaIndex].toString());

                //refreshneme zoznam studentov
                ((AdapterZoznamuStudentov) listViewStudentiTerminu.getAdapter()).notifyDataSetChanged();

                bolaZmenenaZnamka = true;
            }
        });

        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }



//====================================================================================================


    private class TerminAsyncTask extends AsyncTask<Void, Void, List<Termin>> {

        private int mode;
        private final Context context;
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
                    terminDao.delete(termin);
                }

            }

            if (idPredmet != null) {
                return terminDao.getTerminyByPredmet(idPredmet);
            }
            else {
                return terminDao.getAllTerminy();
            }
        }

        @Override
        protected void onPostExecute(List<Termin> terminyOutList) {
            terminySkusokList = terminyOutList;
            //v setUpLayout sa naplni spinner terminov
            setUpLayout();
        }
    }

    public class StudentAsyncTask extends AsyncTask<Integer, Void, List<Student>> {

        private int mode;
        private Context context;
        private Student student;
        private Integer idTermin;
        private StudentDao studentDao;
        private TerminStudentJoinDao terminStudentJoinDao;

        public StudentAsyncTask(int mode, Context context, Student student,
                                Integer idTermin) {
            this.mode = mode;
            this.context = context;
            this.student = student;
            this.idTermin = idTermin;
        }

        @Override
        protected void onPreExecute() {

            studentDao =
                    UcitelDatabase
                    .getInstance(context)
                    .getStudentDao();

            terminStudentJoinDao =
                    UcitelDatabase
                    .getInstance(context)
                    .getTerminStudentJoinDao();

        }

        @Override
        protected List<Student> doInBackground(Integer... ids) {

            if (student != null) {
                switch (mode) {

                    case INSERT:
//                        studentDao.insert(student);
                        if (idTermin != null && student != null)
                            terminStudentJoinDao.insert(new TerminStudentJoin(idTermin, student.getId()));
                        break;

                    case UPDATE:
                        studentDao.update(student);
                        break;

                    case DELETE:
                        studentDao.delete(student);
                        return null;

                    default:
                        break;
                }
            }
            if ( mode == UPDATE_ALL && bolaZmenenaZnamka) {
                    studentDao.update(studentiTerminuList.toArray(
                            new Student[studentiTerminuList.size()]));
            }

//            predmetDao.insert(new Predmet(1, "Testovanie a verifik", "TVP", 'Z'));

//            PredmetDao predmetDao = UcitelDatabase.getInstance(ActivityPredmetDetail.this).getPredmetDao();
            if (idTermin != null)
                return terminStudentJoinDao.getStudentiTerminu(idTermin);
            else
                return null;
        }

        protected void onPostExecute(List<Student> studentiZdBList) {
            studentiTerminuList = studentiZdBList;
            setUpLayoutStudenti();
            studentOutFromSpinnerDialog = null;
        }


    }


    // STUDENTI NEPRIHLASENI NA PREDMET ASYNCTASK - POUZITIE V DIALOGU
    public class StudentiNeprihlaseniAsyncTask extends AsyncTask<Void, Void, List<Student>> {

        private Context context;
        private Termin termin;
        private StudentDao studentDao;
        private TerminStudentJoinDao terminStudentJoinDao;
        private PredmetStudentJoinDao predmetStudentJoinDao;

        public StudentiNeprihlaseniAsyncTask(Context context, Termin termin) {
            this.context = context;
            this.termin = termin;
        }

        @Override
        protected void onPreExecute() {
            studentDao = UcitelDatabase.getInstance(context).getStudentDao();
            predmetStudentJoinDao = UcitelDatabase.getInstance(context).getPredmetStudentJoinDao();
            terminStudentJoinDao = UcitelDatabase.getInstance(context).getTerminStudentJoinDao();
        }

        @Override
        protected List<Student> doInBackground(Void... voids) {

            List<PredmetStudentJoin> allPredmetyStudentov = predmetStudentJoinDao.getAllPredmetyStudentov();
            List<TerminStudentJoin> allTerminyStudentov = terminStudentJoinDao.getAllTerminyStudentov();

            return studentDao.
                    getStudentiPredmetuNeprihlaseniNaTermin(termin.getIdPredmet(), termin.getId());
        }

        protected void onPostExecute(List<Student> studentiZdBList) {
            showDialog(studentiZdBList);

            if (studentOutFromSpinnerDialog != null) {
                //INSERT idStudenta a idTerminu do tabulky termin_student_join
                new StudentAsyncTask(INSERT, getContext(), studentOutFromSpinnerDialog,
                        termin.getId()).execute();
            }

//            studentiTerminuList = studentiZdBList;
//            setUpLayoutStudenti();
        }

    }


}
