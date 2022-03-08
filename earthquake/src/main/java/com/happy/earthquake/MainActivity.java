package com.happy.earthquake;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;


/**
 * List ArrayList 区别：
 *  List 是接口
 *  ArrayList 是具体的类
 *
 *  异步任务 AsyncTask 已经弃用， 现在使用 Loader 进行多线程
 *  LoaderManager 管理一个或多个实例与Activity/Fragment 互动
 *
 *  onCreateLoader()方法 创建和返回新的Loader实例
 *  onLoadFinished()方法 接收loader加载完成的数据
 *
 *  AsyncTaskLoader
 *
 * 测试场景：
 *  旋转设备
 *  转至主屏幕并返回到应用
 *  按返回按钮
 *  打开近期任务
 *  切换到不同的应用
 *  返回到应用
 *  走进自己的场景！
 *
 *
 * 地震数据 API：
 *  http://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson
 *
 *  http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmag=6&limit=10
 *
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    /**
     * 地震 loader ID 的常量值。我们可选择任意整数。
     * 仅当使用多个 loader 时该设置才起作用。
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-05-02&minfelt=50&minmagnitude=5";

    private EarthquakeAdapter adapter;
    private TextView emptyView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         emptyView = findViewById(R.id.empty_view);
         listView = findViewById(R.id.list);
        // Perform the HTTP request for earthquake data and process the response.
//        EarthQuake earthquake = Utils.fetchEarthquakeData(USGS_REQUEST_URL);

        QueryUtils.extractEarthquakes();

//        ArrayList<String> earthquakes = new ArrayList<>();
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");
//        earthquakes.add("San Francisco");

//        final ArrayList<EarthQuake> earthquakes = QueryUtils.extractEarthquakes();

        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
        earthquakeAsyncTask.execute(USGS_REQUEST_URL);

//        LoaderManager.getInstance(this).initLoader(EARTHQUAKE_LOADER_ID,null,this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 更新数组列表
     * @param earthquakes
     */
    private void updateUi(final List<EarthQuake> earthquakes){
        adapter = new EarthquakeAdapter(this,earthquakes);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake curEarthquake = earthquakes.get(position);

                // 将字符串 URL 转换为 URI 对象（以传递至 Intent 中 constructor)
                Uri earthquakeUri = Uri.parse(curEarthquake.getUrl());

                if(earthquakeUri != null){
                    // 创建一个新的 Intent 以查看地震 URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                    // 发送 Intent 以启动新活动
                    startActivity(websiteIntent);
                }else {
                    Toast.makeText(MainActivity.this,getResources()
                            .getText(R.string.app_name),Toast.LENGTH_SHORT);
                }


            }
        });
    }

    @NonNull
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
        // 取出设置中的地震等级数
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<EarthQuake>> loader, List<EarthQuake> earthquakes) {

        // 因数据已加载，隐藏加载指示符
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // 清除之前地震数据的适配器
        adapter.clear();

        emptyView.setText(getResources().getString(R.string.no_earthquakes));

        // 如果存在 {@link Earthquake} 的有效列表，则将其添加到适配器的
        // 数据集。这将触发 ListView 执行更新。

        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<EarthQuake>> loader) {
        adapter.clear();
    }


    /**
     * AsyncTask 用于后台线程执行网络请求
     * param 进度 返回值
     */
//    private class EarthquakeAsyncTask extends AsyncTask<String,Void,EarthQuake>{
//        // 获取数据
//        @Override
//        protected EarthQuake doInBackground(String... strings) {
//            if(strings.length < 1 || strings[0] == null){
//                return null;
//            }
//
//            EarthQuake earthQuake = Utils.fetchEarthquakeData(strings[0]);
//            return earthQuake;
//        }
//
//        @Override
//        protected void onPostExecute(EarthQuake earthQuake) {
//            if(earthQuake == null){
//                return ;
//            }
//            // gengxinUI
////           updateUi(earthQuake);
//        }
//    }
    private class EarthquakeAsyncTask extends AsyncTask<String,Void, List<EarthQuake>>{
        // 获取数据
        @Override
        protected List<EarthQuake> doInBackground(String... strings) {
            if(strings.length < 1 || strings[0] == null){
                return null;
            }

           return Utils.fetchEarthquakeList(strings[0]);
        }

        @Override
        protected void onPostExecute(List<EarthQuake> earthQuakes) {
            // gengxinUI
           updateUi(earthQuakes);
        }
    }

}
