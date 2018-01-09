package com.ruraara.ken.e_nyumbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class TopSettingsActivity extends AppCompatActivity {

    View recyclerView;
    public static String SETTINGS_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_settings);
        setupActionBar();

        recyclerView = findViewById(R.id.settings_list);

        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.setAdapter(new SettingsItemRecyclerViewAdapter());
    }

    class SettingsItemRecyclerViewAdapter
            extends RecyclerView.Adapter<TopSettingsActivity.SettingsItemRecyclerViewAdapter.ViewHolder> {

        private final int[] icons = {R.drawable.ic_user, R.drawable.ic_info_black_24dp};
        private final String[] settings = {"Account", "System"};

        public SettingsItemRecyclerViewAdapter() {
        }

        @Override
        public TopSettingsActivity.SettingsItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_item_row, parent, false);
            return new TopSettingsActivity.SettingsItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TopSettingsActivity.SettingsItemRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.mItem = settings[position];
            holder.mSettingId.setText(String.valueOf(position));
            holder.mSettingIcon.setImageResource(icons[position]);
            holder.mSetting.setText(settings[position]);


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = null;
                    Context context = v.getContext();
                    String position = holder.mSettingId.getText().toString();
                    int pos = Integer.parseInt(position);
                    if (pos == 0) {
                        intent = new Intent(TopSettingsActivity.this, UserAccountActivity.class);
                        intent.putExtra(TopSettingsActivity.SETTINGS_ID, position);
                    } else if (pos == 1) {
                        intent = new Intent(TopSettingsActivity.this, SettingsActivity.class);
                        intent.putExtra(TopSettingsActivity.SETTINGS_ID, position);
                    }

                    context.startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return settings.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mSettingId;
            public final ImageView mSettingIcon;
            public final TextView mSetting;
            public String mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mSettingId = view.findViewById(R.id.setting_id);
                mSettingIcon = view.findViewById(R.id.setting_icon);
                mSetting = view.findViewById(R.id.setting);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mSetting.getText() + "'";
            }
        }
    }
}
