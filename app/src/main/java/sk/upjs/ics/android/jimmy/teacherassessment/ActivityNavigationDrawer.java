package sk.upjs.ics.android.jimmy.teacherassessment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityNavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected ActionBar actionBar;

    TextView textViewPlneMenoUser;
    TextView textViewSkolaUser;

    public ActionBar getMyActionBar() {
        return actionBar;
    }

    protected void setUpActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        actionBar = getSupportActionBar();
        if (actionBar == null) {
            throw new IllegalArgumentException("ActionBar is missing!");
        }
    }

    public void setActionBarElevationEnabled(boolean enabled) {
        if (Build.VERSION.SDK_INT >= 21) {                                           // od verzie API 21 (5.0 Lollipop) sa aplikuje hodnota elevation
            if (enabled) {
                actionBar.setElevation(getResources().getDimension(R.dimen.elevation));
            } else {
                actionBar.setElevation(0f);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        textViewPlneMenoUser = findViewById(R.id.textViewPlneMeno);
        textViewSkolaUser = findViewById(R.id.textViewSkola);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActivityNavigationDrawer activity = this;
        final String[] userNameFakulta = new String[2];

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();


                //aktualizacia udajov usera
                View newView = getLayoutInflater().inflate(R.layout.dialog_user, null);
                final EditText editTextDialogMenoUser = newView.findViewById(R.id.editTextDialogMenoUser);
                final EditText editTextDialogFakultaUser = newView.findViewById(R.id.editTextDialogFakultaUser);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityNavigationDrawer.this);
                mBuilder.setTitle(R.string.user_info);
                mBuilder.setView(newView);

                mBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userNameFakulta[0] = editTextDialogMenoUser.getText().toString();
                        userNameFakulta[1] = editTextDialogFakultaUser.getText().toString();
                        if (userNameFakulta[0].length() > 0)
                            textViewPlneMenoUser.setText(userNameFakulta[0]);
                        if (userNameFakulta[1].length() > 0)
                            textViewSkolaUser.setText(userNameFakulta[1]);
                        dialog.dismiss();
                    }
                });

                mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = mBuilder.create();
                mBuilder.show();

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        item.setChecked(true);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_user_info) {

            Intent intent = new Intent(this, ActivityNavigationDrawer.class);
            startActivity(intent);

        } else if (id == R.id.nav_assessment) {

            fragment = new FragmentHodnotenia();

        } else if (id == R.id.nav_assessment_terminy) {

            fragment = new FragmentHodnotenieCezTermin();

        } else if (id == R.id.nav_terminy_predmetov) {

            fragment = new FragmentTerminyPredmetu();

        }

        if (fragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.contentMainActivity, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
