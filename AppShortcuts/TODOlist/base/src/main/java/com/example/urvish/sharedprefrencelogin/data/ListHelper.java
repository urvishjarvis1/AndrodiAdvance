package com.example.urvish.sharedprefrencelogin.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by urvish on 31/1/18.
 */

public class ListHelper extends SQLiteOpenHelper {
    @NonNull private static final String KEY_ID = "_id";
    @NonNull private static final String KEY_ITEM = "item";
    private static final String TAG = ListHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    @NonNull private static final String WORD_LIST_TABLE = "todo_entries";
    @NonNull private static final String DATABASE_NAME = "todo";
    @NonNull private static final String[] COLUMNS = {KEY_ID, KEY_ITEM};
    //table creation query
    @NonNull private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_ITEM + " TEXT );";
    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public ListHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase sqLiteDatabase) {
        //table creation only first time
        sqLiteDatabase.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(sqLiteDatabase);
    }
    //inserted dummy data...
    private void fillDatabaseWithData(@NonNull SQLiteDatabase db) {
        String item[] = {"to do:", "to do:", "to do:", "to do:", "to do:", "to do:", "to do:", "to do:"};
        ContentValues values = new ContentValues();
        for (int i = 0; i < item.length; i++) {
            values.put(KEY_ITEM, item[i]);
            db.insert(WORD_LIST_TABLE, null, values);
        }

    }



    //Read query
    @NonNull
    public ArrayList<TodoItem> query() {
        String query = "SELECT  * FROM " + WORD_LIST_TABLE +
                " ORDER BY " + KEY_ID + ";";
        Cursor cursor = null;
        int i = 0;
        int[] ids;
        String[] item;
        ArrayList<TodoItem> entry = new ArrayList<>();

        try {
                if (mReadableDB == null) {
                    mReadableDB = getReadableDatabase();
                }
                cursor = mReadableDB.rawQuery(query, null);
                cursor.moveToFirst();
                ids = new int[cursor.getCount()];
                item = new String[cursor.getCount()];
                do {

                    item[i] = cursor.getString(cursor.getColumnIndex(KEY_ITEM));
                    ids[i] = cursor.getInt(cursor.getColumnIndex(KEY_ID));

                    TodoItem todoItem=new TodoItem();
                    todoItem.setmId(ids[i]);
                    todoItem.setmItem(item[i]);
                    entry.add(todoItem);
                    i++;
                } while (cursor.moveToNext());


        } catch (Exception e) {
                Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
                cursor.close();
                return entry;
        }
    }




    //insert query
    public long insertData(String item){
         ContentValues content=new ContentValues();
         content.put(KEY_ITEM,item);
         long newId=0;
         try{
             if(mWritableDB==null){
                 mWritableDB=getWritableDatabase();
             }
            newId=mWritableDB.insert(WORD_LIST_TABLE,null,content);
         }catch (Exception e){
            Log.d(TAG,"Insert exception"+e);

         }
        return newId;
    }

    //getdatafor detailview
    @Nullable
    public String getdata(int pos){
        String query;
            query = "SELECT  * FROM " + WORD_LIST_TABLE +" WHERE _id="+pos+
                    " ORDER BY " + KEY_ID + " ASC ";
        String data=null;
        Cursor cursor=null;
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            data=(cursor.getString(cursor.getColumnIndex(KEY_ITEM)));
            Log.d("count",data);
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return data;
        }

    }
    public int update(String item,int itemid){
        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_ITEM,item);
        Log.d("count",Integer.toString(itemid));
        int id=0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            id=mReadableDB.update(WORD_LIST_TABLE,contentValues,KEY_ID+"=?",new String[]{String.valueOf(itemid)});
        }catch (Exception e){
            Log.d(TAG, "UPDATE QUERY EXCEPTION! " + e.getMessage());
        }
        return id;
    }
    
    //delete Query
    public int delete(int id){
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(WORD_LIST_TABLE,KEY_ID + " =? ", new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.w(ListHelper.class.getName(),
                "Upgrading database from version " + 1 + " to "
                        + 2 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(sqLiteDatabase);
    }
}
