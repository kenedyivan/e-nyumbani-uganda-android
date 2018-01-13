package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.models.AgentProperty;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ken on 10/30/17.
 */

public class AgentPropertiesRecyclerViewAdapter
        extends RecyclerView.Adapter<AgentPropertiesRecyclerViewAdapter.ViewHolder> {

    private final List<AgentProperty.PropertyItem> mValues;
    private final Context mContext;

    public AgentPropertiesRecyclerViewAdapter(List<AgentProperty.PropertyItem> items, Context c) {
        mValues = items;
        mContext = c;
    }

    @Override
    public AgentPropertiesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_property_list_content, parent, false);
        return new AgentPropertiesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AgentPropertiesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mRatingBar.setRating((float) mValues.get(position).rating);
        holder.mAddressView.setText(mValues.get(position).address);

        String status;
        if(Integer.parseInt(mValues.get(position).status) == 1){
            status = "For sale";
        }else{
            status = "For rent";
        }

        holder.mAgentView.setText(status);

        double amount = Double.parseDouble(mValues.get(position).price);
        //DecimalFormat formatter = new DecimalFormat("#,###.00");  //// TODO: 12/1/17 when counting dollars with cents
        DecimalFormat formatter = new DecimalFormat("#,###");

        holder.mPriceView.setText(mValues.get(position).currency.toUpperCase()+" "+formatter.format(amount));

        Picasso.with(mContext)
                .load(mValues.get(position).image)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, PropertyDetailsActivity.class);
                intent.putExtra(PropertyDetailsActivity.ARG_ITEM_ID, holder.mItem.id);
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
        public AgentProperty.PropertyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.title);
            mRatingBar = view.findViewById(R.id.rating);
            mAddressView = view.findViewById(R.id.address);
            mAgentView = view.findViewById(R.id.agent);
            mPriceView = view.findViewById(R.id.price);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
