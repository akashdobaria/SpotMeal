package com.spotmeal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.spotmeal.models.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

public class CreateEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = CreateEvent.class.getSimpleName();

    private DatabaseReference mDatabase;
    private DatabaseReference mEventsReference;
    FirebaseAuth auth;
    private DatabaseReference mUserEventsReference;
    ChildEventListener mEventsListener;
    private static final int  IMAGE_GALLERY_REQUEST = 1;
    private static final int  PLACE_PICKER_REQUEST = 2;
    private boolean mPicturesPermissionGranted;
    private String uid = "";
    private String title = "";
    private String cuisine = "";
    private String spiceLevel = "";
//    private HashMap<String,ArrayList<String >> starters = new HashMap<String,ArrayList<String >>();
//    private HashMap<String,ArrayList<String >> mainCourse= new HashMap<String,ArrayList<String >>();
//    private HashMap<String,ArrayList<String >> deserts= new HashMap<String,ArrayList<String >>();
//    private HashMap<String,ArrayList<String >> drinks = new HashMap<String,ArrayList<String >>();
    private ArrayList<String > starters = new ArrayList<String >();
    private ArrayList<String > mainCourse = new ArrayList<String >();
    private ArrayList<String > deserts = new ArrayList<String >();
    private ArrayList<String > drinks = new ArrayList<String >();
    private Long minGuests = 0L;
    private Long maxGuests = 0L;
    private String costPerPerson = "";
    private String eventDate = "";
    private String startTime = "";
    private String endTime = "";
    private String latitude = "";
    private String longitude = "";
    public  static ArrayList<String>selectedImages;
    private ViewGroup uploadPicturesPreview;

    Uri filePath;
    String fileName;
    ProgressDialog pd;

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://spotmeal-27930.appspot.com/");

        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get database reference
        this.mDatabase = FirebaseDatabase.getInstance().getReference();

        //get reference to events
        this.mEventsReference = this.mDatabase.child("events_p");

        this.mPicturesPermissionGranted = false;
        //get reference to users

        this.selectedImages = new ArrayList<String>();
        this.selectedImages.add("/storage/emulated/0/DCIM/Camera/IMG_20171129_034815.jpg");
        this.selectedImages.add("/storage/emulated/0/DCIM/Camera/IMG_20171129_034812.jpg");
        this.selectedImages.add("/storage/emulated/0/DCIM/Camera/IMG_20171129_034818.jpg");

        this.uploadPicturesPreview = (ViewGroup)findViewById(R.id.upload_pictures_view);


        ((Button)findViewById(R.id.upload_photos)).setVisibility(View.GONE);

        Spinner cuisine_spinner = (Spinner) findViewById(R.id.cuisine_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cuisines, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cuisine_spinner.setAdapter(adapter);

        Spinner spice_level_spinner = (Spinner) findViewById(R.id.spice_level_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.spice_levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spice_level_spinner.setAdapter(adapter);

        Spinner min_guests_spinner = (Spinner) findViewById(R.id.min_guests);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.min_guests, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        min_guests_spinner.setAdapter(adapter);

        Spinner max_guests_spinner = (Spinner) findViewById(R.id.max_guests);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.max_guests, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        max_guests_spinner.setAdapter(adapter);

        Spinner start_time_spinner = (Spinner) findViewById(R.id.start_time_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.start_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        start_time_spinner.setAdapter(adapter);

        Spinner end_time_spinner = (Spinner) findViewById(R.id.end_time_spinner);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.end_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        end_time_spinner.setAdapter(adapter);

        ((EditText)findViewById(R.id.date_input)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFrag frag = new DatePickerFrag();
                frag.show(getSupportFragmentManager(), "datePicker");
            }
        });

        Button selectPictures = (Button)findViewById(R.id.select_photos);
        selectPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "READ_EXTERNAL_STORAGE permission already granted");
                    mPicturesPermissionGranted = true;
                    requestPictureGalleryUpload();

                } else {
                    Log.i(TAG, "READ_EXTERNAL_STORAGE requesting permission");

                    ActivityCompat.requestPermissions(CreateEvent.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            IMAGE_GALLERY_REQUEST);
                }

            }
        });

        Button pickLocation = (Button)findViewById(R.id.pick_location_button);
        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    startActivityForResult(builder.build(CreateEvent.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
        final Button create_event = (Button) findViewById(R.id.create_event);
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_event();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
        /**
         * create event onClick handler
         */
        public void create_event(){

            ChildEventListener eventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Event event = dataSnapshot.getValue(Event.class);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Event event = dataSnapshot.getValue(Event.class);
//                    String eventKey = dataSnapshot.getKey();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    String eventKey = dataSnapshot.getKey();

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                    Event movedEvent = dataSnapshot.getValue(Event.class);
//                    String eventKey = dataSnapshot.getKey();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
//                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());

                }
            };


            this.mEventsReference.addChildEventListener(eventListener);
            // [END post_value_event_listener]

            // Keep copy of post listener so we can remove it when app stops
            this.mEventsListener = eventListener;

            mEventsReference = mDatabase.child("events_p");

            this.uid = auth.getUid();
            this.title = ((EditText)findViewById(R.id.event_title)).getText().toString();
            this.cuisine = ((Spinner)findViewById(R.id.cuisine_spinner)).getSelectedItem().toString();
            this.spiceLevel = ((Spinner)findViewById(R.id.spice_level_spinner)).getSelectedItem().toString();
            this.starters.add(((EditText)findViewById(R.id.starters_input1)).getText().toString());
            this.starters.add(((EditText)findViewById(R.id.starters_input2)).getText().toString());
            this.mainCourse.add(((EditText)findViewById(R.id.main_course_input1)).getText().toString());
            this.mainCourse.add(((EditText)findViewById(R.id.main_course_input2)).getText().toString());
            this.deserts.add(((EditText)findViewById(R.id.desert_input1)).getText().toString());
            this.deserts.add(((EditText)findViewById(R.id.desert_input2)).getText().toString());
            this.drinks.add(((EditText)findViewById(R.id.drinks_input1)).getText().toString());
            this.drinks.add(((EditText)findViewById(R.id.drinks_input2)).getText().toString());
            this.minGuests = Long.valueOf(((Spinner)findViewById(R.id.min_guests)).getSelectedItem().toString());
            this.maxGuests = Long.valueOf(((Spinner)findViewById(R.id.max_guests)).getSelectedItem().toString());

            this.costPerPerson = ((EditText)findViewById(R.id.cost)).getText().toString();
            this.eventDate = ((EditText)findViewById(R.id.date_input)).getText().toString();
            this.startTime = ((Spinner)findViewById(R.id.start_time_spinner)).getSelectedItem().toString();
            this.endTime = ((Spinner)findViewById(R.id.end_time_spinner)).getSelectedItem().toString();

            /* latitude and longitude set in resut listener */

            if( this.title.trim().equals("") || this.starters.size()== 0 || this.mainCourse.size()== 0||
                    this.deserts.size()== 0||this.drinks.size()== 0||this.costPerPerson.trim() == "" ||this.eventDate.trim().equals("")
                    || this.latitude.trim().equals("") || this.longitude.trim().equals(""))
            {
                ((EditText)findViewById(R.id.event_title)).setError( "All details is required!" );
                ((EditText)findViewById(R.id.event_title)).setHint("please enter details");
            } else{
                Event newEvent = new Event(this.uid, this.title, this.cuisine, this.spiceLevel,this.starters, this.mainCourse, this.deserts, this.drinks, this.minGuests, this.maxGuests, Long.valueOf(this.costPerPerson), this.eventDate, this.startTime, this.endTime,this.latitude, this.longitude, 0L);
                String key = mEventsReference.push().getKey();
                Log.i(TAG,"***** Key *******"+ key +"*************");
                newEvent.setKey(key);
                Map<String,Object> map = newEvent.toMap();
                mEventsReference.child(key).updateChildren(map);

                mUserEventsReference = mDatabase.child("users_p").orderByChild("uid").equalTo(auth.getUid()).getRef().child("events_hosting");
                map.clear();
                map.put(key, newEvent);
//                mDatabase.child("users_p").child(auth.getUid()).child("events_hosting").push().setValue(newEvent);
                mDatabase.child("users_p").child(auth.getUid()).child("events_hosting").updateChildren(map);

                pd.show();
                for(int i=0; i<selectedImages.size(); i++){

                    filePath = Uri.fromFile(new File(selectedImages.get(i)));
                    fileName = newEvent.getKey()+"/"+i;

                    if(filePath != null) {
//                    StorageReference childRef = storageRef.child(fileName.getText().toString());
                        StorageReference childRef = storage.getReferenceFromUrl("gs://spotmeal-27930.appspot.com/"+fileName);
                        //uploading the image
                        UploadTask uploadTask = childRef.putFile(filePath);

                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                pd.dismiss();
//                                Toast.makeText(UploadImageTry.this, "Upload successful", Toast.LENGTH_SHORT).show();
//                                imageContainer.setImageResource(0);
                                Log.i("image upload successful", "Path: "+filePath+"  |||   Filename: "+fileName);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

//                                Toast.makeText(UploadImageTry.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                                Log.i("image upload failed", "Path: "+filePath+"  |||   Filename: "+fileName);
                            }
                        });
                    }
                    else {
//                        Toast.makeText(UploadImageTry.this, "Select an image", Toast.LENGTH_SHORT).show();
                        Log.i("No images selected", "image list empty");
                    }
                }
                pd.dismiss();

                finish();
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == IMAGE_GALLERY_REQUEST) {
                    selectedImages = new ArrayList<String>();
                    //ontentResolver contentResolver = getContentResolver();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Log.i(TAG, "Processing selected images");

                    if (data.getClipData() != null) {

                        ClipData mClipData = data.getClipData();
                        if (mClipData.getItemCount() > 10) {
                            Log.i(TAG, "More than 10 images selected");
                            Toast.makeText(getApplicationContext(), "Please select upto 10 images only", Toast.LENGTH_LONG).show();
                        } else {
                            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                            for (int i = 0; i < mClipData.getItemCount(); i++) {
                                ClipData.Item item = mClipData.getItemAt(i);
                                Uri uri = item.getUri();
                                mArrayUri.add(uri);
                                // Get the cursor
                                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                                // Move to first row
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String selectedImage = cursor.getString(columnIndex);
                                int id = cursor.getInt(columnIndex);
                                selectedImages.add(selectedImage);
                                cursor.close();

                            }
                            Log.i(TAG, String.valueOf(selectedImages.size()));
                            showPicturesPreview(selectedImages);
                        }
                    }


                }
                if (requestCode == PLACE_PICKER_REQUEST) {

                    Place place = PlacePicker.getPlace(data, this);
                    this.latitude = String.valueOf(place.getLatLng().latitude);
                    this.longitude = String.valueOf(place.getLatLng().longitude);
                    String toastMsg = String.format("Place: %s", place.getName());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                    ((TextView) findViewById(R.id.selected_location)).setText(place.getName());
                }
            }
        }


        //this method enables users to select multiple pictures from the mobile device.
        public void requestPictureGalleryUpload(){
            Log.i(TAG, "requestPictureGalleryUpload");
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            photoPickerIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), IMAGE_GALLERY_REQUEST);
        }

        public void showPicturesPreview(final ArrayList<String> selectedPictures){
            Log.i(TAG, "showPicturesPreview-update grid");

            for(String uri: selectedPictures){
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setImageURI(Uri.fromFile(new File(uri)));
                this.uploadPicturesPreview.addView(imageView);
            }


//            ((Button)findViewById(R.id.upload_photos)).setVisibility(View.VISIBLE);
//
//            ((Button)findViewById(R.id.upload_photos)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                }
//            });
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,
                                               @NonNull String permissions[],
                                               @NonNull int[] grantResults) {
            mPicturesPermissionGranted = false;
            switch (requestCode) {
                case IMAGE_GALLERY_REQUEST: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mPicturesPermissionGranted = true;
                        requestPictureGalleryUpload();
                    }else{
                        Log.i(TAG," IMAGE_GALLERY_REQUEST not granted ");
                    }
                }
                break;
            }

        }
        public void toggleEventInfo(View v){

            Animation slide_up = AnimationUtils.loadAnimation(this,
                    R.anim.slide_up);
            Animation slide_down = AnimationUtils.loadAnimation(this,
                    R.anim.slide_down);
            TextView eventViewTitle = (TextView)findViewById(R.id.event_information_label);
            ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.event_information_view);
            if(hiddenPanel.isShown()){
                Log.i(TAG , "******************* slide up ****************");

                hiddenPanel.startAnimation(slide_up);
                hiddenPanel.setVisibility(View.GONE);
                eventViewTitle.setText(R.string.event_info_title_collapsed);

            }else{
                Log.i(TAG , "******************* slide down ****************");

                hiddenPanel.startAnimation(slide_down);
                hiddenPanel.setVisibility(View.VISIBLE);
                eventViewTitle.setText(R.string.event_info_title_expanded);

            }

        }

    public void toggleImageUpload(View v){

        Animation slide_up = AnimationUtils.loadAnimation(this,
                R.anim.slide_up);
        Animation slide_down = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
        TextView eventViewTitle = (TextView)findViewById(R.id.add_images_title);

        ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.add_images_view);
        if(hiddenPanel.isShown()){
            Log.i(TAG , "******************* slide up ****************");

            hiddenPanel.startAnimation(slide_up);
            hiddenPanel.setVisibility(View.GONE);
            eventViewTitle.setText(R.string.add_images_title_collapsed);

        }else{
            Log.i(TAG , "******************* slide down ****************");

            hiddenPanel.startAnimation(slide_down);
            hiddenPanel.setVisibility(View.VISIBLE);
            eventViewTitle.setText(R.string.add_images_title_expanded);

        }

    }

    public void toggleSelectLocation(View v){

        Animation slide_up = AnimationUtils.loadAnimation(this,
                R.anim.slide_up);
        Animation slide_down = AnimationUtils.loadAnimation(this,
                R.anim.slide_down);
        TextView locationViewTitle = (TextView)findViewById(R.id.select_location_title);

        ViewGroup hiddenPanel = (ViewGroup)findViewById(R.id.location_picker_view);
        if(hiddenPanel.isShown()){
            Log.i(TAG , "******************* slide up ****************");

            hiddenPanel.startAnimation(slide_up);
            hiddenPanel.setVisibility(View.GONE);
            locationViewTitle.setText(R.string.select_location_title_collapsed);
        }else{
            Log.i(TAG , "******************* slide down ****************");

            hiddenPanel.startAnimation(slide_down);
            hiddenPanel.setVisibility(View.VISIBLE);
            locationViewTitle.setText(R.string.select_location_title_expanded);


        }

    }

    public static class DatePickerFrag extends android.support.v4.app.DialogFragment
    {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), (CreateEvent) getActivity(), year, month, day);
            dialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return  dialog;
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dob = (month+1) + "/" + day + "/" + year;
        ((EditText)findViewById(R.id.date_input)).setText(dob);
    }


}
