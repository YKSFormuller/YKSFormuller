package com.yksformuller.activity;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.yksformuller.R;
import com.yksformuller.fragment.BellFragment;
import com.yksformuller.fragment.DownloadFragment;
import com.yksformuller.fragment.GeoFragment;
import com.yksformuller.fragment.MathFragment;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment = null;
    String fragment_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
