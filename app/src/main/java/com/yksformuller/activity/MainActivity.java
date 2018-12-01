package com.yksformuller.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.R;
import com.yksformuller.fragment.BellFragment;
import com.yksformuller.fragment.DownloadFragment;
import com.yksformuller.fragment.GeoFragment;
import com.yksformuller.fragment.MathFragment;
import com.yksformuller.model.YKSDate;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment = null;
    String fragment_name;
    FirebaseDatabase db;
    List<YKSDate> dateList=new ArrayList<YKSDate>();
    Bundle args;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseDatabase.getInstance();
        args=new Bundle();
        createDateList();


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //başlangıç için matematik fragment çağrılıyor.
        getFragment(new MathFragment(), "Matematik");
        //bottom tabbar click dinleniyor.
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if(item.getItemId()==R.id.action_math){
                    fragment=new MathFragment();
                    fragment_name="Matematik";
                    //fragment çağırma
                    getFragment(fragment,fragment_name);
                }
                else if(item.getItemId()==R.id.action_geo){
                    fragment=new GeoFragment();
                    fragment_name="Geometri";
                    //fragment çağırma
                    getFragment(fragment,fragment_name);
                }
                else if(item.getItemId()==R.id.action_download){
                    fragment=new DownloadFragment();
                    fragment_name="Formüller";
                    //fragment çağırma
                    getFragment(fragment,fragment_name);
                }
                else if(item.getItemId()==R.id.action_bell){
                    fragment=new BellFragment();
                    fragment_name="YKS Sayaç";
                    //fragment çağırma
                    args.putString("sinav1",dateList.get(0).getDate());
                    args.putString("sinav2",dateList.get(1).getDate());
                    fragment.setArguments(args);
                    getFragment(fragment,fragment_name);
                }
                return true;
            }
        });
    }
    private void getFragment(Fragment fragment, String fragment_name){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //fragment değişimi gerçekleştiriliyor.
        transaction.replace(R.id.container, fragment).commit();
        //stackteki fragment sayısı
        int count = getSupportFragmentManager().getBackStackEntryCount();
        //stackte birden fazla fragment birikmesini önlüyor.
        if(count!=0){
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(count - 1);
            if (backStackEntry.getName().contains(fragment_name)) {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    private void createDateList() {
        DatabaseReference dbDate =db.getReference("yks");
        dbDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot date:dataSnapshot.getChildren()){
                    String exam= date.getValue(YKSDate.class).getExamName();
                    String dateString=date.getValue(YKSDate.class).getDate();
                    YKSDate yksDate=new YKSDate(exam,dateString);
                    dateList.add(yksDate);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
