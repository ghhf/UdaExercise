package com.happy.earthquake;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EarthquakeAdapter extends BaseAdapter {
    private List<EarthQuake> list = new ArrayList<>();
    private Context mContext;

    public EarthquakeAdapter(Context mContext, List<EarthQuake> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getUpdated();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        }

        EarthQuake curEarthQuake = (EarthQuake) getItem(position);

        /**
         * float 32位。如需在大型浮点数数组中节内存，请使用 float 而不是 double，此类型绝不用于精确值，如货币 可使用 BigDecimal
         * double 64位，对于十进制此数据类型通常是默认选择，此类型绝不用于精确值，如货币 可使用 BigDecimal
         */

        TextView tvMagnitude = listItemView.findViewById(R.id.magnitude);
        tvMagnitude.setText(formatMagnitude(curEarthQuake.getMag()));

        GradientDrawable magnitudeCircle = (GradientDrawable) tvMagnitude.getBackground();
        int magnitudeColor = getMagnitudeColor(curEarthQuake.getMag());
        magnitudeCircle.setColor(magnitudeColor);


        String place = curEarthQuake.getTitle();

        int ofIdx = place.indexOf("of");
        String location = place;
        String distance = "";
        if(ofIdx>0){
            location = place.substring(ofIdx+2,place.length()-1);
            distance = place.substring(0,ofIdx);
        }
        TextView tvDistance = listItemView.findViewById(R.id.distance);
        tvDistance.setText(distance);

        TextView tvLocation = listItemView.findViewById(R.id.location);
        tvLocation.setText(location);

        TextView tvDate = listItemView.findViewById(R.id.date);
        tvDate.setText(longTime2Date(curEarthQuake.getTime()));
        return listItemView;
    }

    private static String longTime2Date(long milliSeconds){
        Date dateObject = new Date(milliSeconds);
        // 格式 Mar 3,1984
        SimpleDateFormat dateFormatter = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormatter.format(dateObject);
    }

    private String formatMagnitude(double magnitude){
        // 0 表示数字的占位符、# 也表示 数字，但是不显示前导零
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);

    }
    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResId = R.color.magnitude1;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor){
            case 1:
                magnitudeColorResId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResId = R.color.magnitude9;
                break;
            default:
                break;
        }
        return ContextCompat.getColor(mContext, magnitudeColorResId);
    }

    public void addAll(List<EarthQuake> list){
        this.list = list;
    }

    public void clear(){
        this.list = null;
    }

}
