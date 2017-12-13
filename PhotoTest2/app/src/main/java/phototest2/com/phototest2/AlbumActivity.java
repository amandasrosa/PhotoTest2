package phototest2.com.phototest2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import phototest2.com.phototest2.model.Pictures;
import phototest2.com.phototest2.model.PicturesDAO;

/**
 * Created by Amanda on 08/12/2017.
 */

public class AlbumActivity extends AppCompatActivity {
    private String photographerName;
    private GridView photoAlbum;
    private GridAdapter gridAdapter;
    ArrayList<Pictures> pictures = new ArrayList<Pictures>();

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        photographerName = getIntent().getStringExtra("photographerName");
        TextView photographerTextView = (TextView) findViewById(R.id.photographer_name_album);
        photographerTextView.setText(photographerName);

    }

    private void loadPhotoAlbum() {

        PicturesDAO dao = new PicturesDAO(this);
        pictures = dao.dbSearch(photographerName);

        if (pictures.isEmpty() || pictures == null) {
            Toast.makeText(AlbumActivity.this, "There are no pictures to show. Go back and take some photos.", Toast.LENGTH_LONG).show();
        }
        dao.close();

        photoAlbum = (GridView)findViewById(R.id.album_pictures);
        gridAdapter = new GridAdapter(this,R.layout.grid_view_photo,pictures);
        photoAlbum.setAdapter(gridAdapter);

        photoAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pictures.get(position).turnSelected();
                ((GridAdapter)photoAlbum.getAdapter()).notifyDataSetChanged();
            }
        });

        Button smsButton = (Button)findViewById(R.id.sms_button);
        Button backButton = (Button)findViewById(R.id.back_button);

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedPics = "";
                for (Pictures p : pictures) {
                    if (p.selected()) {
                        selectedPics += ", Photo " + p.toString();
                    }

                }
                selectedPics = selectedPics.substring(2);
                String body = "The photo section was fantastic, please look " + selectedPics + ". They are incredible!";

                Intent intentSMS = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
                intentSMS.putExtra("sms_body", body);

                try {
                    startActivity(intentSMS);
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AlbumActivity.this,
                            "SMS failed, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentGotoForm = new Intent(AlbumActivity.this, MainActivity.class);
                photographerName = getIntent().getStringExtra("photographerName");
                intentGotoForm.putExtra("photographerName", photographerName);
                startActivity(intentGotoForm);
            }
        });
    }

    @Override
    protected void onResume() {
        loadPhotoAlbum();
        super.onResume();
    }

}
