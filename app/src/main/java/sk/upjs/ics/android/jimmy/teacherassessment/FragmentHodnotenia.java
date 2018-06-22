package sk.upjs.ics.android.jimmy.teacherassessment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentHodnotenia extends Fragment {

    private ViewPager viewPager;
    private SlidingTabLayout tabs;

    private MenuItem menuItemNezaradene;

    private ActivityNavigationDrawer myActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hodnotenia_viewpager, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) { // vyvola sa pri swipe-nuti na nejaku stranku, POZOR nevyvola sa pri uvodnom zobrazeni view
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() { // urcuje farbu pruzku pod textom tabu
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getContext(), R.color.biela);
            }
            @Override
            public int getDividerColor(int position) {
                return ContextCompat.getColor(getContext(), R.color.biela_transparent);
            }
        });

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.myActivity = (ActivityNavigationDrawer) getActivity();

        nastavHodnotenieLayout(0);

        if (savedInstanceState != null) {                       // ak dochadza ku rekonstrukcii aktivity otocenim, tak sa pokusime obnovit dialog - nerobime autorefresh
//            obnovDialog(savedInstanceState);                    // navyse neresetujeme priznak zmenenySemester, co moze vyvolat refresh() v onResume(), ale len v pripade, ze refresh() nebezi

        } else {
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_navigation_hodnotenia_menu, menu);
        menuItemNezaradene = menu.findItem(R.id.action_neprihlaseni_na_termin);
        menuItemNezaradene.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "klik na menu v spolocnom fragmente pre hodnotenia", Toast.LENGTH_SHORT).show();
                //TODO:
//                if (runningServiceAction == null) {
//                    Intent intent = new Intent(myActivity, ActivityRozvrhNezaradene.class);
//                    intent.putExtra(ActivityRozvrhovaAkcia.PARAM_SEMESTER, rozvrh.kodSemester);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
            }
        });

        //TODO:
//        resetMenuItemNezaradene();
        super.onCreateOptionsMenu(menu, inflater);
    }



    /**
     * ked uz vieme, ze  mame data k dispozicii
     */
//    private void nastavLayout() {
//        nastavHodnotenieLayout(ActivitySettings.getDpHodnotenieSposob(myActivity));
////        nastavHodnotenieLayout(0);
////        myActivity.getActionBar().setSubtitle(rozvrh.akadRok + myActivity.getSubtitleSemester(rozvrh.kodSemester));
//        myActivity.getMyActionBar().setSubtitle("2016/2017" + " :)");
//    }

    private void nastavHodnotenieLayout(int zalozka) {
        viewPager.setAdapter(new PagerAdapterHod(getChildFragmentManager(), myActivity));
        tabs.setViewPager(viewPager);
        viewPager.setCurrentItem(zalozka);

        tabs.setVisibility(View.VISIBLE);
        viewPager.setVisibility(View.VISIBLE);
    }


    // adapter funguje na baze fragmentov, tento typ adaptera je vhodny na vopred znamy fixny staticky zoznam stranok
    private class PagerAdapterHod extends FragmentPagerAdapter {

        private String[] pageTitle;

        private PagerAdapterHod(FragmentManager fm, Context context) {
            super(fm);
            pageTitle = context.getResources().getStringArray(R.array.hodnotenie_nadpisy_zaloziek);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new FragmentHodnotenieCezTermin();
            }
            else {
                return new FragmentVyucovanePredmety();
            }

        }

        @Override
        public int getCount() {                                             // pocet stranok
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {           // akcie, ktore maju custom layout (action_nezaradene) potrebuju vlastny listener, tato metoda pre nich nefunguje
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
