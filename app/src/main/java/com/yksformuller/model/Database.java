package com.yksformuller.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database  extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "formulDB";
    private static final String ID = "id";
    private static final String SUBJECT_NAME = "subject";
    private static final String FORMULA_NAME = "formul";
    private static final String IMAGE_URL = "url";
    private static final String TABLE_NAME = "formulList" ;
    private static final String TABLE_NAME_1= "subjectList";
    private static final String TABLE_NAME_2= "notlarim";
    private List<String> list;
    private List<DownloadData> formula;
    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT, "
                + FORMULA_NAME + " TEXT, "
                + IMAGE_URL    + " BLOB)";

        String createSubject = "CREATE TABLE "+ TABLE_NAME_1 +"("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT)";

        String createTable1 = "CREATE TABLE " + TABLE_NAME_2 + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SUBJECT_NAME + " TEXT, "
                + FORMULA_NAME + " TEXT, "
                + IMAGE_URL    + " BLOB)";

        db.execSQL(createTable);
        db.execSQL(createSubject);
        db.execSQL(createTable1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addData(String subjectName, String formulaName, byte [] resimURL){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBJECT_NAME, subjectName);
            cv.put(FORMULA_NAME, formulaName);
            cv.put(IMAGE_URL, resimURL);
            db.insert(TABLE_NAME, null,cv);
        }catch (Exception e){

        }
        db.close();
    }

    public void addTable(String subjectName){
        SQLiteDatabase db =this.getWritableDatabase();
        try{
           ContentValues cv=new ContentValues();
           cv.put(SUBJECT_NAME,subjectName);
           db.insert(TABLE_NAME_1,null,cv);
        }
        catch (Exception e){

        }
        db.close();
    }

    public void addNot(String subjectName, String formulaName, byte [] resimURL){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SUBJECT_NAME, subjectName);
            cv.put(FORMULA_NAME, formulaName);
            cv.put(IMAGE_URL, resimURL);
            db.insert(TABLE_NAME_2, null,cv);
        }catch (Exception e){

        }
        db.close();
    }

    public boolean isEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master where tbl_name = '"+TABLE_NAME_1+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public List<String> getTable(){
        list=new ArrayList<String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<DownloadData> getFormula(String sb){
        formula = new ArrayList<DownloadData>();
        DownloadData downloadData=new DownloadData();
        String selectQuery = "SELECT * FROM formulList WHERE subject='"+ sb + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            downloadData.setSubjectName(cursor.getString(cursor.getColumnIndex(SUBJECT_NAME)));
            downloadData.setFormulaName(cursor.getString(cursor.getColumnIndex(FORMULA_NAME)));
            downloadData.setImageURL(cursor.getBlob(cursor.getColumnIndex(IMAGE_URL)));
            formula.add(downloadData);
            downloadData=null;
            while (cursor.moveToNext()){
                downloadData=new DownloadData();
                downloadData.setSubjectName(cursor.getString(cursor.getColumnIndex(SUBJECT_NAME)));
                downloadData.setFormulaName(cursor.getString(cursor.getColumnIndex(FORMULA_NAME)));
                downloadData.setImageURL(cursor.getBlob(cursor.getColumnIndex(IMAGE_URL)));
                formula.add(downloadData);
            }
        }
        db.close();
        return formula;
    }

    public List<DownloadData> getNot(){
        formula = new ArrayList<>();
        DownloadData downloadData=new DownloadData();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_2;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            downloadData.setSubjectName(cursor.getString(cursor.getColumnIndex(SUBJECT_NAME)));
            downloadData.setFormulaName(cursor.getString(cursor.getColumnIndex(FORMULA_NAME)));
            downloadData.setImageURL(cursor.getBlob(cursor.getColumnIndex(IMAGE_URL)));
            formula.add(downloadData);
            downloadData=null;
            while (cursor.moveToNext()){
                downloadData=new DownloadData();
                downloadData.setSubjectName(cursor.getString(cursor.getColumnIndex(SUBJECT_NAME)));
                downloadData.setFormulaName(cursor.getString(cursor.getColumnIndex(FORMULA_NAME)));
                downloadData.setImageURL(cursor.getBlob(cursor.getColumnIndex(IMAGE_URL)));
                formula.add(downloadData);
            }
        }
        db.close();
        return formula;
    }

    public void deleteFormulList(String sb){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME,"subject=?",new String[]{sb});
            db.delete(TABLE_NAME_1,"subject=?",new String[]{sb});
        }catch (Exception e){
        }
        db.close();
    }

    public void deleteNotList(String fl){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME_2,"formul=?",new String[]{fl});
        }catch (Exception e){
        }
        db.close();
    }

    public void deleteNote(){
        SQLiteDatabase db =this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME_2,null,null);
        }
        catch (Exception e){

        }
        db.close();
    }
}
