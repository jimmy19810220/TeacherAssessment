package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import sk.upjs.ics.android.jimmy.teacherassessment.database.Predmet;
import sk.upjs.ics.android.jimmy.teacherassessment.database.PredmetDao;
import sk.upjs.ics.android.jimmy.teacherassessment.database.UcitelDatabase;

public class ActivityPredmetDetail extends AppCompatActivity {

    private EditText editTextPredmetSkratka;
    private EditText editTextPredmetNazov;
    private Predmet predmet;
    private Integer predmetId;
    private boolean ignoreSaveOnFinish;

    public static final int INSERT  = 1;
    public static final int UPDATE = 2;
    public static final int DELETE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predmet_detail);

        //ak to nie je rozumne cislo, posle mi null namiesto "-1"
        predmetId = (Integer) getIntent().getSerializableExtra(FragmentVyucovanePredmety.PREDMET_ID_EXTRA);

        getSupportActionBar().setTitle(getString(R.string.pridanie_aktualizacia_predmetu));

        editTextPredmetSkratka = findViewById(R.id.editTextPredmetSkratka);
        editTextPredmetNazov = findViewById(R.id.editTextPredmetNazov);

        if (predmetId == null) { // pripad ak vytvaram novy predmet
        //INSERT predmet
            predmet = new Predmet();
        } else {
            //UPDATE predmet
            // pripad ak editujem existujuci predmet
//            predmet = vyucovanePredmetyDAO.getPredmet(predmetId);
            //select * from predmet where id = predmetId + naplnenie instancnej premennej "predmet"
            new PredmetAsyncTask().execute(predmetId);
        }

        FloatingActionButton fab = findViewById(R.id.fabUlozitPredmet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePredmet();
                Snackbar.make(v, R.string.student_saved, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Toast.makeText(ActivityPredmetDetail.this, R.string.student_saved, Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
//        savePredmet();
    }

    private void savePredmet() {
        // nechceme, aby sa pridaval predmet s prazdnou skratkou a nazvom
        if (editTextPredmetSkratka.getText() == null || editTextPredmetSkratka.getText().toString().trim().length() == 0
                || editTextPredmetNazov.getText() == null || editTextPredmetNazov.getText().toString().trim().length() ==  0) {
            return;
        }

        // v pripade ze sa odstranuje predmet a teda nema byt ulozeny
        if (ignoreSaveOnFinish) {
            ignoreSaveOnFinish = false;
            return;
        }

        //INSERT alebo UPDATE predmetu
//        if (predmetId == null) { // INSERT
//            predmet = new Predmet();
//        }

        predmet.setSkratka(editTextPredmetSkratka.getText().toString());
        predmet.setNazov(editTextPredmetNazov.getText().toString());

        if (predmetId == null)
            new PredmetAsyncTask(INSERT).execute();
        else {
            new PredmetAsyncTask(UPDATE).execute();
        }

//        vyucovanePredmetyDAO.saveOrUpdate(predmet);

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
        if (predmetId == null) {
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

            Utils.showConfirmationDialog(getString(R.string.delete_subject_really),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new PredmetAsyncTask(DELETE).execute();

//                            vyucovanePredmetyDAO.delete(predmet);
                            // priznak aby sa predmet neukladal po navrate z detail aktivity
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




    public class PredmetAsyncTask extends AsyncTask<Integer, Void, Predmet> {

        private int mode;

        public PredmetAsyncTask(Integer... mode) {
            if (mode != null && mode.length > 0)
                this.mode = mode[0];
        }

        @Override
        protected Predmet doInBackground(Integer... idPredmet) {

            PredmetDao predmetDao = UcitelDatabase
                    .getInstance(ActivityPredmetDetail.this)
                    .getPredmetDao();

            if (predmet != null) {
                switch (mode) {

                    case INSERT:
                        predmetDao.insert(predmet);
                        break;

                    case UPDATE:
                        predmetDao.update(predmet);
                        break;

                    case DELETE:
                        predmetDao.delete(predmet);
                        return null;

                    default:
                        break;
                }
            }

//            predmetDao.insert(new Predmet(1, "Testovanie a verifik", "TVP", 'Z'));

//            PredmetDao predmetDao = UcitelDatabase.getInstance(ActivityPredmetDetail.this).getPredmetDao();
            if (idPredmet != null && idPredmet.length > 0)
                return predmetDao.getPredmetById(idPredmet[0]);
            else
                return null;
        }

        protected void onPostExecute(Predmet predmetZdB) {
            if (predmetZdB != null)
                predmet = predmetZdB;
            if (predmet != null) {
                editTextPredmetNazov.setText(predmet.getNazov());
                editTextPredmetSkratka.setText(predmet.getSkratka());
            }
        }


    }



}
