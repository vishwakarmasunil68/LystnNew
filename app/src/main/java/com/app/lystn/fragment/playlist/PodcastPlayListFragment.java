package com.app.lystn.fragment.playlist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.adapter.GenrePlayListAdapter;
import com.app.lystn.fragmentcontroller.FragmentController;
import com.app.lystn.pojo.artiste.PodcastDetailPOJO;
import com.app.lystn.pojo.artiste.PodcastEpisodeDetailsPOJO;
import com.app.lystn.pojo.home.HomeContentPOJO;
import com.app.lystn.util.TagUtils;
import com.app.lystn.webservice.DownloadCallback;
import com.app.lystn.webservice.DownloadSongManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

public class PodcastPlayListFragment extends FragmentController {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_sorting)
    LinearLayout ll_sorting;
    @BindView(R.id.tv_phases)
    TextView tv_phases;
    @BindView(R.id.ll_download)
    LinearLayout ll_download;

    List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS = new ArrayList<>();
    PodcastDetailPOJO podcastDetailPOJO;

    int downloadingPosition = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_podcast_playlist, container, false);
        setUpView(getActivity(), this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachGenericAdapter(recyclerView);

        ll_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                if (getActivity() instanceof HomeActivity) {
//                    DownloadPOJO downloadPOJO=new DownloadPOJO();
//                    downloadPOJO.setPodcastEpisodeDetailsPOJOS(podcastEpisodeDetailsPOJOS);
//                    HomeActivity homeActivity = (HomeActivity) getActivity();
//                    homeActivity.downloadSong(downloadPOJO);
//                }
                downloadingPosition = 0;
                downloadAllSongs();
            }
        });

        ll_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_sort_popup, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(getActivity().getApplicationContext(), "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (item.getItemId()) {
                            case R.id.popup_az:
                                Log.d(TagUtils.getTag(), "az clicked");
                                sortAsc(true);
                                break;
                            case R.id.popup_za:
                                Log.d(TagUtils.getTag(), "za clicked");
                                sortAsc(false);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();//showing popup menu
            }
        });
    }

    public void downloadAllSongs() {
        if (getActivity() instanceof HomeActivity) {

            if (podcastEpisodeDetailsPOJOS.size() > 0 && downloadingPosition != -1 && ((podcastEpisodeDetailsPOJOS.size()) > downloadingPosition)) {

                if (!podcastEpisodeDetailsPOJOS.get(downloadingPosition).isDownloaded()) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    new DownloadSongManager(getActivity().getApplicationContext(),
                            homeActivity.getDbManager(),
                            new DownloadCallback() {
                                @Override
                                public void onSuccessDownload(String podcast_id) {
                                    downloadingPosition++;
                                    podcastEpisodeAdapter.notifyDataSetChanged();
                                    downloadAllSongs();
                                }

                                @Override
                                public void onDownloadFailed(String error) {
                                    podcastEpisodeAdapter.notifyDataSetChanged();
                                }
                            }).downloadSong(podcastEpisodeDetailsPOJOS.get(downloadingPosition), podcastDetailPOJO);
                }
            }
        }
    }

    public void setPhases(String size) {
        tv_phases.setText(size + " Episodes");
    }

    public void setPodcastList(PodcastDetailPOJO podcastDetailPOJO, List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS) {
        this.podcastEpisodeDetailsPOJOS.clear();
        this.homeContentPOJOS.clear();
        this.podcastDetailPOJO = podcastDetailPOJO;
        this.podcastEpisodeDetailsPOJOS.addAll(podcastEpisodeDetailsPOJOS);
        for (PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO : podcastEpisodeDetailsPOJOS) {
            HomeContentPOJO homeContentPOJO = new HomeContentPOJO();

            homeContentPOJO.setConId(podcastEpisodeDetailsPOJO.getEpisodeId());
            homeContentPOJO.setConName(podcastEpisodeDetailsPOJO.getTitle());
            homeContentPOJO.setImgIrl(podcastEpisodeDetailsPOJO.getImgLocalUri().toString());
            homeContentPOJO.setCotDeepLink(podcastEpisodeDetailsPOJO.getStreamUri());
            homeContentPOJO.setDescription(podcastEpisodeDetailsPOJO.getDescription());
            homeContentPOJO.setSubtitle(podcastEpisodeDetailsPOJO.getSubtitle());
            homeContentPOJO.setPlayTimes(podcastEpisodeDetailsPOJO.getPlaybackCount());

            homeContentPOJOS.add(homeContentPOJO);

        }
        podcastEpisodeAdapter.notifyDataSetChanged();
    }

    public List<HomeContentPOJO> getHomeContentPOJOS() {
        return homeContentPOJOS;
    }

    public void setHomeContentPOJOS(List<HomeContentPOJO> homeContentPOJOS) {
        this.homeContentPOJOS.clear();
        this.homeContentPOJOS.addAll(homeContentPOJOS);
        podcastEpisodeAdapter.notifyDataSetChanged();
    }

    public void sortAsc(boolean is_asc) {
        List<PodcastEpisodeDetailsPOJO> podcastEpisodeDetailsPOJOS = new ArrayList<>(this.podcastEpisodeDetailsPOJOS);
        Collections.sort(podcastEpisodeDetailsPOJOS, new Comparator<PodcastEpisodeDetailsPOJO>() {
            public int compare(PodcastEpisodeDetailsPOJO s1, PodcastEpisodeDetailsPOJO s2) {
                // notice the cast to (Integer) to invoke compareTo
//                if (is_asc) {
//                    return (s1.getTitle().trim()).compareTo(s2.getTitle().trim());
//                } else {
//                    return (s2.getTitle().trim()).compareTo(s1.getTitle().trim());
//                }

                if (is_asc) {
                    return Integer.compare(s1.getEpisodeSeq(), s2.getEpisodeSeq());
                } else {
                    return Integer.compare(s2.getEpisodeSeq(), s1.getEpisodeSeq());
                }
            }
        });

        this.podcastEpisodeDetailsPOJOS.clear();
        this.podcastEpisodeDetailsPOJOS.addAll(podcastEpisodeDetailsPOJOS);

        this.homeContentPOJOS.clear();

        for (PodcastEpisodeDetailsPOJO podcastEpisodeDetailsPOJO : podcastEpisodeDetailsPOJOS) {
            HomeContentPOJO homeContentPOJO = new HomeContentPOJO();

            homeContentPOJO.setConId(podcastEpisodeDetailsPOJO.getEpisodeId());
            homeContentPOJO.setConName(podcastEpisodeDetailsPOJO.getTitle());
            homeContentPOJO.setImgIrl(podcastEpisodeDetailsPOJO.getImgLocalUri().toString());
            homeContentPOJO.setCotDeepLink(podcastEpisodeDetailsPOJO.getStreamUri());
            homeContentPOJO.setDescription(podcastEpisodeDetailsPOJO.getDescription());
            homeContentPOJO.setSubtitle(podcastEpisodeDetailsPOJO.getSubtitle());
            homeContentPOJO.setPlayTimes(podcastEpisodeDetailsPOJO.getPlaybackCount());

            homeContentPOJOS.add(homeContentPOJO);

        }

        this.podcastEpisodeAdapter.notifyDataSetChanged();
        setCurrentPlayingPosition();

    }

    public void setCurrentPlayingPosition() {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.setHomeContentPOJOS(homeContentPOJOS);
        }
    }

    public PodcastDetailPOJO getPodcastDetailPOJO() {
        return podcastDetailPOJO;
    }

    List<HomeContentPOJO> homeContentPOJOS = new ArrayList<>();
    GenrePlayListAdapter podcastEpisodeAdapter;

    public void attachGenericAdapter(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        podcastEpisodeAdapter = new GenrePlayListAdapter(getActivity(), this, podcastEpisodeDetailsPOJOS);
        recyclerView.setAdapter(podcastEpisodeAdapter);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void playAudio(int position) {
        if (getActivity() instanceof HomeActivity) {
            HomeActivity homeActivity = (HomeActivity) getActivity();
            homeActivity.playAudio(homeContentPOJOS, position, "Genre", podcastDetailPOJO);
        }
    }

}
