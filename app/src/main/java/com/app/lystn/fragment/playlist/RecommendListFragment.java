package com.app.lystn.fragment.playlist;

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
import com.app.lystn.adapter.RecommentRVAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecommendListFragment extends FragmentController {

    @BindView(R.id.rv_news)
    RecyclerView rv_news;
    @BindView(R.id.rv_music)
    RecyclerView rv_music;
    @BindView(R.id.rv_films)
    RecyclerView rv_films;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_recomment_list,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");
        genrePOJOS.add("");

        attachGenericAdapter(rv_films);
        attachGenericAdapter(rv_music);
        attachGenericAdapter(rv_news);

    }

    List<String> genrePOJOS = new ArrayList<>();

    public void attachGenericAdapter(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecommentRVAdapter homeGenreAdapter = new RecommentRVAdapter(getActivity(), this, genrePOJOS);
        recyclerView.setAdapter(homeGenreAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
