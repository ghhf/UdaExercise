package com.happy.earthquake;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Utils {
    public static final String TAG = Utils.class.getSimpleName();


    public static List<EarthQuake> fetchEarthquakeList(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        jsonResponse = makeHttpRequest(url);

        return extractDataFromJson(jsonResponse);

    }

    public static EarthQuake fetchEarthquakeData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        jsonResponse = makeHttpRequest(url);

        EarthQuake earthQuake = extractFeatureFromJson(jsonResponse);

        return earthQuake;
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url){
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.setConnectTimeout(1500);
            httpURLConnection.connect();

            if(httpURLConnection.getResponseCode() == 200){
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(TAG,"response==="+ httpURLConnection.getResponseCode()+"");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
        }

        if(inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }

    private static EarthQuake extractFeatureFromJson(String earthquakeJSON){
        if(TextUtils.isEmpty(earthquakeJSON)){
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");
            if(featureArray.length() > 0){
                JSONObject firstFeature = featureArray.getJSONObject(0);
                JSONObject properties = firstFeature.getJSONObject("properties");
                String title = properties.getString("title");
                long time = properties.getLong("time");
                int tsunamiAlert = properties.getInt("tsunami");
                EarthQuake earthQuake = new EarthQuake();
                earthQuake.setTime(time);
                earthQuake.setTitle(title);
                earthQuake.setTsunami(tsunamiAlert);
                return earthQuake;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static List<EarthQuake> extractDataFromJson(String result){
        List<EarthQuake> earthQuakes = new ArrayList<>();
        if(TextUtils.isEmpty(result)){
            return null;
        }
        try{
            JSONObject baseJsonResponse = new JSONObject(result);
            JSONArray featureArray = baseJsonResponse.getJSONArray("features");
            if(featureArray.length() > 0){
                for(int i = 0; i < featureArray.length(); i++){
                    JSONObject firstFeature = featureArray.getJSONObject(i);
                    JSONObject properties = firstFeature.getJSONObject("properties");
                    String title = properties.getString("title");
                    long time = properties.getLong("time");
                    int tsunamiAlert = properties.getInt("tsunami");

                    EarthQuake earthQuake = new EarthQuake();
                    earthQuake.setTime(time);
                    earthQuake.setTitle(title);
                    earthQuake.setTsunami(tsunamiAlert);

                    earthQuakes.add(earthQuake);
                }

            }

        }catch(JSONException e){
            e.printStackTrace();

        }
        return earthQuakes;
    }
    /**
     * 把得到的 JSON InputStream 数据转为字符串
     *
     * InputStream Represents a stream of bytes(small chunks of data)
     * BufferedReader  Help us read text from an InputStream.帮助我们从InputStream中读取文本
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder sBuilder = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                sBuilder.append(line);
                line = reader.readLine();
            }
        }
        return sBuilder.toString();
    }
}
