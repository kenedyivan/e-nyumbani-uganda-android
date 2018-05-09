package com.ruraara.ken.enyumbani;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ruraara.ken.enyumbani.appData.AppData;
import com.ruraara.ken.enyumbani.models.PropertyForRent;
import com.ruraara.ken.enyumbani.utils.SharedDrawerNavigationUpHomeState;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ken on 10/30/17.
 */

public class PropertyForRentRecyclerViewAdapter
        extends RecyclerView.Adapter<PropertyForRentRecyclerViewAdapter.ViewHolder> {

    private final List<PropertyForRent.PropertyItem> mValues;
    private final Context mContext;

    public PropertyForRentRecyclerViewAdapter(List<PropertyForRent.PropertyItem> items, Context c) {
        mValues = items;
        mContext = c;
    }

    @Override
    public PropertyForRentRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.property_list_content, parent, false);
        return new PropertyForRentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PropertyForRentRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mRatingBar.setRating((float) mValues.get(position).rating);
        holder.mAddressView.setText(mValues.get(position).address);
        holder.mAgentView.setText(mValues.get(position).agent);

        double amount = Double.parseDouble(mValues.get(position).price);
        //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
        DecimalFormat formatter = new DecimalFormat("#,###");

        holder.mPriceView.setText(mValues.get(position).currency.toUpperCase()+" "+formatter.format(amount));

        Picasso.with(mContext)
                .load(mValues.get(position).image)
                .into(holder.mImageView);

        final SharedDrawerNavigationUpHomeState sd = new SharedDrawerNavigationUpHomeState(mContext);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                sd.goneToRent(SharedDrawerNavigationUpHomeState.GONE);
                Intent intent = new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra(PropertyDetailsActivity.ARG_ITEM_ID, holder.mItem.id);
                context.startActivity(intent);
            }
        });

        holder.mAgentButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                sd.goneToRent(SharedDrawerNavigationUpHomeState.GONE);
                Intent intent = new Intent(context, AgentProfileActivity.class);
                intent.putExtra(AgentProfileActivity.ARG_AGENT_ID, mValues.get(position).agentId);
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
        public final TextView mTitleView;
        public final TextView mAddressView;
        public final TextView mAgentView;
        public final TextView mPriceView;
        public final ImageView mImageView;
        public final RatingBar mRatingBar;
        public final Button mAgentButton;
        public PropertyForRent.PropertyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.title);
            mRatingBar = view.findViewById(R.id.rating);
            mAddressView = view.findViewById(R.id.address);
            mAgentView = view.findViewById(R.id.agent);
            mPriceView = view.findViewById(R.id.price);
            mImageView = view.findViewById(R.id.imageView);
            mAgentButton = view.findViewById(R.id.agent_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}