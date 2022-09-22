package com.example.collectme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//References: Tutlane.com. 2021. Android ListView with Examples - Tutlane. [online] Available at: <https://www.tutlane.com/tutorial/android/android-listview-with-examples> [Accessed 26 May 2021].
//References: Stack Overflow. 2021. take a picture and save it using android studio. [online] Available at: <https://stackoverflow.com/questions/40117332/take-a-picture-and-save-it-using-android-studio> [Accessed 26 May 2021].
//References: Stack Overflow. 2021. How to save user input from EditText to a variable to be used on a different Activity. [online] Available at: <https://stackoverflow.com/questions/31090558/how-to-save-user-input-from-edittext-to-a-variable-to-be-used-on-a-different-act> [Accessed 26 May 2021].
//References: Professor DK, 2021. Simple Login App Tutorial Using Android Studio 2.3.3 (NEW). [video] Available at: <https://www.youtube.com/watch?v=lF5m4o_CuNg> [Accessed 26 May 2021].

public class AddItems extends AppCompatActivity {
    Button signOut, add, take;
    ImageView photo;
    SharedPreferences itm;
    EditText category, description, date;
    DatabaseReference ref;
    Items items;
    static final int REQUEST_IMAGE_CAPTURE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);

        ref = FirebaseDatabase.getInstance().getReference().child("Items");

        items = new Items();

        signOut = (Button) findViewById(R.id.btnSignOut3);
        signOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(AddItems.this, MainActivity.class);
                startActivity(intent);
            }
        });

        itm = getSharedPreferences("Items",MODE_PRIVATE);

        category = (EditText) findViewById(R.id.txtCat);
        description = (EditText) findViewById(R.id.txtDesc);
        date = (EditText) findViewById(R.id.txtDate);

        add = (Button) findViewById(R.id.btnAdd2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ctg = category.getText().toString().trim();
                String desc = description.getText().toString().trim();
                String dt = date.getText().toString().trim();
                items.setCategory(ctg);
                items.setDescription(desc);
                items.setDate(dt);
                ref.push().setValue(items);
                Toast.makeText(AddItems.this, "Items have been added to database!", Toast.LENGTH_LONG).show();
            }
        });

        take = (Button) findViewById(R.id.btnTake);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo.setImageBitmap(imageBitmap);
            try {
                createImageFile();
                galleryAddPic();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    String currentPhotoPath;
    private File createImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName="JPEG"+timeStamp+"_";
        File storageDir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath="file:"+image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic(){
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f=new File(currentPhotoPath);
        Uri contentUri= Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void makeItem(String item){
        String or = itm.getString(item, null);
        SharedPreferences.Editor preferencesEditor = itm.edit();
        preferencesEditor.putString("Items",item);
        preferencesEditor.commit();
    }
}