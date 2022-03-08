package com.happy.pets;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.happy.pets.data.PetContract;
import com.happy.pets.data.PetContract.PetEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "EditorActivity";

    /**
     * Identifier for the pet data loader
     */
    private static final int EXISTING_PET_LOADER = 0;

    /**
     * Content URI for the existing pet (null if it's a new pet)
     */
    private Uri mCurrentPetUri;

    /**
     * EditText field to enter the pet's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the pet's breed
     */
    private EditText mBreedEditText;

    /**
     * EditText field to enter the pet's weight
     */
    private EditText mWeightEditText;

    /**
     * EditText field to enter the pet's gender
     */
    private Spinner mGenderSpinner;

    /**
     * Gender of the pet. The possible values are:
     * 0 for unknown gender, 1 for male, 2 for female.
     */
    private int mGender = PetEntry.GENDER_UNKNOWN;

//    private PetDbHelper mDbHelper;

    /**
     * 监听用户是否修改了数据
     */
    private boolean mPetHasChanged = false;

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mPetHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();
        if (mCurrentPetUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_pet));
            // 新增时，不可进行删除操作
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_update_pet));
            LoaderManager.getInstance(this).initLoader(EXISTING_PET_LOADER, null, this);
        }
//        mDbHelper=new PetDbHelper(this);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        mNameEditText.setOnTouchListener(mOnTouchListener);
        mBreedEditText.setOnTouchListener(mOnTouchListener);
        mWeightEditText.setOnTouchListener(mOnTouchListener);
        mGenderSpinner.setOnTouchListener(mOnTouchListener);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE; // Female
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN; // Unknown
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN; // Unknown
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
//                insertPet();
                savePet();
                // 插入成功后 返回上个页面
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                // 如果没有改变 返回 CatalogActivity
                if(!mPetHasChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }else {
                    // 如果有未保存的改变，设置一个对话框，让用户点击处理放弃或继续
                    DialogInterface.OnClickListener discardButtonClickListener =
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                                }
                            };
                    showUnsavedChangesDialog(discardButtonClickListener);
                    return  true;
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
         super.onPrepareOptionsMenu(menu);
         if(mCurrentPetUri == null){
             MenuItem menuItem = menu.findItem(R.id.action_delete);
             menuItem.setVisible(false);
         }
        return true;
    }

    private void insertPet() {

        String name = mNameEditText.getText().toString().trim();
        String breed = mBreedEditText.getText().toString().trim();
        int weight = Integer.parseInt(mWeightEditText.getText().toString().trim());

        ContentValues contentValues = new ContentValues();
        contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, name);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, breed);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, mGender);
        contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, weight);

//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME,null,contentValues);
//
//        if(newRowId == -1){
//            Toast.makeText(this,"添加宠物失败，请重试",Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this,"添加宠物成功",Toast.LENGTH_SHORT).show();
//
//        }
//        Log.w(TAG,"New row ID " + newRowId);

        Uri newUri = getContentResolver().insert(PetContract.CONTENT_URI, contentValues);

        if (newUri == null) {
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful), Toast.LENGTH_SHORT).show();
        }
    }

    private void savePet(){
        String nameStr = mNameEditText.getText().toString();
        String breedStr = mBreedEditText.getText().toString();

        String weightStr = mWeightEditText.getText().toString();

        if (mCurrentPetUri == null &&
                TextUtils.isEmpty(nameStr) && TextUtils.isEmpty(breedStr) &&
                TextUtils.isEmpty(weightStr) && mGender == PetEntry.GENDER_UNKNOWN) {return;}

        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME,nameStr);
        values.put(PetEntry.COLUMN_PET_BREED,breedStr);
        values.put(PetEntry.COLUMN_PET_GENDER,mGender);

        int weight = 0;
        if (!TextUtils.isEmpty(weightStr)) {
            weight = Integer.parseInt(weightStr);
        }
        values.put(PetEntry.COLUMN_PET_WEIGHT,weight);

        if(mCurrentPetUri == null){
            Uri newUri = getContentResolver().insert(PetContract.CONTENT_URI,values);

            if(newUri == null){
                Toast.makeText(this,getString(R.string.editor_insert_pet_failed),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,getString(R.string.editor_insert_pet_successful),Toast.LENGTH_LONG).show();

            }
        }else {
            int rowsAffected = getContentResolver().update(mCurrentPetUri,values,null,null);

            if(rowsAffected == 0){
                Toast.makeText(this,getString(R.string.editor_update_pet_failed),Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,getString(R.string.editor_update_pet_successful),Toast.LENGTH_LONG).show();

            }
        }
    }

    @NonNull
    @Override
    public Loader onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        return new CursorLoader(this, mCurrentPetUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            String name = cursor.getString(nameColumnIndex);
            String breed = cursor.getString(breedColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            int weight = cursor.getInt(weightColumnIndex);

            mNameEditText.setText(name);
            mBreedEditText.setText(breed);
            mWeightEditText.setText(Integer.toString(weight));

            switch (gender) {
                case PetEntry.GENDER_FEMALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case PetEntry.GENDER_MALE:
                    mGenderSpinner.setSelection(2);
                    break;
                case PetEntry.GENDER_UNKNOWN:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        mNameEditText.setText("");
        mGenderSpinner.setSelection(0);
        mBreedEditText.setText("");
        mWeightEditText.setText("");
    }

    @Override
    public void onBackPressed() {
        if(!mPetHasChanged){
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonCLickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        };

        showUnsavedChangesDialog(discardButtonCLickListener);
    }

    /**
     * 未保存 返回/退出时 提示框
     */
    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 删除确认框
     */
    private void showDeleteConfirmationDialog(){
            // Create an AlertDialog.Builder and set the message, and click listeners
            // for the positive and negative buttons on the dialog.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.unsaved_changes_dialog_msg);
            builder.setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deletePet();
                    finish();
                }
            });
            builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            // Create and show the AlertDialog
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
    }

    private void deletePet(){
        if(mCurrentPetUri != null){
            int rowsDeleted = getContentResolver().delete(mCurrentPetUri,null,null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}