package com.yksformuller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;
import com.yksformuller.activity.DownloadActivity;
import com.yksformuller.activity.FormulaActivity;
import com.yksformuller.adapter.FormulaAdapter;
import com.yksformuller.model.Database;
import com.yksformuller.model.DownloadData;
import com.yksformuller.model.SwipeController;
import com.yksformuller.model.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

import com.yksformuller.model.Formula;

public class SubjectFragment extends Fragment implements View.OnClickListener, ItemClickListener,SearchView.OnQueryTextListener {

    private FirebaseDatabase db;
    private Database data;
    private FormulaAdapter adapter;
    private RecyclerView rvSubjectList;
    private TextView textView;
    private SwipeController swipeController = null;
    private List<String> SubjectList = new ArrayList<String>();
    private List<Formula> formulaList = new ArrayList<Formula>();
    private List<DownloadData> list1= new ArrayList<DownloadData>();
    private List<DownloadData> list2= new ArrayList<DownloadData>();
    private String ders, konu;
    private SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ders = getArguments().getString("ders");
        konu = getArguments().getString("konu");
        db = FirebaseDatabase.getInstance();
        this.data=new Database(getActivity());
        if(!ders.equals("download")){
            createSubjectList();
        }
        if(ders.equals("download")){
            if(konu.equals("Notlarım")){
                list1=data.getNot();
                for(int i=0; i<list1.size(); i++){
                    SubjectList.add(list1.get(i).getFormulaName());
                }
            }
            else{
                list1=data.getFormula(konu);
                for(int i=0; i<list1.size(); i++){
                    SubjectList.add(list1.get(i).getFormulaName());
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        rvSubjectList = (RecyclerView) view.findViewById(R.id.subjectList);
        textView =(TextView) view.findViewById(R.id.textView);
        if(konu.equals("Notlarım") && list1.size()==0){
          rvSubjectList.setVisibility(View.INVISIBLE);
          textView.setVisibility(View.VISIBLE);
          textView.setText("Eklenmiş bir notunuz bulunmamaktadır.");
          textView.setTextSize(20);
        }
        searchView=(SearchView)view.findViewById(R.id.searchViewSubject);
        adapter = new FormulaAdapter(this.getActivity(), SubjectList);
        rvSubjectList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSubjectList.setLayoutManager(linearLayoutManager);
        setCustomizeSearchView();
        searchView.setOnQueryTextListener(this);
        adapter.setClickListener(this);
        if(konu.equals("Notlarım")){
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

            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvSubjectList);
            swipeController = new SwipeController("SİL ",new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    data.deleteNotList(SubjectList.get(position));
                    SubjectList.remove(position);
                    rvSubjectList.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Eklenmiş bir notunuz bulunmamaktadır.");
                    textView.setTextSize(20);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("YKS Formüller");
                    builder.setMessage("Silme işleminiz başarıyla gerçekleşti.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(rvSubjectList);

            rvSubjectList.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });
        }
        rvSubjectList.setAdapter(adapter);
        return view;
    }

    public void createSubjectList() {
        DatabaseReference dbFormula = db.getReference(ders);
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
                    String formulaName = formulas.getValue(Formula.class).getFormulAdi();
                    String subjectName = formulas.getValue(Formula.class).getKonuAdi();
                    String photoUrl = formulas.getValue(Formula.class).getResimurl();
                    Formula formula = new Formula(formulaName, subjectName, photoUrl);
                    if (subjectName.equals(konu)) {
                        SubjectList.add(formulaName);
                        formulaList.add(formula);
                    }

                }
                for (int i = 4; i <= SubjectList.size(); i += 5) {
                    SubjectList.add(i,"");

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
        if(ders.equals("download")){
            Intent intent=new Intent(getActivity(),DownloadActivity.class);
            intent.putExtra("formulName", SubjectList.get(position));
            intent.putExtra("photoURL",list1.get(position).getImageURL());
            getActivity().startActivity(intent);
        }
        else{
            Intent intent = new Intent(getActivity(), FormulaActivity.class);
            intent.putExtra("subjectName",formulaList.get(position).getKonuAdi());
            intent.putExtra("formulName", SubjectList.get(position));
            intent.putExtra("photoURL", formulaList.get(position).getResimurl());
            getActivity().startActivity(intent);
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
        for (String subject : SubjectList) {
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