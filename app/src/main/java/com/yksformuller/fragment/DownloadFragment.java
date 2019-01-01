package com.yksformuller.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
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

import com.yksformuller.Interface.ItemClickListener;
import com.yksformuller.R;
import com.yksformuller.adapter.DownloadAdapter;
import com.yksformuller.model.Database;
import com.yksformuller.model.SwipeController;
import com.yksformuller.model.SwipeControllerActions;

import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends Fragment implements View.OnClickListener,ItemClickListener {

    private Database db;
    private RecyclerView rvDownload;
    private DownloadAdapter adapter;
    private List<String> list;
    private Fragment fragment=null;
    private String fragment_name;
    private SwipeController swipeController=null;
    private Bundle args;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        this.db=new Database(getActivity());
        if(db.isEmpty()){
            list=db.getTable();
            list.add("Notlarım");
        }
        else{
            list=null;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_download, parent, false);
        if(list!=null){
            rvDownload=(RecyclerView)view.findViewById(R.id.downloadList);
            adapter=new DownloadAdapter(getActivity(),list);
            rvDownload.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvDownload.setLayoutManager(linearLayoutManager);
            adapter.setClickListener(this);
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

            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvDownload);
            swipeController = new SwipeController("SİL",new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    if(list.get(position).equals("Notlarım")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("YKS Formüller");
                        builder.setMessage("Tüm notlarınızı silmek istediğinizden emin misiniz?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.deleteNote();
                            }
                        });
                        builder.setNegativeButton("HAYIR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                    else{
                        db.deleteFormulList(list.get(position));
                        list.remove(position);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("YKS Formüller");
                        builder.setMessage("Silme işleminiz başarıyla gerçekleşti.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.show();
                    }
                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(rvDownload);

            rvDownload.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(View view, int position) {
        fragment = new SubjectFragment();
        fragment_name = "Konular";
        args = new Bundle();
        args.putString("ders", "download");
        args.putString("konu", list.get(position));
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
}
