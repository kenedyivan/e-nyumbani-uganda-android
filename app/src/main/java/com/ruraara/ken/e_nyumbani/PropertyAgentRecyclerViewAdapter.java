package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.models.PropertyAgent;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ken on 10/30/17.
 */

public class PropertyAgentRecyclerViewAdapter
        extends RecyclerView.Adapter<PropertyAgentRecyclerViewAdapter.ViewHolder> {

    private final List<PropertyAgent.Agent> mValues;
    private final Context mContext;

    public PropertyAgentRecyclerViewAdapter(List<PropertyAgent.Agent> agents, Context c) {
        mValues = agents;
        mContext = c;
    }

    @Override
    public PropertyAgentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_agent_row, parent, false);
        return new PropertyAgentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PropertyAgentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final String name = mValues.get(position).firstName + " " + mValues.get(position).lastName;
        holder.mNameView.setText(name);
        holder.mCompanyView.setText(mValues.get(position).company);

        if (Integer.parseInt(mValues.get(position).all) == 1) {
            holder.mAllView.setText(mValues.get(position).all + " FeaturedProperty");
        } else {
            holder.mAllView.setText(mValues.get(position).all + " Properties");
        }

        holder.mSaleView.setText(mValues.get(position).sale + " For sale");

        holder.mRentView.setText(mValues.get(position).rent + " For rent");


        Picasso.with(mContext)
                .load(AppData.getAgentsImagesPath() + mValues.get(position).image)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, TabbedAgentPropertiesActivity.class);
                intent.putExtra(TabbedAgentPropertiesActivity.ARG_ITEM_ID, holder.mItem.id);
                intent.putExtra(TabbedAgentPropertiesActivity.ARG_ITEM_NAME, name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mCompanyView;
        public final TextView mAllView;
        public final TextView mSaleView;
        public final TextView mRentView;
        public final CircleImageView mImageView;
        public PropertyAgent.Agent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.name);
            mCompanyView = view.findViewById(R.id.company);
            mAllView = view.findViewById(R.id.all);
            mSaleView = view.findViewById(R.id.sale);
            mRentView = view.findViewById(R.id.rent);
            mImageView = view.findViewById(R.id.profile_image);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
