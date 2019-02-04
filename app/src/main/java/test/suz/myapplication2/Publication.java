/*
 * Copyright (c) 2018.
 * Sergey Suzanskyi
 */

package test.suz.myapplication2;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKAttachments;


import org.json.JSONException;

import java.util.Random;

public final class Publication extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final int girlsInUse = 500;
    int start_pos = 0;
    int buttonCounter = 0;

    long publish_date = System.currentTimeMillis() / 1000L;
    final int groupId = -159059779;
    int _age_from = 16;
    int _age_to = 25;
    int _user_popular = 0;
    boolean _user_online = false;

    Random r = new Random();

    VKRequest users_search = null;
    VKResponse f_people = null;

    ImageView[] imageViews = new ImageView[4];

    String[] photoUrls = new String[4];


    VKAttachments attachments = new VKAttachments();

    Button btnNext, btnPost;
    Woman beautiful;

    int girlId;
    String fName, lName;

    private AdView mAdView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        r = null;
        users_search = null;
        f_people = null;
        imageViews = null;
        photoUrls = null;
        attachments = null;
        btnNext = btnPost = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        imageViews[0] = findViewById(R.id.img_1);
        imageViews[1] = findViewById(R.id.img_2);
        imageViews[2] = findViewById(R.id.img_3);
        imageViews[3] = findViewById(R.id.img_4);

        btnNext = findViewById(R.id.btn_no);
        btnPost = findViewById(R.id.btn_yes);
        // String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());

        BeforeStartCheck();


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void BeforeStartCheck() {
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(Publication.this, VKScope.WALL, VKScope.NOHTTPS, VKScope.FRIENDS);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пожалуйста, авторизуйтесь!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            UserSearch();
            TargetDraw();

            ButtonOnClickListener();
            ImageOnClickListener();

        }
    }

    public void GetCountDelayPosts() {


        VKRequest count = VKApi.wall().get(VKParameters.from(
                VKApiConst.OWNER_ID, groupId,
                "filter", "postponed",
                VKApiConst.COUNT, 1,
                VKApiConst.ACCESS_TOKEN, VKAccessToken.currentToken()
        ));

        count.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    buttonCounter = (int) response.json.getJSONObject("response").get("count");
                    btnPost.setText("Post [" + buttonCounter + "]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void ImageOnClickListener() {

        imageViews[0].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setVisibility(View.GONE);
                attachments.set(0, null);
                Toast.makeText(getApplicationContext(), "Photo has been deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        imageViews[1].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setVisibility(View.GONE);
                attachments.set(1, null);

                Toast.makeText(getApplicationContext(), "Photo has been deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        imageViews[2].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setVisibility(View.GONE);
                attachments.set(2, null);
                Toast.makeText(getApplicationContext(), "Photo has been deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        imageViews[3].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setVisibility(View.GONE);
                attachments.set(3, null);
                Toast.makeText(getApplicationContext(), "Photo has been deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }
    public void RemoveNullAttachment()
    {
        for ( VKAttachments.VKApiAttachment attach:attachments
             ) {
            if(attach==null)
            {
                attachments.remove(attach);
            }
        }
    }

    public void TargetDraw() {

        for (ImageView imageView : imageViews) {
          //  imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(android.R.color.transparent);
        }
        attachments.clear();
        if (beautiful != null && start_pos < girlsInUse - 1) {
            beautiful.Show(imageViews);
        } else {
            start_pos = 0;
            beautiful = new Woman(f_people, start_pos, imageViews, true);
        }
        photoUrls = beautiful.url_photo604;
        fName = beautiful.getFirst_name();
        lName = beautiful.getLast_name();
        girlId = beautiful.getId();
        attachments = beautiful.getVkAttachments();
        start_pos++;
        beautiful = new Woman(f_people, start_pos, imageViews, false);


    }

    public boolean isUserAdmin() {
        final int[] admins = {25291090, 57211825};
        for (int admin : admins) {
            if (admin == Integer.parseInt(VKAccessToken.currentToken().userId)) return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {

                _age_from = Integer.parseInt(data.getStringExtra("Age_from"));
                _age_to = Integer.parseInt(data.getStringExtra("Age_to"));

                if (_age_from > _age_to) {
                    _age_from = _age_from + _age_to;
                    _age_to = _age_from - _age_to;
                    _age_from = _age_from - _age_to;
                }

                _user_online = data.getBooleanExtra("Online", true);
                _user_popular = data.getByteExtra("Popular", (byte) 0);
                start_pos = 0;
                UserSearch();
            }

        }
        VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался

            }

            @Override
            public void onError(VKError error) {

            }
        });
    }

    public void UserSearch() {

        users_search = VKApi.users().search(VKParameters.from(
                VKApiConst.SEX, 1,
                VKApiConst.AGE_FROM, _age_from,
                VKApiConst.AGE_TO, _age_to,
                VKApiConst.HAS_PHOTO, 1,
                VKApiConst.SORT, _user_popular,
                VKApiConst.COUNT, girlsInUse,
                VKApiConst.BIRTH_DAY, r.nextInt(28)
                )

        );

        users_search.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                f_people = response;
                TargetDraw();
            }
        });
    }


    public void ButtonOnClickListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ImageView img:imageViews) {
                    img.setVisibility(View.GONE);
                };

                Snackbar.make(v, " Model: " + beautiful.getFirst_name() + " " + beautiful.getLast_name() + "  <3", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();


                TargetDraw();
            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PostProcess().execute();
            }
        });
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_manage:
                Intent search_intent = new Intent(this, SearchSettings.class);
                startActivityForResult(search_intent, 1);
                break;
            case R.id.nav_exit:
                VKSdk.logout();
                finish();
                break;
            case R.id.nav_share:
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String textToSend = "Смотри какие красивые девушки в сообществе vk.com/ny_vay";
                intent.putExtra(Intent.EXTRA_TEXT, textToSend);
                try {
                    startActivity(Intent.createChooser(intent, "Описание действия"));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Some error", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class PostProcess extends AsyncTask<Void, Integer, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            RemoveNullAttachment();
            GetCountDelayPosts();
            publish_date += 1800;
            VKRequest post;

            if (isUserAdmin()) {
                post = VKApi.wall().post(VKParameters.from(
                        VKApiConst.PUBLISH_DATE, publish_date,
                        VKApiConst.OWNER_ID, groupId,
                        VKApiConst.FROM_GROUP, 0,
                        VKApiConst.MESSAGE, "Model: @id" + girlId + "(" + fName + " " + lName + ") <3 \n ___________ \n  Ну Вау. ",
                        VKApiConst.ATTACHMENTS, attachments
                ));
            } else
                post = VKApi.wall().post(VKParameters.from(
                        VKApiConst.OWNER_ID, groupId,
                        VKApiConst.FROM_GROUP, 0,
                        VKApiConst.MESSAGE, "Model: @id" + girlId + "(" + fName + " " + lName + ") <3 \n ___________ \n  Ну Вау. ",
                        VKApiConst.ATTACHMENTS, attachments
                ));

            post.executeWithListener(new VKRequest.VKRequestListener()

            {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Добавлено в очередь!", Toast.LENGTH_LONG);
                    toast.show();
                    TargetDraw();
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                    publish_date += 3600;
                    new PostProcess().execute(); //
                }
            });
            return null;
        }

    }
}

