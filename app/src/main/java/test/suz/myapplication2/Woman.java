/*
 * Copyright (c) 2018.
 * Sergey Suzanskyi
 */

package test.suz.myapplication2;

import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import org.json.JSONException;
import org.json.JSONObject;

public class Woman {

    String[] url_photo604 = null;
    VKAttachments attachments = new VKAttachments();
    private int id;
    private String last_name;
    private String first_name;
    private int photo_count = 4;
    private VKRequest user_photos = null;

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

        user_photos = new VKRequest("photos.get", VKParameters.from(
                VKApiConst.OWNER_ID, id,
                VKApiConst.REV, 1,
                VKApiConst.ALBUM_ID, "profile",
                VKApiConst.COUNT, photo_count,
                VKApiConst.EXTENDED, 0
        ));
        user_photos.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject photo; // TODO убрать лишние объекты, можно обойтись одним !!! Убрал
                try {
                    photo = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(0);
                    url_photo604[0] = photo.getString("photo_604");
                    attachments.add(new VKApiPhoto(photo));
                    //////////////////////////////////////////
                    photo = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(1);
                    url_photo604[1] = photo.getString("photo_604");
                    attachments.add(new VKApiPhoto(photo));
                    //////////////////////////////////////////
                    photo = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(2);
                    url_photo604[2] = photo.getString("photo_604");
                    attachments.add(new VKApiPhoto(photo));
                    //////////////////////////////////////////
                    photo = response.json.getJSONObject("response").getJSONArray("items").getJSONObject(3);
                    url_photo604[3] = photo.getString("photo_604");
                    attachments.add(new VKApiPhoto(photo));
                } catch (JSONException ignored) {
                }
            }
        });
    }

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
