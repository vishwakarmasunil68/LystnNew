package com.app.lystn.fragment.playlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.app.lystn.R;
import com.app.lystn.adapter.ViewPagerAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayListFragment extends FragmentController {

    @BindView(R.id.tv_frag_1)
    TextView tv_frag_1;
    @BindView(R.id.view_frag_1)
    View view_frag_1;
    @BindView(R.id.tv_frag_2)
    TextView tv_frag_2;
    @BindView(R.id.view_frag_2)
    View view_frag_2;
    @BindView(R.id.tv_frag_3)
    TextView tv_frag_3;
    @BindView(R.id.view_frag_3)
    View view_frag_3;
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    List<TextView> textViews=new ArrayList<>();
    List<View> views=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_play_list, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViews.add(tv_frag_1);
        textViews.add(tv_frag_2);
        textViews.add(tv_frag_3);


        views.add(view_frag_1);
        views.add(view_frag_2);
        views.add(view_frag_3);


        setupViewPager(viewPager);
        makeTabSelected(0);

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new PlayListAllAudioFragment(), "Monitor");
        adapter.addFrag(new PlayListDetailFragment(), "Monitor");
        adapter.addFrag(new RecommendListFragment(), "Monitor");

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                makeTabSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void makeTabSelected(int position) {

        for(int i=0;i<views.size();i++){
            views.get(i).setVisibility(View.GONE);
        }
        views.get(position).setVisibility(View.VISIBLE);

        for(int i=0;i<textViews.size();i++){
            textViews.get(i).setTextSize(14);
        }
        textViews.get(position).setTextSize(16);
    }


}
