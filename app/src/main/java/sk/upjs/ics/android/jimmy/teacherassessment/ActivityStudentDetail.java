package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetStudentJoin;
import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetStudentJoinDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.Student;
import sk.upjs.ics.android.jimmy.teacherassessment.database.StudentDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;


// nacitanie dat cez AsyncTask, editacia a pridanie noveho zaznamu cez AsyncTaskLoader
public class ActivityStudentDetail extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<Student> {

    private EditText editTextStudentMeno;
    private EditText editTextStudentPriezvisko;
    private Student student;
    private boolean ignoreSaveOnFinish;

    private Integer studentId;
    private Integer predmetId;

    private EditText editTextStudentEmail;

    public static final int INSERT  = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;
    private int mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        studentId = (Integer) getIntent().getSerializableExtra(ActivityZoznamStudentovPredmetu.STUDENT_ID_EXTRA);
        predmetId = (Integer) getIntent().getSerializableExtra(ActivityZoznamStudentovPredmetu.PREDMET_ID_EXTRA);
        mode = (Integer) getIntent().getSerializableExtra(ActivityZoznamStudentovPredmetu.MODE_EXTRA);

        getSupportActionBar().setTitle(getString(R.string.pridanie_aktualizacia_studenta));

        editTextStudentMeno = findViewById(R.id.editTextStudentMeno);
        editTextStudentPriezvisko = findViewById(R.id.editTextStudentPriezvisko);
        editTextStudentEmail = findViewById(R.id.editTextStudentEmail);

        FloatingActionButton fab = findViewById(R.id.fabUlozitStudenta);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveStudent();
                Snackbar.make(v, R.string.student_saved, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(ActivityStudentDetail.this, R.string.student_saved, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });



        if (mode == INSERT) {
            // pripad ak vytvaram noveho studenta
//            studentPredmetu = new StudentPredmetu();
            student = new Student();
        } else {
            // pripad ak editujem existujuceho studenta
//            studentPredmetu = studentPredmetuDAO.getStudentPredmetu(studentId);
            //vytiahnem si studenta z DB
            new StudentAsyncTask(UPDATE).execute(studentId);
        }

//        getSupportLoaderManager().initLoader(OPERATION_SEARCH_LOADER, null, this);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
//        saveStudent();
    }

    private void saveStudent() {
        // nechceme, aby sa pridaval student s prazdnym meno a priezviskom
        if (editTextStudentMeno.getText() == null || editTextStudentMeno.getText().toString().trim().length() == 0
                || editTextStudentPriezvisko.getText() == null || editTextStudentPriezvisko.getText().toString().trim().length() ==  0) {
            return;
        }

        // v pripade ze sa odstranuje uloha a nema byt ulozena
        if (ignoreSaveOnFinish) {
            ignoreSaveOnFinish = false;
            return;
        }

        student.setMeno(editTextStudentMeno.getText().toString());
        student.setPriezvisko(editTextStudentPriezvisko.getText().toString());
        student.setPlneMeno(student.getMeno() + " " + student.getPriezvisko() );
        student.setEmail(editTextStudentEmail.getText().toString());

//        if (studentId == null)
//            new StudentAsyncTask(INSERT).execute();
//        else {
//            new StudentAsyncTask(UPDATE).execute();
//        }

        Bundle bundle = new Bundle();
        if (studentId == null) {
            bundle.putInt("mode", INSERT);
            bundle.putInt("predmetId", predmetId);
        }
        else {
            bundle.putInt("mode", UPDATE);
            bundle.putInt("studentId", studentId);
        }

        bundle.putString("studentMeno", editTextStudentMeno.getText().toString());
        bundle.putString("studentPriezvisko", editTextStudentPriezvisko.getText().toString());
        bundle.putString("studentEmail", editTextStudentEmail.getText().toString());
        getSupportLoaderManager().restartLoader(0, bundle, this);

//        studentPredmetuDAO.saveOrUpdate(studentPredmetu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_action_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //prepinanie viditelnosti poloziek menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem deleteMenuItem = menu.findItem(R.id.menu_item_delete);
        if (studentId == null) {
            deleteMenuItem.setVisible(false);
        }
        else {
            deleteMenuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_item_delete) {

            this.mode = DELETE;

            Utils.showConfirmationDialog(getString(R.string.delete_student_really),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new StudentAsyncTask(DELETE).execute();

//                            studentPredmetuDAO.delete(studentPredmetu);
                            // priznak aby sa student neukladal po navrate z detail aktivity
                            ignoreSaveOnFinish = true;
                            finish();
                        }
                    },
                    null,
                    this);

             return true;
        }

        // v tomto pripade to funguje aj bez nasledovneho kodu
        if (id == android.R.id.home) {
            finish();
            // https://developer.android.com/training/implementing-navigation/ancestral.html
            // dokumentacia odporuca pouzit tuto metodu, ktora obsahuje aj volanie finish
            // NavUtils.navigateUpFromSameTask(this);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public static class StudentAsyncTaskLoader extends AsyncTaskLoader<Student> {

        private int mode;
        private StudentDao studentDao;
        private PredmetStudentJoinDao predmetStudentJoinDao;
        private Student student;
        private Integer studentId;
        private String meno;
        private String priezvisko;
        private String email;
        private Integer predmetId;

        public StudentAsyncTaskLoader(@NonNull Context context, StudentDao studentDao,
                                      PredmetStudentJoinDao predmetStudentJoinDao,
                                      Bundle bundle) {
            super(context);
            if (bundle != null) {
                mode = bundle.getInt("mode");
                studentId = bundle.getInt("studentId");
                meno = bundle.getString("studentMeno");
                priezvisko = bundle.getString("studentPriezvisko");
                email = bundle.getString("studentEmail");
                predmetId = bundle.getInt("predmetId");
                this.predmetStudentJoinDao = predmetStudentJoinDao;

//                student = new Student();
//
//                student.setMeno(meno);
//                student.setPriezvisko(priezvisko);
//                student.setEmail(email);
            }
            this.studentDao = studentDao;
        }

        @Override
        protected void onStartLoading() {

            switch (mode) {
                case INSERT:

                    student = new Student();
                    student.setMeno(meno);
                    student.setPriezvisko(priezvisko);
                    student.setEmail(email);

                    break;


                default:
                    break;

            }

            forceLoad();
        }

        @Nullable
        @Override
        public Student loadInBackground() {

            long idStudentInserted = -1;

//            if (studentId != null && studentId > 0) { //aktualizacia alebo mazanie
//                studentDao
//            }

            switch (mode) {

                case INSERT:

                    idStudentInserted = studentDao.insert(student);
                    if (predmetId != null && predmetId > 0 &&
                            idStudentInserted > 0)
                        predmetStudentJoinDao.insert(new PredmetStudentJoin(predmetId, (int) idStudentInserted));
                    break;

                case UPDATE:

                    student = studentDao.getStudentById(studentId);
                    if (student != null) {
                        student.setMeno(meno);
                        student.setPriezvisko(priezvisko);
                        student.setEmail(email);

                        studentDao.update(student);
                    }

                    break;

                case DELETE:
                    studentDao.delete(student);
                    return null;

                default:
                    break;
            }

            if (student != null) {
//                List<Student> studentList = studentDao.getAllStudents();

//                return studentDao.getStudentById((int) idStudentInserted);
                return null;
            }
            else
                return null;

        }
    }


    @NonNull
    @Override
    public Loader<Student> onCreateLoader(int id, @Nullable Bundle args) {

        StudentDao studentDao = UcitelDatabase
                .getInstance(ActivityStudentDetail.this)
                .getStudentDao();

        PredmetStudentJoinDao predmetStudentJoinDao = null;
        if (mode == INSERT) {
            predmetStudentJoinDao = UcitelDatabase
                    .getInstance(ActivityStudentDetail.this)
                    .getPredmetStudentJoinDao();
        }


        return new StudentAsyncTaskLoader(this, studentDao, predmetStudentJoinDao, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Student> loader, Student student) {

        if (student != null) {
            Log.d("HAHA", "student s idckom: " + student.getId());
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Student> loader) {

    }




    public class StudentAsyncTask extends AsyncTask<Integer, Void, Student> {

        private int mode;

        public StudentAsyncTask(Integer mode) {
            if (mode != null && mode > 0)
                this.mode = mode;
        }

        @Override
        protected Student doInBackground(Integer... idStudent) {

            StudentDao studentDao = UcitelDatabase
                    .getInstance(ActivityStudentDetail.this)
                    .getStudentDao();

//            PredmetStudentJoinDao predmetStudentJoinDao = UcitelDatabase
//                    .getInstance(ActivityStudentDetail.this)
//                    .getPredmetStudentJoinDao();

            if (student != null) {
                switch (mode) {

                    case INSERT:
                        studentDao.insert(student);
//                        if (predmetId != null && studentId != null)
//                            predmetStudentJoinDao.insert(new PredmetStudentJoin(predmetId, studentId));
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

//            predmetDao.insert(new Predmet(1, "Testovanie a verifik", "TVP", 'Z'));

//            PredmetDao predmetDao = UcitelDatabase.getInstance(ActivityPredmetDetail.this).getPredmetDao();
            if (idStudent != null && idStudent.length > 0)
                return studentDao.getStudentById(idStudent[0]);
            else
                return null;
        }

        protected void onPostExecute(Student studentZdB) {
            if (studentZdB != null)
                student = studentZdB;
            //toto sa deje pri aktualizacii studenta
            if (student != null) {
                editTextStudentMeno.setText(student.getMeno());
                editTextStudentPriezvisko.setText(student.getPriezvisko());
                editTextStudentEmail.setText(student.getEmail());

//                new AsyncTask<>(new )

            }
        }


    }


//    public class StudentAsyncTaskLoader extends AsyncTaskLoader<List<String>> {
//
//        public StudentAsyncTaskLoader(@NonNull Context context) {
//            super(context);
//        }
//
//        @Nullable
//        @Override
//        public List<String> loadInBackground() {
//            return null;
//        }
//
//
//
//
//    }



}
