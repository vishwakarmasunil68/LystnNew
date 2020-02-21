package com.app.lystn.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.lystn.R;
import com.app.lystn.fragment.login.LoginSelectLanguageFragment;
import com.app.lystn.pojo.LanguagePOJO;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LanguageSelectAdapter extends RecyclerView.Adapter<LanguageSelectAdapter.ViewHolder> {

    private List<LanguagePOJO> items;
    Activity activity;
    Fragment fragment;


    public LanguageSelectAdapter(Activity activity, Fragment fragment, List<LanguagePOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_language_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.tv_country_name.setText(items.get(position).getName());
        holder.iv_country.setImageResource(items.get(position).getDrawable_id());
        if(items.get(position).isSelected()){
            holder.iv_right_tick.setVisibility(View.VISIBLE);
        }else{
            holder.iv_right_tick.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<items.size();i++){
                    items.get(i).setSelected(false);
                }
                items.get(position).setSelected(true);
                if(fragment instanceof LoginSelectLanguageFragment){
                    LoginSelectLanguageFragment loginSelectLanguageFragment= (LoginSelectLanguageFragment) fragment;
                    loginSelectLanguageFragment.checkContinueVisibility();
                }
                notifyDataSetChanged();
            }
        });


        holder.itemView.setTag(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_country_name)
        TextView tv_country_name;
        @BindView(R.id.iv_right_tick)
        ImageView iv_right_tick;
        @BindView(R.id.iv_country)
        ImageView iv_country;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}