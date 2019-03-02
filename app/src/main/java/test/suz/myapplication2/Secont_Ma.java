package test.suz.myapplication2;

import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKAttachments;

import java.util.ArrayList;
import java.util.Random;

import static com.vk.sdk.VKUIHelper.getApplicationContext;

public class Secont_Ma extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Random r = new Random();

    static VKRequest users_search = null;
    public static VKResponse f_people = null;

    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secont__ma);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        BeforeStartCheck();
    }

    public void BeforeStartCheck() {
        if (!VKSdk.isLoggedIn()) {

            VKSdk.login(Secont_Ma.this, VKScope.WALL, VKScope.NOHTTPS, VKScope.FRIENDS);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пожалуйста, авторизуйтесь!", Toast.LENGTH_LONG);
            toast.show();
        } else {

            new UserSearch().execute();
            //  FragmentSettings.UserSearch();
            // UserSearch(); //TODO STATIC call from fragment

            //  TargetDraw();

            //  ButtonOnClickListener();
            //  ImageOnClickListener();

        }
    }

    public static ArrayList<Woman> GirlsList = new ArrayList<>();

    public static class FillGirlList extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
          for (int i=0; i <Params.girlsInUse;i++) {
              if(i%5==0) {
                  try {
                      Thread.sleep(2000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
              GirlsList.add(new Woman(f_people, i));
          }

            return null;
        }

    }

    public static class UserSearch extends AsyncTask<Void, Integer, Void> {

        private Random r = new Random();

        @Override
        protected Void doInBackground(Void... voids) {
            users_search = VKApi.users().search(VKParameters.from(
                    VKApiConst.SEX, 1,
                    VKApiConst.AGE_FROM, Params._age_from,
                    VKApiConst.AGE_TO, Params._age_to,
                    VKApiConst.HAS_PHOTO, 1,
                    VKApiConst.SORT, Params._user_popular,
                    VKApiConst.COUNT, Params.girlsInUse,
                    VKApiConst.BIRTH_DAY, r.nextInt(28))

            );

            users_search.executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    f_people = response;
                    new FillGirlList().execute();

                }
            });
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_secont__ma, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return new FragmentSettings();
                case 1:
                    if (GirlsList != null)
                        return new FragmentGirls();
                    else
                        Toast.makeText(getApplicationContext(), "Еще не готово", Toast.LENGTH_SHORT).show();

            }
            return new FragmentSettings(); // default
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }
}
