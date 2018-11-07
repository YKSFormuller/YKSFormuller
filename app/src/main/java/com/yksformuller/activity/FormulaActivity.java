package com.yksformuller.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksformuller.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormulaActivity extends AppCompatActivity {

    @BindView(R.id.formulName)
    TextView formulName;
    @BindView(R.id.photoURL)
    ImageView photoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formula);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        showFormula();
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
        return super.onOptionsItemSelected(item);
    }

}
