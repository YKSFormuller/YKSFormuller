package com.yksformuller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;
import com.yksformuller.adapter.FormulaAdapter;
import java.util.ArrayList;
import java.util.List;

public class GeoFragment extends Fragment  implements View.OnClickListener,ItemClickListener {

    FirebaseDatabase db;
    FormulaAdapter adapter;
    RecyclerView rvGeoList;
    List<model.Formula> listFormula = new ArrayList<model.Formula>();

    List<String>geoSubjectList=new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();

        createSubjectList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_geo, parent, false);
        rvGeoList=(RecyclerView) view.findViewById(R.id.geoList);


        adapter = new FormulaAdapter(this.getActivity(), geoSubjectList);

        rvGeoList.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGeoList.setLayoutManager(linearLayoutManager);

        adapter.setClickListener(this);
        return view;

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
    public void createSubjectList(){

        DatabaseReference dbFormula = db.getReference("geometri");
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot formulas:dataSnapshot.getChildren()){
//                    String formulaName=formulas.getValue(model.Formula.class).getFormulAdi();
                    String subjectName=formulas.getValue(model.Formula.class).getKonuAdi();
//                    String photoUrl=formulas.getValue(model.Formula.class).getResimurl();
//                    model.Formula formula=new model.Formula(formulaName,subjectName,photoUrl);
                   // listFormula.add(formula);
                    if(!geoSubjectList.contains(subjectName)){
                        geoSubjectList.add(subjectName);

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(View view, int position) {

        String s = geoSubjectList.get(position);
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
