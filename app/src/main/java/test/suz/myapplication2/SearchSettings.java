package test.suz.myapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchSettings extends AppCompatActivity {

    Switch user_online, swDelay;
    Switch user_popular;
    EditText age_from;
    EditText age_to;
    ImageView Avatarka;
    CalendarView Cal;
    long date;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_online = findViewById(R.id.switch_online);
        user_popular = findViewById(R.id.switch_popular);
        age_from = findViewById(R.id.age_from);
        age_to = findViewById(R.id.age_to);
        Avatarka = findViewById(R.id.Avatarka);
        swDelay = findViewById(R.id.swDelay);

        getParamsFromActivity();
        AdminInfo();


    }

    public void TransferParamsToActiviy() {
        intent.putExtra("Online", user_online.isChecked());
        intent.putExtra("Age_to", age_to.getText().toString());
        intent.putExtra("Popular", user_popular.isChecked());
        intent.putExtra("Age_from", age_from.getText().toString());
      //  intent.putExtra("date", Cal.getDate() );
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void AdminInfo() {
        VKRequest admin = new VKRequest("users.get", VKParameters.from(
                VKApiConst.USER_ID, VKAccessToken.currentToken().userId,
                VKApiConst.FIELDS, "photo_200"
        ));
        admin.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject admin_info = response.json.getJSONArray("response").getJSONObject(0);
                    Picasso.get().load(admin_info.getString("photo_200")).into(Avatarka);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    public void getParamsFromActivity()// передаю на главную активити значения
    {

        user_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TransferParamsToActiviy();
            }
        });
        user_popular.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TransferParamsToActiviy();
            }
        });

        age_to.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TransferParamsToActiviy();
            }
        });
        age_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TransferParamsToActiviy();
            }
        });


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
