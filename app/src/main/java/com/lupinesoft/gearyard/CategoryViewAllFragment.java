package com.lupinesoft.gearyard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CategoryViewAllFragment extends Fragment {
    ImageView backArrow, btnTv, btnLaptop;
    Fragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_category_view_all, container, false);
        backArrow = v.findViewById(R.id.cateViewAll_backButton_id);
        btnTv = v.findViewById(R.id.ctg_btn_tv_id);
        btnLaptop = v.findViewById(R.id.ctg_btn_laptop_id);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new HomeFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right,
                                R.anim.enter_right_to_left, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        btnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new tvFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations( R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        btnLaptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new laptopFragment();
                getFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations( R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                R.anim.enter_left_to_right, R.anim.exit_right_to_left)
                        .replace(R.id.fragmentId, fragment)
                        .commit();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
