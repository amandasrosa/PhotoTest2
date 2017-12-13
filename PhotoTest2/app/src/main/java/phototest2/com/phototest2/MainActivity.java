package phototest2.com.phototest2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import phototest2.com.phototest2.model.Pictures;
import phototest2.com.phototest2.model.PicturesDAO;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_CODE = 990;
    private String dirAppPhoto;
    private String photographerNameString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getStringExtra("photographerName") != null) {
            EditText fieldPhotographerName = (EditText)findViewById(R.id.photographer_name);
            fieldPhotographerName.setText(getIntent().getStringExtra("photographerName"));
        }
        Button buttonPhoto = (Button)findViewById(R.id.button_photo);
        Button buttonAlbum = (Button)findViewById(R.id.album_button);

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText fieldPhotographerName = (EditText)findViewById(R.id.photographer_name);
                photographerNameString  = fieldPhotographerName.getText().toString();
                if (photographerNameString.equals("")) {
                    Toast.makeText(MainActivity.this, "Please fill the name of the photographer to take your picture", Toast.LENGTH_LONG).show();
                } else {
                    dirAppPhoto = getExternalFilesDir(null) + "/" + System.currentTimeMillis() + ".jpg";
                    File filePhoto = new File(dirAppPhoto);

                    if (filePhoto != null) {
                        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intentCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri uri = FileProvider.getUriForFile(view.getContext(), BuildConfig.APPLICATION_ID, filePhoto);
                        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                        if (intentCamera.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intentCamera, CAMERA_CODE);
                        }
                    }
                }
            }
        });

        buttonAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText fieldPhotographerName = (EditText)findViewById(R.id.photographer_name);
                photographerNameString  = fieldPhotographerName.getText().toString();
                Intent intentGotoForm = new Intent(MainActivity.this, AlbumActivity.class);
                intentGotoForm.putExtra("photographerName", photographerNameString);
                startActivity(intentGotoForm);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CODE) {
                if (photographerNameString.equals("")) {
                    Toast.makeText(MainActivity.this, "Photo cannot be saved, because there is no name for the photographer", Toast.LENGTH_LONG).show();
                } else {
                    Pictures pictures = new Pictures();
                    pictures.setPhotographerName(photographerNameString);
                    pictures.setPhotoPath(dirAppPhoto);
                    PicturesDAO dao = new PicturesDAO(this);
                    dao.dbInsert(pictures);
                    dao.close();

                    Toast.makeText(MainActivity.this, "Photo saved. Take more pictures for the album", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
