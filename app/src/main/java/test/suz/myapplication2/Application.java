/*
 * Copyright (c) 2018.
 * Sergey Suzanskyi
 */

package test.suz.myapplication2;

import com.google.android.gms.ads.MobileAds;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

public class Application extends android.app.Application {
    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                VKSdk.logout();
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();

        VKSdk.initialize(this);
        MobileAds.initialize(this, "ca-app-pub-9435132814188187~2053004425");
    }

}
