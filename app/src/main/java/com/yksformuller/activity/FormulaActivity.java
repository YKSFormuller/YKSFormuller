package com.yksformuller.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yksformuller.R;
import com.yksformuller.model.Database;
import com.yksformuller.model.DownloadData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormulaActivity extends AppCompatActivity {

    @BindView(R.id.formulName)
    TextView formulName;
    @BindView(R.id.photoURL)
    ImageView photoURL;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference httpsReference;
    FirebaseAuth mAuth;
    Database dbSql;
    List<DownloadData> list=new ArrayList<DownloadData>();
    boolean varmi=false;
    final long ONE_MEGABYTE = 1024 * 1024;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        showFormula();
        storage=FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        dbSql=new Database(this);
        FirebaseUser user = mAuth.getCurrentUser();

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        PhotoViewAttacher photoViewAttacher=new PhotoViewAttacher(photoURL);
        photoViewAttacher.update();

        if (user != null) {

        } else {
            signInAnonymously();
        }
    }
    private void showFormula() {
        Intent intent=getIntent();
        formulName.setText(intent.getStringExtra("formulName"));
        Glide.with(this).load(intent.getStringExtra("photoURL")).into(photoURL);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_button, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        if(item.getItemId()==R.id.action_download1){
            downloadFormula();
            if(varmi==false){
                AlertDialog.Builder builder = new AlertDialog.Builder(FormulaActivity.this);
                builder.setTitle("YKS Formüller");
                builder.setMessage("Formülünüz kaydedildi. İndirilen Formüller bölümünde 'Notlarım' içerisinde bulabilirsiniz.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        });
    }
    private void downloadFormula(){
        final Intent intent=getIntent();
        list=dbSql.getNot();
        for(int i=0; i<list.size(); i++){
            if(intent.getStringExtra("formulName").equals(list.get(i).getFormulaName())){
                varmi=true;
            }
        }
        if(varmi){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("YKS Formüller");
            builder.setMessage("Bu formülü daha önce kaydettiniz.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
            builder.show();
        }
        else {
            httpsReference = storage.getReferenceFromUrl(intent.getStringExtra("photoURL"));
            httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    dbSql.addNot(intent.getStringExtra("subjectName"),intent.getStringExtra("formulName"),bytes);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }
    }

}
