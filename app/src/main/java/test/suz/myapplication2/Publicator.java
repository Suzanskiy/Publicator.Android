package test.suz.myapplication2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Publicator extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    long publish_date = System.currentTimeMillis() / 1000L;
    final int groupId = -159059779;
    int owner_id = 0;
    int _age_from = 18;
    int _age_to = 22;
    int _user_popular = 0;
    boolean _user_online = true;

    Random r = new Random();

    VKRequest users_search = null;
    VKResponse finded_people = null;

    JSONObject one_user = null;
    VKRequest user_photos = null;
    ImageView img1, img2, img3, img4;

    String url, url1, url2, url3 = null;
    String first_name, last_name;

    VKAttachments attachments = new VKAttachments();

    Button btnNext, btnPost;

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

        img1 = findViewById(R.id.img_1);
        img2 = findViewById(R.id.img_2);
        img3 = findViewById(R.id.img_3);
        img4 = findViewById(R.id.img_4);

        btnNext = findViewById(R.id.btn_no);
        btnPost = findViewById(R.id.btn_yes);

        BeforeStartCheck();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void BeforeStartCheck() {
        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(Publicator.this, VKScope.WALL, VKScope.NOHTTPS, VKScope.FRIENDS);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пожалуйста, авторизуйтесь!", Toast.LENGTH_LONG);
            toast.show();

        } else {
            UserSearch();
            TargetDraw(finded_people);

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
                    int count = (int) response.json.getJSONObject("response").get("count");
                    btnPost.setText("Post [" + count + "]");
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

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                dialog = new Dialog(Publicator.this);

                dialog.setContentView(R.layout.dialog_view);
                ImageView imageZoom = dialog.findViewById(R.id.Image_zoom);

                Picasso.get().load(url).into(imageZoom);

                dialog.show();
                imageZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                dialog = new Dialog(Publicator.this);

                dialog.setContentView(R.layout.dialog_view);
                ImageView imageZoom = dialog.findViewById(R.id.Image_zoom);
                Picasso.get().load(url1).into(imageZoom);

                dialog.show();
                imageZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                dialog = new Dialog(Publicator.this);

                dialog.setContentView(R.layout.dialog_view);
                ImageView imageZoom = dialog.findViewById(R.id.Image_zoom);
                Picasso.get().load(url2).into(imageZoom);

                dialog.show();
                imageZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog;
                dialog = new Dialog(Publicator.this);

                dialog.setContentView(R.layout.dialog_view);
                ImageView imageZoom = dialog.findViewById(R.id.Image_zoom);
                Picasso.get().load(url3).into(imageZoom);

                dialog.show();
                imageZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public void TargetDraw(VKResponse response) { // TODO убрать параметр VkRespnse и обращаться напрямую

        img1.setImageResource(android.R.color.transparent);
        img2.setImageResource(android.R.color.transparent);
        img3.setImageResource(android.R.color.transparent);
        img4.setImageResource(android.R.color.transparent);
        url = url1 = url2 = url3 = null;

        attachments.clear();
        final int photo_count = 4;
        GetCountDelayPosts();

        try {
            one_user = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(r.nextInt(1000));
            if (one_user != null) {
                owner_id = one_user.getInt("id");
                last_name = one_user.getString("last_name");
                first_name = one_user.getString("first_name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user_photos = new VKRequest("photos.get", VKParameters.from(
                VKApiConst.OWNER_ID, owner_id,
                VKApiConst.REV, 1,
                VKApiConst.ALBUM_ID, "profile",
                VKApiConst.COUNT, photo_count,
                VKApiConst.EXTENDED, 0

        ));
        user_photos.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject photo0; // TODO убрать лишние объекты, можно обойтись одним !!! Убрал
                try {
                    photo0 = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(0);
                    url = photo0.getString("photo_604");
                    Picasso.get().load(url).into(img1);
                    attachments.add(new VKApiPhoto(photo0));
                    //////////////////////////////////////////
                    photo0 = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(1);
                    url1 = photo0.getString("photo_604");
                    Picasso.get().load(url1).into(img2);
                    attachments.add(new VKApiPhoto(photo0));
                    //////////////////////////////////////////
                    photo0 = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(2);
                    url2 = photo0.getString("photo_604");
                    Picasso.get().load(url2).into(img3);
                    attachments.add(new VKApiPhoto(photo0));
                    //////////////////////////////////////////
                    photo0 = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(3);
                    url3 = photo0.getString("photo_604");
                    Picasso.get().load(url3).into(img4);
                    attachments.add(new VKApiPhoto(photo0));
                } catch (JSONException ignored) {
                }
            }
        });

    }

    public boolean isUserAdmin()

    {
        final int[] admins = {25291090, 57211825};

        for (int i = 0; i < admins.length; i++) {
            if (admins[i] == Integer.parseInt(VKAccessToken.currentToken().userId)) return true;
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


               /* a = a + b;
                b = a —b;
                a = a —b;
               */
                if (_age_from > _age_to) {
                    _age_from = _age_from + _age_to;
                    _age_to = _age_from - _age_to;
                    _age_from = _age_from - _age_to;
                }

                _user_online = data.getBooleanExtra("Online", true);
                _user_popular = data.getByteExtra("Popular", (byte) 0);
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
                VKApiConst.COUNT, 1000
                // рандомизировать по Birtday запроc
                )

        );

        users_search.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                finded_people = response;
            }
        });


    }

    public void ButtonOnClickListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TargetDraw(finded_people);
                Snackbar.make(v, "Загружаю следующую девушку", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostProcess();
            }
        });
    }

    public void PostProcess() {


        publish_date += 1800;
        VKRequest post;

        if (isUserAdmin()) {
            post = VKApi.wall().post(VKParameters.from(
                    VKApiConst.PUBLISH_DATE, publish_date,
                    VKApiConst.OWNER_ID, groupId,
                    VKApiConst.FROM_GROUP, 0,
                    VKApiConst.MESSAGE, "Model: @id" + owner_id + "(" + first_name + " " + last_name + ") <3 \n ___________ \n  Ну Вау. ",
                    VKApiConst.ATTACHMENTS, attachments
            ));
        } else
            post = VKApi.wall().post(VKParameters.from(
                    VKApiConst.OWNER_ID, groupId,
                    VKApiConst.FROM_GROUP, 0,
                    VKApiConst.MESSAGE, "Model: @id" + owner_id + "(" + first_name + " " + last_name + ") <3 \n ___________ \n  Ну Вау. ",
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
                TargetDraw(finded_people);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                publish_date += 3600;
                PostProcess(); //  TODO может уйти в бесконечность, исправить!!! Важно

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
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_manage:
                Intent search_intent = new Intent(this, search_settings.class);
                startActivityForResult(search_intent, 1);
                break;
            case R.id.nav_exit:
                VKSdk.logout();
                finish();
                break;
            case R.id.nav_send:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
