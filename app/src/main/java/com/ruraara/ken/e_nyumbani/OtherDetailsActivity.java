package com.ruraara.ken.e_nyumbani;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherDetailsActivity extends AppCompatActivity {

    boolean press;
    CircleImageView mAvatarView;
    ImageButton mChangePickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_details);


        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_other_details, null);

        mAvatarView = dialogView.findViewById(R.id.avatar);
        mChangePickView = dialogView.findViewById(R.id.changePic);

        mAvatarView.setImageResource(R.drawable.img2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                press = true;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                press = false;
            }
        });


        builder.setView(dialogView);
        // Set other dialog properties
        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                if (!press) {
                    Log.d("Dialog: ", "dismissed");
                    finish();
                }
            }
        });

        dialog.show();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_agent:
                if (checked)
                    // Property agents
                    break;
            case R.id.radio_user:
                if (checked)
                    // Property buyers
                    break;
        }
    }
}
