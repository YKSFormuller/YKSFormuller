package com.yksformuller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.yksformuller.R;
import com.yksformuller.model.YKSDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class BellFragment extends Fragment {

    CircularProgressBar circularTyt;
    CircularProgressBar circularAyt;
    TextView tyt,ayt;
    String tytDate,aytDate;
    int yuzdeTyt,yuzdeAyt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bell, parent, false);
        circularTyt = (CircularProgressBar)view.findViewById(R.id.tytCircle);
        circularAyt = (CircularProgressBar)view.findViewById(R.id.aytCircle);
        tyt=(TextView)view.findViewById(R.id.tytText);
        ayt=(TextView)view.findViewById(R.id.aytText);
        dateProgress();
        tyt.setText(tytDate+" gün kaldı.");
        ayt.setText(aytDate+" gün kaldı.");
        int animationDuration = 2500;
        circularTyt.setProgressWithAnimation(yuzdeTyt, animationDuration);
        circularAyt.setProgressWithAnimation(yuzdeAyt,animationDuration);
        return view;
    }

    private void dateProgress(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String [] currentTime=dateFormat.format(date).split("/");
        String [] tyt=getArguments().getString("sinav1").split("/");
        String [] ayt=getArguments().getString("sinav2").split("/");

        //TYT
        int gunTyt,ayTyt=0,yilTyt=0;
        gunTyt=Integer.parseInt(tyt[0])-Integer.parseInt(currentTime[0]);
        if(gunTyt<0)
        {
            gunTyt+=30;
            ayTyt-=1;
        }
        ayTyt+=Integer.parseInt(tyt[1])-Integer.parseInt(currentTime[1]);
        if(ayTyt<0)
        {
            ayTyt+=12;
            yilTyt-=1;
        }
        yilTyt+=Integer.parseInt(tyt[2])-Integer.parseInt(currentTime[2]);
        yuzdeTyt=(100*(gunTyt*1+ayTyt*30+yilTyt*365))/365;
        tytDate = String.valueOf(gunTyt*1+ayTyt*30+yilTyt*365);

        //AYT ve YDT
        int gunAyt,ayAyt=0,yilAyt=0;
        gunAyt=Integer.parseInt(ayt[0])-Integer.parseInt(currentTime[0]);
        if(gunAyt<0)
        {
            gunAyt+=30;
            ayAyt-=1;
        }
        ayAyt+=Integer.parseInt(ayt[1])-Integer.parseInt(currentTime[1]);
        if(ayAyt<0)
        {
            ayAyt+=12;
            yilAyt-=1;
        }
        yilAyt+=Integer.parseInt(ayt[2])-Integer.parseInt(currentTime[2]);
        yuzdeAyt=(100*(gunAyt*1+ayAyt*30+yilAyt*365))/365;
        aytDate = String.valueOf(gunAyt*1+ayAyt*30+yilAyt*365);
    }
}

