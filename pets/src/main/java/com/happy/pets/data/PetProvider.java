package com.happy.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * ContentProvider
 *
 * URI 三个部分：scheme、content 主机名和数据类型
 *
 * pets 表信息：content://com.happy.pets/pets
 *
 * pets 表中某一行数据：content://com.happy.pets/pets/1
 */
public class PetProvider extends ContentProvider {

    public static final String LOG_TAG = PetProvider.class.getSimpleName();

    private static final int PETS = 100;
    private static final int PET_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS,PETS);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY,PetContract.PATH_PETS + "/#",PET_ID);
    }

    private PetDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetContract.PetEntry.TABLE_NAME,projection,selection,selectionArgs,
                        null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        // 数据发生变化通知监听器
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    /**
     * @param uri
     * @return 返回描述输入 URI 中存储的数据类型的字符串，该字符串事 MIME 类型，也称为 内容类型
     *
     * 在我们的应用中，基本上有两种类型的 URI。
     * 第一种 URI 为“content://com.example.android.pets/pets/”，指代整个 pets 表。
     * 它代表整个宠物列表。用 MIME 类型来说，这称为数据目录。
     *
     * 第二种 URI 是“content://com.example.android.pets/pets/#”，
     * 它代表单个宠物。用 MIME 类型来说，单个数据行即单个数据项。
     */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return PetContract.PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not support for " + uri );
        }
    }

    private Uri insertPet(Uri uri,ContentValues values){
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // 数据完整性坚持
        String name = values.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer gender = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
        if (gender == null || !PetContract.PetEntry.isValidGender(gender)) {
            throw new IllegalArgumentException("Pet requires a gender");
        }

        // 如果重量为空，没关系，我们可以继续进行插入（数据库会自动插入默认重量 0
        // 如果重量不为空，而为负值，那么我们需要抛出一个异常
        Integer weight = values.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires a weight");
        }

        long id = database.insert(PetContract.PetEntry.TABLE_NAME,null,values);

        if(id == -1){
            Log.e(LOG_TAG,"Failed to insert row for " + uri);
            return null;
        }
        // 通知监听器 数据发生了变化
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // DELETE pets WHERE breed= ‘Calico’
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int deleteResult ;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case PETS:
                deleteResult = database.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                if(deleteResult != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return deleteResult;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "+=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                deleteResult = database.delete(PetContract.PetEntry.TABLE_NAME,selection,selectionArgs);
                if(deleteResult != 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return deleteResult;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // UPDATE pets SET name = ‘Milo’, breed=’French bulldog’, weight=20 WHERE name=‘Toto’
        final int match = sUriMatcher.match(uri);
        int updateResult ;
        switch (match){
            case PETS:
                updateResult = updatePet(uri,values,selection,selectionArgs);
                if (updateResult != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return updateResult;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                updateResult = updatePet(uri,values,selection,selectionArgs);

                if (updateResult != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return updateResult;

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }

    }

    private int updatePet(Uri uri, ContentValues contentValues,String selection,String[] selectionArgs){
        // 没有需要更新的值 不要进行更新操作
        if(contentValues.size() == 0){
            return 0;
        }

        // 检查更新数据是否合法
        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)){
            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if(name == null){
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)){
            Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
            if(gender == null || !PetContract.PetEntry.isValidGender(gender)){
                throw new IllegalArgumentException("Pet requires a gender");
            }
        }

        if(contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)){
            Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);
            if(weight != null && weight < 0){
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }


        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        return database.update(PetContract.PetEntry.TABLE_NAME,contentValues,selection,selectionArgs);

    }
}
