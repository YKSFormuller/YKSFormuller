package com.yksformuller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.R;
import com.yksformuller.adapter.FormulaAdapter;
import com.yksformuller.Interface.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import co.dift.ui.SwipeToAction;
import model.Formula;

public class MathFragment extends Fragment implements View.OnClickListener, ItemClickListener {

    FirebaseDatabase db;
    FormulaAdapter adapter;
    RecyclerView rvMmathList;
    List<Formula> listFormula = new ArrayList<Formula>();

    List<String> mathSubjectList = new ArrayList<String>();

    SwipeToAction swipeToAction;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();

        createSubjectList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_math, parent, false);

        rvMmathList = (RecyclerView) view.findViewById(R.id.mathList);

     //   rvMmathList.setHasFixedSize(true);

        //adapter = new FormulaAdapter(this.getActivity(), listFormula);
        adapter = new FormulaAdapter(this.getActivity(), mathSubjectList);

        rvMmathList.setAdapter(adapter);

//        swipeToAction=new SwipeToAction(rvMmathList, new SwipeToAction.SwipeListener() {
//            @Override
//            public boolean swipeLeft(Object itemData) {
//                Toast.makeText(getActivity().getApplicationContext(), "swipeLeft", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            @Override
//            public boolean swipeRight(Object itemData) {
//                return false;
//            }
//
//            @Override
//            public void onClick(Object itemData) {
//
//            }
//
//            @Override
//            public void onLongClick(Object itemData) {
//
//            }
//        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMmathList.setLayoutManager(linearLayoutManager);

        adapter.setClickListener(this);


        return view;


    }

    public void createSubjectList() {

        DatabaseReference dbFormula = db.getReference("matematik");
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
//                    String formulaName=formulas.getValue(Formula.class).getFormulAdi();
                    String subjectName = formulas.getValue(Formula.class).getKonuAdi();
//                    String photoUrl=formulas.getValue(Formula.class).getResimurl();
//                    Formula formula=new Formula(formulaName,subjectName,photoUrl);
//                    listFormula.add(formula);
                    if (!mathSubjectList.contains(subjectName))
                        mathSubjectList.add(subjectName);

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

        String s = mathSubjectList.get(position);
        Toast.makeText(getActivity().getApplicationContext(), s, Toast.LENGTH_SHORT).show();

    }

    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
//        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
//                .setAction(actionName, action);
//
//        View v = snack.getView();
//        v.setBackgroundColor(getResources().getColor(R.color.secondary));
//        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
//        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);
//
//        snack.show();
    }
}
