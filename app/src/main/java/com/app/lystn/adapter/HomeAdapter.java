package com.app.lystn.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.activity.HomeActivity;
import com.app.lystn.pojo.home.HomePOJO;
import com.app.lystn.util.TagUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomePOJO> items;
    Activity activity;
    Fragment fragment;


    public HomeAdapter(Activity activity, Fragment fragment, List<HomePOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_home_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


//        Log.d(TagUtils.getTag(), "item size:-" + items.size());
        if (items.get(position) != null) {
//            Log.d(TagUtils.getTag(), "name:-" + items.get(position).getBkName());
//            Log.d(TagUtils.getTag(), "type:-" + items.get(position).getBkType());
//            Log.d(TagUtils.getTag(), "shape:-" + items.get(position).getShapeType());
            holder.tv_bk_name.setText(items.get(position).getBkName());

            holder.tv_see_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (items.get(position).getBkType().equalsIgnoreCase("category")) {
                        if (activity instanceof HomeActivity) {
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.showCategoryListFragment(items.get(position).getBkId());
                        }
                    } else if (
                            items.get(position).getBkType().equalsIgnoreCase("genre") ||
                                    items.get(position).getBkType().equalsIgnoreCase("radio") ||
                                    items.get(position).getBkType().equalsIgnoreCase("artiste")
                    ) {
                        if(items.get(position).getBkType().equalsIgnoreCase("radio")){
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.showAllRadioContentFragment(items.get(position));
                        }else{
                            HomeActivity homeActivity = (HomeActivity) activity;
                            homeActivity.showHomeAllContentFragment(items.get(position));
                        }
                    }
                }
            });

            if (items.get(position).getBkType().equalsIgnoreCase("spotlight")) {
                holder.tv_see_all.setVisibility(View.GONE);

                SpotlightAdapter spotlightAdapter;

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                holder.rv_bk.setHasFixedSize(true);
                holder.rv_bk.setLayoutManager(linearLayoutManager);
                spotlightAdapter = new SpotlightAdapter(activity, fragment, items.get(position).getHomeContentPOJOS());
                holder.rv_bk.setAdapter(spotlightAdapter);
                holder.rv_bk.setNestedScrollingEnabled(false);
                holder.rv_bk.setItemAnimator(new DefaultItemAnimator());
            } else {
                if (items.get(position).getShapeType().equalsIgnoreCase("pill")) {

                    HomePillsAdapter homePillsAdapter;

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                    holder.rv_bk.setHasFixedSize(true);
                    holder.rv_bk.setLayoutManager(linearLayoutManager);
                    homePillsAdapter = new HomePillsAdapter(activity, fragment, items.get(position).getHomeContentPOJOS(),items.get(position).getBkType());
                    holder.rv_bk.setAdapter(homePillsAdapter);
                    holder.rv_bk.setNestedScrollingEnabled(false);
                    holder.rv_bk.setItemAnimator(new DefaultItemAnimator());

                } else if (items.get(position).getShapeType().equalsIgnoreCase("circle")) {

                    Log.d(TagUtils.getTag(), "bkname:-" + items.get(position).getBkName());
                    Log.d(TagUtils.getTag(), "position:-" + position);

                    HomeCircleAdapter homeCircleAdapter;

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                    holder.rv_bk.setHasFixedSize(true);
                    holder.rv_bk.setLayoutManager(linearLayoutManager);
                    homeCircleAdapter = new HomeCircleAdapter(activity, fragment, items.get(position).getHomeContentPOJOS(),items.get(position).getBkType());
                    holder.rv_bk.setAdapter(homeCircleAdapter);
                    holder.rv_bk.setNestedScrollingEnabled(false);
                    holder.rv_bk.setItemAnimator(new DefaultItemAnimator());

                } else if (items.get(position).getShapeType().equalsIgnoreCase("square")) {

                    HomeSquareTypeAdapter homeSquareTypeAdapter;

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
                    holder.rv_bk.setHasFixedSize(true);
                    holder.rv_bk.setLayoutManager(linearLayoutManager);
                    homeSquareTypeAdapter = new HomeSquareTypeAdapter(activity, fragment, items.get(position).getHomeContentPOJOS(),items.get(position).getBkType());
                    holder.rv_bk.setAdapter(homeSquareTypeAdapter);
                    holder.rv_bk.setNestedScrollingEnabled(false);
                    holder.rv_bk.setItemAnimator(new DefaultItemAnimator());

                }
            }
        }

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_bk_name)
        TextView tv_bk_name;
        @BindView(R.id.tv_see_all)
        TextView tv_see_all;
        @BindView(R.id.rv_bk)
        RecyclerView rv_bk;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}