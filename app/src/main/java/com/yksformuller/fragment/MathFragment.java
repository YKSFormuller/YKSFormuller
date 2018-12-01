package com.yksformuller.fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.R;
import com.yksformuller.adapter.FormulaAdapter;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.model.SwipeController;
import com.yksformuller.model.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

import model.Formula;

public class MathFragment extends Fragment implements View.OnClickListener, ItemClickListener,SearchView.OnQueryTextListener {

    FirebaseDatabase db;
    FormulaAdapter adapter;
    RecyclerView rvMmathList;
    Fragment fragment = null;
    String fragment_name;
    Bundle args;
    List<String> mathSubjectList = new ArrayList<String>();
    SearchView searchView;
    SwipeController swipeController = null;

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
        searchView = (SearchView) view.findViewById(R.id.searchViewMath);
       setCustomizeSearchView();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };


        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvMmathList);


        adapter = new FormulaAdapter(this.getActivity(), mathSubjectList);

        rvMmathList.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMmathList.setLayoutManager(linearLayoutManager);


        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                //KAYDET BUTONUNA TIKLANDIĞINDA GİRİLEN YER 
              //  adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvMmathList);

        rvMmathList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        searchView.setOnQueryTextListener(this);
        adapter.setClickListener(this);

        return view;


    }

    public void createSubjectList() {

        DatabaseReference dbFormula = db.getReference("matematik");
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
                    String subjectName = formulas.getValue(Formula.class).getKonuAdi();

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


        fragment = new SubjectFragment();
        fragment_name = "Konular";
        args = new Bundle();
        args.putString("ders", "matematik");
        args.putString("konu", mathSubjectList.get(position));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //fragment değişimi gerçekleştiriliyor.
        transaction.replace(R.id.container, fragment).commit();
        //stackteki fragment sayısı
        int count = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        //stackte birden fazla fragment birikmesini önlüyor.
        if (count != 0) {
            FragmentManager.BackStackEntry backStackEntry = getActivity().getSupportFragmentManager().getBackStackEntryAt(count - 1);
            if (backStackEntry.getName().contains(fragment_name)) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    boolean arama = false;
    ArrayList<String> list;

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.toLowerCase();
        list = new ArrayList<>();
        for (String subject : mathSubjectList) {
            String dessertName = subject.toLowerCase();
            if (dessertName.contains(newText)) {
                list.add(subject);
            }

        }
        adapter.setFilter(list);
        arama = true;
        return true;
    }

    private void setCustomizeSearchView() {
        int searchSrcTextId = getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(searchSrcTextId);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setHintTextColor(Color.BLACK);


        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.onActionViewExpanded();
            }
        });

    }
}
