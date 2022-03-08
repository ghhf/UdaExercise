package com.happy.pets;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.happy.pets.data.PetContract;
import com.happy.pets.data.PetDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "CatalogActivity";
    private static final int PET_LOADER = 0;
    private PetCursorAdapter mCursorAdapter;

    private PetDbHelper mDbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.catalog_list);

        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        mCursorAdapter = new PetCursorAdapter(this,null,false);
        listView.setAdapter(mCursorAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetContract.CONTENT_URI,id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

//        mDbHelper = new PetDbHelper(this);
//        displayDatabaseInfo();
//        getLoaderManager().initLoader(PET_LOADER,null, this);
        LoaderManager.getInstance(this).initLoader(PET_LOADER,null,this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // EditorActivity 返回时 刷新列表数据
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPet();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME,"Toto");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED,"Terrier");
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, PetContract.PetEntry.GENDER_MALE);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT,7);

//        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);
//        Log.w(TAG,"New row ID " + newRowId);

        Uri newUri = getContentResolver().insert(PetContract.CONTENT_URI,contentValues);

        Log.w(TAG,"New row  " + newUri);

    }

    private void deleteAllPet(){
        int rowsDeleted = getContentResolver().delete(PetContract.CONTENT_URI, null, null);
    }

    private void displayDatabaseInfo(){
//        SQLiteDatabase db = mDbHelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + PetContract.PetEntry.TABLE_NAME, null);

        Cursor cursor = getContentResolver().query(PetContract.CONTENT_URI,null,null,null,null);

        // 设置查询条件，
        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};

        String selection = PetContract.PetEntry.COLUMN_PET_GENDER + "=?";

        String[] selectionArgs = new String[]{String.valueOf(PetContract.PetEntry.GENDER_FEMALE)};

//        Cursor c = db.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,
//                null,null,null);

        Cursor c = getContentResolver().query(PetContract.CONTENT_URI,projection,selection,selectionArgs,null);

        PetCursorAdapter adapter = new PetCursorAdapter(this,cursor,true);
        listView.setAdapter(adapter);


        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);
//        adapter.notifyAll();
//        tv.setText("NUmber " + cursor.getCount());
//        tv.append("\n" + PetContract.PetEntry._ID + " - "
//                + PetContract.PetEntry.COLUMN_PET_NAME + " - "
//                + PetContract.PetEntry.COLUMN_PET_BREED + " - "
//                + PetContract.PetEntry.COLUMN_PET_GENDER + " - "
//                + PetContract.PetEntry.COLUMN_PET_WEIGHT + "\n");

//        try {
//            int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
//            int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
//            int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
//            int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);
//
//            while (cursor.moveToNext()){
//                int currentId = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String currentBreed = cursor.getString(breedColumnIndex);
//                int currentGender = cursor.getInt(genderColumnIndex);
//                String gender = "unknown";
//                if(currentGender == PetContract.PetEntry.GENDER_FEMALE){
//                    gender = "female";
//                }else if(currentGender == PetContract.PetEntry.GENDER_MALE){
//                    gender = "male";
//                }
//                int currentWeight = cursor.getInt(weightColumnIndex);
////                tv.append("\n" + currentId + " - " + currentName+ " - " + currentBreed
////                        + " - " + gender + " - " + currentWeight);
//            }
//
//        }finally {
//            // 释放资源
//            c.close();
//        }

    }

    /**
     * 在后台线程运行 执行 Content Provider 的 query 方法
     * @param id
     * @param args
     * @return
     */
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        String[] projection = {
                PetContract.PetEntry._ID,
                PetContract.PetEntry.COLUMN_PET_BREED,
                PetContract.PetEntry.COLUMN_PET_NAME,
                PetContract.PetEntry.COLUMN_PET_WEIGHT};

        return new CursorLoader(this,
                PetContract.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}