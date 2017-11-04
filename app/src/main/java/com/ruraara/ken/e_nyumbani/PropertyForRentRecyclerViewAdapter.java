package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruraara.ken.e_nyumbani.appData.AppData;
import com.ruraara.ken.e_nyumbani.dummy.Property;
import com.ruraara.ken.e_nyumbani.dummy.PropertyForRent;
import com.squareup.picasso.Picasso;

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
    public void onBindViewHolder(final PropertyForRentRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).title);
        holder.mAddressView.setText(mValues.get(position).address);
        holder.mAgentView.setText(mValues.get(position).agent);
        holder.mPriceView.setText(mValues.get(position).price);
        //holder.mImageView.setImageResource(mValues.get(position).image);

        Picasso.with(mContext)
                .load(AppData.getImagesPath() + mValues.get(position).image)
                .into(holder.mImageView);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                Intent intent = new Intent(context, PropertyDetails.class);
                intent.putExtra(PropertyDetails.ARG_ITEM_ID, holder.mItem.id);
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
        public PropertyForRent.PropertyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = view.findViewById(R.id.title);
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
