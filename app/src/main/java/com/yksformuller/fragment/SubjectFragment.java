package com.yksformuller.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;
import com.yksformuller.activity.FormulaActivity;
import com.yksformuller.adapter.FormulaAdapter;

import java.util.ArrayList;
import java.util.List;

import model.Formula;

public class SubjectFragment extends Fragment implements View.OnClickListener, ItemClickListener {

    FirebaseDatabase db;
    FormulaAdapter adapter;
    RecyclerView rvSubjectList;
    List<String> SubjectList = new ArrayList<String>();
    List<Formula> list=new ArrayList<Formula>();
    String ders,konu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ders=getArguments().getString("ders");
        konu=getArguments().getString("konu");
        db = FirebaseDatabase.getInstance();
        createSubjectList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_subject, container, false);
        rvSubjectList = (RecyclerView) view.findViewById(R.id.subjectList);
        adapter = new FormulaAdapter(this.getActivity(), SubjectList);
        rvSubjectList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSubjectList.setLayoutManager(linearLayoutManager);
        adapter.setClickListener(this);
        return view;
    }

    public void createSubjectList() {

        DatabaseReference dbFormula = db.getReference(ders);
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
                    String formulaName=formulas.getValue(model.Formula.class).getFormulAdi();
                    String subjectName = formulas.getValue(model.Formula.class).getKonuAdi();
                    String photoUrl=formulas.getValue(Formula.class).getResimurl();
                    Formula formula=new Formula(formulaName,subjectName,photoUrl);
                    if (subjectName.equals(konu)){
                        SubjectList.add(formulaName);
                        list.add(formula);
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
        Intent intent=new Intent(getActivity(), FormulaActivity.class);
        intent.putExtra("formulName",SubjectList.get(position));
        intent.putExtra("photoURL",list.get(position).getResimurl());
        getActivity().startActivity(intent);
    }

}