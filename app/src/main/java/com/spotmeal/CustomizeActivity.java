package com.spotmeal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotmeal.models.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomizeActivity extends AppCompatActivity {
    Event currentEvent;

    CheckBox starter1;
    CheckBox starter2;
    CheckBox entree1;
    CheckBox entree2;
    CheckBox desert1;
    CheckBox desert2;
    CheckBox drink1;
    CheckBox drink2;
    CheckBox drink3;
    RadioButton spiceLevel1;
    RadioButton spiceLevel2;
    RadioButton spiceLevel3;
    String customOrderString;
    Button preview;
    Button back;
    EditText comments;
    private DatabaseReference mCurrentEventHostReference;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsersReference;
    private boolean validInput = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customize);
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mCurrentEventHostReference = FirebaseDatabase.getInstance().getReference().child("users_p").child(EventDetails.currentEvent.getUid());
        this.mUsersReference = this.mDatabase.child("users_p");

        currentEvent = EventDetails.currentEvent;
//        String starter = customEvent.getStarters();
        //customEvent = getIntent().getExtras().getParcelable("customEvent");
        customOrderString = "";
        starter1 = (CheckBox) findViewById(R.id.st1);
        starter2 = (CheckBox) findViewById(R.id.st2);

        entree1 = (CheckBox) findViewById(R.id.mc1);
        entree2 = (CheckBox) findViewById(R.id.mc2);

        desert1 = (CheckBox) findViewById(R.id.desert1);
        desert2 = (CheckBox) findViewById(R.id.desert2);

        spiceLevel1 = (RadioButton) findViewById(R.id.spice_low);
        spiceLevel2 = (RadioButton) findViewById(R.id.spice_medium);
        spiceLevel3 = (RadioButton) findViewById(R.id.spice_spicy);

        drink1 = (CheckBox) findViewById(R.id.drink1);
        drink2 = (CheckBox) findViewById(R.id.drink2);
//        drink3 = (CheckBox) findViewById(R.id.drink3);

        preview = (Button) findViewById(R.id.confirmBtn);
        back = (Button) findViewById(R.id.back_button);
        starter1.setText(EventDetails.currentEvent.getStarters().get(0));
        starter2.setText(currentEvent.getStarters().get(1));

        entree1.setText(currentEvent.getMainCourse().get(0));
        entree2.setText(currentEvent.getMainCourse().get(1));

        desert1.setText(currentEvent.getDeserts().get(0));
        desert2.setText(currentEvent.getDeserts().get(1));

        drink1.setText(currentEvent.getDrinks().get(0));
        drink2.setText(currentEvent.getDrinks().get(1));

        comments = (EditText)findViewById(R.id.customize_comment);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customOrderString = "";
                validateData();
                if(validInput){
                    setData();
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomizeActivity.this);
                    builder.setTitle("Confirm Reservation");
                    View viewInflated = LayoutInflater.from(view.getContext()).inflate(R.layout.reservation_preview_dialog,  null);
                    builder.setView(viewInflated);

                    final TextView reservation_preview = (TextView) viewInflated.findViewById(R.id.reservation_preview);
                    reservation_preview.setText(customOrderString);


                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("customizations", customOrderString);
                            map.put("noOfSeats", getIntent().getExtras().get("seatsToBeReserved"));
                            map.put("guestName", HomeActivity.mCurrentUser.getName());
                            map.put("contact", HomeActivity.mCurrentUser.getContact());

                            mCurrentEventHostReference.child("events_hosting").child(EventDetails.currentEvent.getKey()).child("reservations").push().setValue(map);
                            mUsersReference.child(HomeActivity.mCurrentUser.getUid()).child("events_attending").child(EventDetails.currentEvent.getKey()).setValue(EventDetails.currentEvent);
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }


            }
        });
    }

    public void validateData(){
        validInput = true;
        if((!starter1.isChecked() && !starter2.isChecked()) || (!entree1.isChecked() && !entree2.isChecked())){
            validInput = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Select at lease 1 Starter and 1 Entree!")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    public void setData(){

        customOrderString+="\nStarters: ";
        if(starter1.isChecked()){
            customOrderString+= starter1.getText().toString();
            customOrderString+="\t";
        }
        if(starter2.isChecked()){
            customOrderString+= starter2.getText().toString();
        }
        customOrderString+="\nEntrees: ";
        if(entree1.isChecked()){
            customOrderString+= entree1.getText().toString();
            customOrderString+="\t";

        }
        if(entree2.isChecked()){
            customOrderString+= entree2.getText().toString();

        }
        customOrderString+="\nDeserts: ";
        if(desert1.isChecked()){
            customOrderString+= desert1.getText().toString();
            customOrderString+="\t";
        }
        if(desert2.isChecked()){
            customOrderString+= desert2.getText().toString();
        }
        customOrderString+="\nDrinks: ";

        if(drink1.isChecked()){
            customOrderString+= drink1.getText().toString();
            customOrderString+="\t";
        }
        if(drink2.isChecked()){
            customOrderString+= drink2.getText().toString();
            customOrderString+="\t";
        }
        /*if(drink3.isChecked()){
            customOrderString+= drink3.getText().toString();
        }*/
        customOrderString+="\nSpice Level: ";
        if(spiceLevel1.isChecked()){
            customOrderString+= spiceLevel1.getText().toString();
        }
        if(spiceLevel2.isChecked()){
            customOrderString+= spiceLevel2.getText().toString();
        }
        if(spiceLevel3.isChecked()){
            customOrderString+= spiceLevel3.getText().toString();
        }
        customOrderString+="\nSpecial Request: "+comments.getText().toString();

        Log.i("FinalOrder","%%%%%%%%%%% Final order%%%%%%%" +customOrderString);
    }
}


