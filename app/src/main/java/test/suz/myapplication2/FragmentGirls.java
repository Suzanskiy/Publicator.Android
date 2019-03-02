package test.suz.myapplication2;

import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentGirls extends android.support.v4.app.Fragment {
    ArrayList<ImageView> imageViews = new ArrayList<>();
    Woman currentWoman;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(getContext(), "home", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(getContext(), Params.start_pos+"/"+Secont_Ma.GirlsList.size(), Toast.LENGTH_SHORT).show();
                    InitialiseView();
                    return true;
            }
            return false;
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_secont__ma, container, false);


        imageViews.add((ImageView) v.findViewById(R.id.img_1));
        imageViews.add((ImageView) v.findViewById(R.id.img_2));
        imageViews.add((ImageView) v.findViewById(R.id.img_3));
        imageViews.add((ImageView) v.findViewById(R.id.img_4));


        BottomNavigationView navigation = (BottomNavigationView) v.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        InitialiseView();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      //  InitialiseView();
    }

    public void InitialiseView() {
        Params.start_pos++;
        for (int i = 0; i < imageViews.size(); i++)
            imageViews.get(i).setVisibility(View.GONE);

        if (!Secont_Ma.GirlsList.isEmpty()) {
            currentWoman = Secont_Ma.GirlsList.get(Params.start_pos);
            if (currentWoman.url_photo604.size() == 0) {
                InitialiseView();
            } else {
                for (int i = 0; i < currentWoman.url_photo604.size(); i++) {

                    Picasso.get().load(currentWoman.url_photo604.get(i)).into(imageViews.get(i), new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), "Профиль скрыт", Toast.LENGTH_SHORT).show();
                        }
                    });
                    imageViews.get(i).setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
