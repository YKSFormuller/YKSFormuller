package com.yksformuller.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.yksformuller.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends AppCompatActivity {

    @BindView(R.id.formulName)
    TextView formulName;
    @BindView(R.id.photoURL)
    ImageView photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(photoURL);
        photoViewAttacher.update();

        showFormula();
    }

    private void showFormula(){
        Intent intent=getIntent();
        formulName.setText(intent.getStringExtra("formulName"));
        byte [] blob =intent.getByteArrayExtra("photoURL");
        Bitmap bmp=BitmapFactory.decodeByteArray(blob,0,blob.length);
        photoURL.setImageBitmap(bmp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
