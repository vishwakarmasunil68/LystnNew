package com.app.lystn.fragment.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.search.SearchPodcastAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.home.HomeContentPOJO;

import java.util.List;

import butterknife.BindView;

public class SearchPodcastsFragment extends FragmentController {

    @BindView(R.id.rv_results)
    RecyclerView rv_results;

    List<HomeContentPOJO> homeContentPOJOS;

    public SearchPodcastsFragment(List<HomeContentPOJO> homeContentPOJOS) {
        this.homeContentPOJOS = homeContentPOJOS;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_podcasts, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        attachGenericAdapter(rv_results);

    }


    public void attachGenericAdapter(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        SearchPodcastAdapter searchResultAdapter = new SearchPodcastAdapter(getActivity(), this, homeContentPOJOS);
        rv.setAdapter(searchResultAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void openPodcastPage(String id) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.startPodcastFragment(id);
        }
    }
}
