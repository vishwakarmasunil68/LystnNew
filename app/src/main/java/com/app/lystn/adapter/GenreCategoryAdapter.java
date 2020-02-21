package com.app.lystn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.fragment.login.LoginTagFragment;
import com.app.lystn.pojo.login.CategoryTagPOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreCategoryAdapter extends RecyclerView.Adapter<GenreCategoryAdapter.ViewHolder> {

    private List<CategoryTagPOJO> items;
    Activity activity;
    Fragment fragment;


    public GenreCategoryAdapter(Activity activity, Fragment fragment, List<CategoryTagPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_genre_tag_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_genre.setText(items.get(position).getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(position).setActive(!items.get(position).isActive());
                if(fragment instanceof LoginTagFragment){
                    LoginTagFragment loginTagFragment= (LoginTagFragment) fragment;
                    loginTagFragment.continueBtnEnabling();
                }
                notifyDataSetChanged();
            }
        });

        if(items.get(position).isActive()){
            holder.tv_genre.setBackgroundResource(R.drawable.ll_tag_gradient);
        }else{
            holder.tv_genre.setBackgroundResource(R.drawable.ll_type_back);
        }

        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_genre)
        TextView tv_genre;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}