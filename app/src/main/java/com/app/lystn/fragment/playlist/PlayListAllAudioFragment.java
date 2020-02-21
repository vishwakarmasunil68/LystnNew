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
import com.app.lystn.adapter.PlayListAudioAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayListAllAudioFragment extends FragmentController {

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_playlist_all_audio,container,false);
        setUpView(getActivity(),this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attachGenericAdapter(rv_list);

        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");
        frag_playlist_all_audio.add("");

        attachGenericAdapter(rv_list);

    }

    List<String> frag_playlist_all_audio=new ArrayList<>();

    public void attachGenericAdapter(RecyclerView rv) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        PlayListAudioAdapter searchResultAdapter = new PlayListAudioAdapter(getActivity(), this, frag_playlist_all_audio);
        rv.setAdapter(searchResultAdapter);
        rv.setNestedScrollingEnabled(false);
        rv.setItemAnimator(new DefaultItemAnimator());
    }
}
