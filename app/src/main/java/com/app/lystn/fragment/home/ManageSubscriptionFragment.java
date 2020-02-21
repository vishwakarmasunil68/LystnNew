package com.app.lystn.fragment.home;

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
import com.app.lystn.adapter.SearchResultAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ManageSubscriptionFragment extends FragmentController {

    @BindView(R.id.rv_subscription)
    RecyclerView rv_subscription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_manage_subscription,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchResultPOJOS.add("");
        searchResultPOJOS.add("");
        searchResultPOJOS.add("");
        searchResultPOJOS.add("");

        attachGenericAdapter(rv_subscription);

    }

    SearchResultAdapter searchResultAdapter;
    List<String> searchResultPOJOS = new ArrayList<>();

    public void attachGenericAdapter(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getActivity(), this, searchResultPOJOS);
        rv.setAdapter(searchResultAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

}
