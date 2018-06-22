package sk.upjs.ics.android.jimmy.teacherassessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetStudentJoinDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.Student;
import sk.upjs.ics.android.jimmy.teacherassessment.database.StudentDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;

/**
 * Zoznam studentov predmetu, na ktory sme klikli v zozname predmetov na predchadzajucej aktivite
 */
public class ActivityZoznamStudentovPredmetu extends AppCompatActivity {

    public static final String ID_PREDMET = "idPredmet";
    public static final String SKRATKA_PREDMET = "skratkaPredmet";
    private static final String BUNDLE_KEY_STUDENTI = "studenti";
    public static final String STUDENT_ID_EXTRA = "studentID";
    public static final String PREDMET_ID_EXTRA = "predmetID";
    public static final String MODE_EXTRA = "mode";

    int mode = -1;
    public static final int NO_MODE  = 0;
    public static final int INSERT  = 1;
    public static final int UPDATE = 2;
    public static final int UPDATE_ALL = 4;
    public static final int DELETE = 3;

    private static Integer idPredmet;
    private String  skratkaPredmet;
    private boolean forceRefresh;
    protected ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_studenti_predmetu);

        idPredmet = (Integer) getIntent().getSerializableExtra(ID_PREDMET);
        skratkaPredmet = (String) getIntent().getSerializableExtra(SKRATKA_PREDMET);

        Log.d("idPredmet", "idPredmet: " + idPredmet + "");

        //zlty toolbar (hlavicka tabulky(listViewu))
        ((TextView) findViewById(R.id.toolbar_textVlavo)).setText(R.string.student_meno);
        ((TextView) findViewById(R.id.toolbar_textVpravo)).setText(R.string.znamka);


        //TODO: toolbar
        //toolbar
        setUpActionBar();
        setActionBarElevationEnabled(true);
        actionBar.setTitle(getString(R.string.title_activity_studenti_predmetu) + " " + skratkaPredmet);
        actionBar.setDisplayHomeAsUpEnabled(true);                                          // chceme sipku spat

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.containerStudentiPredmetu,
                    new FragmentStudentiPredmetu()).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.zoznam_studentov_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.addNewTask) {
            Intent intent = new Intent(this, ActivityStudentDetail.class);
            intent.putExtra(PREDMET_ID_EXTRA, idPredmet);
            intent.putExtra(MODE_EXTRA, INSERT);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static class FragmentStudentiPredmetu extends Fragment {

        private ActivityZoznamStudentovPredmetu myActivity;
        private List<Student> studentiList;
        private TextView textViewKodPredmetu;
        private TextView textViewNazovPredmetu;
        private ListView listViewStudenti;
        private TextView textViewEmptyList;
        private int checknutaZnamkaIndex;
        private boolean bolaZmenenaZnamka;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_studenti_predmetu, container, false);
            listViewStudenti = rootView.findViewById(R.id.listViewStudenti);

            listViewStudenti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                //INSERT student
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onClickStudent(position);
                }
            });

            listViewStudenti.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                //UPDATE or DELETE
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return onLongClickStudent(position);
                }
            });

            textViewEmptyList = rootView.findViewById(R.id.textViewEmptyListStudentiPredmetu);

            Log.d("onCreateViewFragment", "onCreateViewFragment");

            FloatingActionButton fab = rootView.findViewById(R.id.fabStudentiPredmetu);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final View viewNew = view;

                    if (bolaZmenenaZnamka) {
                        Utils.showConfirmationDialog(getResources().getText(R.string.zmenena_znamka_otazka_na_ulozenie).toString(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new StudentAsyncTask(idPredmet, UPDATE_ALL).execute();

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

            myActivity = (ActivityZoznamStudentovPredmetu) getActivity();
            setHasOptionsMenu(true);                                    // Indicate that this fragment would like to influence the set of actions in the action bar.

            //TODO:
//            new StudentAsyncTask().execute();

            if (savedInstanceState == null) {
                resetLayout();
            }
            else {
                studentiList = (ArrayList) savedInstanceState.getSerializable(BUNDLE_KEY_STUDENTI);
                listViewStudenti.setAdapter(new AdapterZoznamuStudentov(studentiList));
            }

//            resetLayout();

        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putSerializable(BUNDLE_KEY_STUDENTI, new ArrayList<>(studentiList));
        }

        protected void resetLayout() {
            //select * from predmet + naplnenie "vyucovanePredmetyList"
            new StudentAsyncTask(idPredmet, NO_MODE).execute();
        }

        private void setUpLayout() {
            listViewStudenti.setAdapter(new AdapterZoznamuStudentov(studentiList));
            listViewStudenti.setVisibility(View.VISIBLE);

            if (studentiList.size() > 0) {
                textViewEmptyList.setVisibility(View.GONE);
            } else {
                textViewEmptyList.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    myActivity.finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public void onClickStudent(int indexStudentaVZozname) {
            showDialogPreVyberZnamky(indexStudentaVZozname);
        }

        //UPDATE studenta
        public boolean onLongClickStudent(int indexStudentaVZozname) {
            Intent intent = new Intent(myActivity, ActivityStudentDetail.class);

            Student student =
                    (Student) (listViewStudenti.getAdapter()).getItem(indexStudentaVZozname);

            intent.putExtra(STUDENT_ID_EXTRA, student.getId());
            intent.putExtra(MODE_EXTRA, UPDATE);
            startActivity(intent);

            return true;
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
//                    studentiPredmetuList.get(indexStudent).setZnamka(items[checknutaZnamkaIndex].toString());
                    studentiList.get(indexStudent).setZnamka(items[checknutaZnamkaIndex].toString());

                    //refreshneme zoznam studentov
                    ((AdapterZoznamuStudentov) listViewStudenti.getAdapter()).notifyDataSetChanged();

                    bolaZmenenaZnamka = true;
                }
            });

            builder.setNegativeButton(R.string.cancel, null);
            builder.create().show();
        }


        @Override
        public void onResume() {
            super.onResume();



            resetLayout();
//            setUpLayout();
        }

        @Override
        public void onPause() {
            super.onPause();

//            if (bolaZmenenaZnamka) {
//                Utils.showConfirmationDialog(getResources().getText(R.string.zmenena_znamka_otazka_na_ulozenie).toString(),
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                new StudentAsyncTask(-1, UPDATE_ALL).execute();
//                            }
//                        },
//                null,
//                        myActivity);
//            }

        }

        public class StudentAsyncTask extends AsyncTask<Void, Void, List<Student>> {

            private Integer idPredmet;
            private StudentDao studentDao;
            private PredmetStudentJoinDao predmetStudentJoinDao;
            int mode;

            public StudentAsyncTask(Integer idPredmet, int mode) {
                this.idPredmet = idPredmet;
                this.mode = mode;
            }

            @Override
            protected void onPreExecute() {
                studentDao = UcitelDatabase
                        .getInstance(myActivity)
                        .getStudentDao();

                predmetStudentJoinDao = UcitelDatabase
                        .getInstance(myActivity)
                        .getPredmetStudentJoinDao();

                super.onPreExecute();
            }

            @Override
            protected List<Student> doInBackground(Void... voids) {

                if (mode == UPDATE_ALL && bolaZmenenaZnamka) {
                    studentDao.update(studentiList.toArray(new Student[studentiList.size()]));
                }


//                return studentDao.getAllStudents();
//                List<PredmetStudentJoin> p = predmetStudentJoinDao.getAllPredmetyStudentov();
//                List<Student> s = studentDao.getAllStudents();
                List<Student> sp = predmetStudentJoinDao.getStudentiPredmetu(idPredmet);

                return sp;
//                return studentDao.getStudentiPredmetu(idPredmet);

            }

            protected void onPostExecute(List<Student> studentiListZdB) {
                bolaZmenenaZnamka = false;
                studentiList = studentiListZdB;
//            ((AdapterVyucovanePredmety) listViewPredmety.getAdapter()).notifyDataSetChanged();
//            textViewEmptyList.setVisibility(vyucovanePredmetyList.size() > 0 ? View.GONE : View.VISIBLE);
//                listViewStudenti.setAdapter(new AdapterZoznamuStudentov(myActivity, studentiListZdB));
                setUpLayout();
            }

        }

    }

    protected void setUpActionBar() {
//        this.setSupportActionBar((Toolbar)this.findViewById(R.id.toolbar));
        this.actionBar = this.getSupportActionBar();
        if (this.actionBar == null) {
            throw new IllegalArgumentException("ActionBar is missing!");
        }
    }

    public ActionBar getMyActionBar() {
        return this.actionBar;
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
    protected void onResume() {
        super.onResume();
    }




}
