/*
 * Copyright (c) 2018.
 * Sergey Suzanskyi
 */

package test.suz.myapplication2;

import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Woman {

    ArrayList<String> url_photo604 = new ArrayList<>();
    VKAttachments attachments = new VKAttachments();
    private int id;
    private String last_name;
    private String first_name;
    private int photoCount = 4;


    public Woman(VKResponse response1000, int index) {

        try {
            JSONObject temp = response1000.json.getJSONObject("response").getJSONArray("items").getJSONObject(index);
            if (temp != null) {
                id = temp.getInt("id");
                last_name = temp.getString("last_name");
                first_name = temp.getString("first_name");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VKRequest userPhotos = new VKRequest("photos.get", VKParameters.from(
                VKApiConst.OWNER_ID, id,
                VKApiConst.REV, 1,
                VKApiConst.ALBUM_ID, "profile",
                VKApiConst.COUNT, photoCount,
                VKApiConst.EXTENDED, 0
        ));
        userPhotos.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject photo;
                try {

                    for (int i = 0; i < photoCount; i++) {
                        photo = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(i);
                        url_photo604.add(photo.getString("photo_604"));
                        attachments.add(new VKApiPhoto(photo));
/*
                        Picasso.get().load(url_photo604[i]).fetch(new Callback() {
                            @Override
                            public void onSuccess() {
                                if (draw)
                                    Show(imgs);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
                        */

                    }

                } catch (JSONException ignored) {
                }
            }
        });
    }

    public VKAttachments getVkAttachments() {
        return attachments;
    }
/*
    public void Show(ImageView[] imgs) {
        for (int i = 0; i < photoCount; i++) {
            imgs[i].setVisibility(View.VISIBLE);
            Picasso.get().load(url_photo604[i]).into(imgs[i]);
        }

    }
    */

    public int getId() {
        return id;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }
}
