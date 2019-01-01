package com.yksformuller.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;
import com.yksformuller.adapter.FormulaAdapter;
import com.yksformuller.model.Database;
import com.yksformuller.model.Formula;
import com.yksformuller.model.SwipeController;
import com.yksformuller.model.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

public class GeoFragment extends Fragment implements View.OnClickListener, ItemClickListener, SearchView.OnQueryTextListener {

    FirebaseDatabase db;
    FormulaAdapter adapter;
    RecyclerView rvGeoList;
    SearchView searchView;
    Fragment fragment = null;
    String fragment_name;
    Bundle args;
    SwipeController swipeController = null;
    List<String> geoSubjectList = new ArrayList<String>();
    List<String> list1=new ArrayList<String>();
    FirebaseStorage storage;
    StorageReference httpsReference;
    Database dbSql;
    String tableName;
    boolean varmi=false;
    final long ONE_MEGABYTE = 1024 * 1024;
    ImageView imgOffline;
    TextView txtOffline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        dbSql=new Database(getActivity());
        storage=FirebaseStorage.getInstance();
        createSubjectList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_geo, parent, false);
        rvGeoList = (RecyclerView) view.findViewById(R.id.geoList);
        searchView = (SearchView) view.findViewById(R.id.searchViewGeo);
        imgOffline = (ImageView) view.findViewById(R.id.imgOfflineGeo);
        txtOffline = (TextView) view.findViewById(R.id.txtOfflineGeo);

        if (!isNetworkConnected()) {
            imgOffline.setVisibility(View.VISIBLE);
            txtOffline.setVisibility(View.VISIBLE);
            rvGeoList.setVisibility(View.GONE);
        }else{
            imgOffline.setVisibility(View.GONE);
            txtOffline.setVisibility(View.GONE);
            rvGeoList.setVisibility(View.VISIBLE);
        }

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


        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvGeoList);

        adapter = new FormulaAdapter(this.getActivity(), geoSubjectList);

        rvGeoList.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvGeoList.setLayoutManager(linearLayoutManager);

        swipeController = new SwipeController("KAYDET",new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                tableName=geoSubjectList.get(position);
                list1=dbSql.getTable();
                for(int i=0; i<list1.size(); i++){
                    if(tableName.equals(list1.get(i))){
                        varmi=true;
                    }
                }
                if(varmi){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("YKS Formüller");
                    builder.setMessage("Bu formülleri daha önce kaydettiniz.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.show();
                }
                else {
                    dbSql.addTable(tableName);
                    dowloadFormula();
                }
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(rvGeoList);

        rvGeoList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        searchView.setOnQueryTextListener(this);
        adapter.setClickListener(this);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void createSubjectList() {

        DatabaseReference dbFormula = db.getReference("geometri");
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
                    String subjectName = formulas.getValue(Formula.class).getKonuAdi();
                    if (!geoSubjectList.contains(subjectName)) {
                        geoSubjectList.add(subjectName);

                    }
                }
                for (int i = 4; i <= geoSubjectList.size(); i += 5) {
                    geoSubjectList.add(i,"");

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
        args.putString("ders", "geometri");
        args.putString("konu", geoSubjectList.get(position));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //fragment değişimi gerçekleştiriliyor.
        transaction.replace(R.id.container, fragment).commit();
//        //stackteki fragment sayısı
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
        for (String subject : geoSubjectList) {
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

    private void dowloadFormula(){
        DatabaseReference dbFormula = db.getReference("geometri");
        dbFormula.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot formulas : dataSnapshot.getChildren()) {
                    final String subjectName = formulas.getValue(Formula.class).getKonuAdi();
                    final String formulAdi =formulas.getValue(Formula.class).getFormulAdi();
                    final String resimURL=formulas.getValue(Formula.class).getResimurl();
                    if(subjectName.equals(tableName)){
                        httpsReference = storage.getReferenceFromUrl(resimURL);
                        httpsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                dbSql.addData(subjectName,formulAdi,bytes);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("YKS Formüller");
        builder.setMessage("Formülünüz kaydedildi. İndirilen Formüller bölümünde bulabilirsiniz.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getBaseContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isNetworkConnected()) {
            imgOffline.setVisibility(View.VISIBLE);
            txtOffline.setVisibility(View.VISIBLE);
            rvGeoList.setVisibility(View.GONE);
        }else{
            imgOffline.setVisibility(View.GONE);
            txtOffline.setVisibility(View.GONE);
            rvGeoList.setVisibility(View.VISIBLE);
        }
    }
}
