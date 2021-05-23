package cn.happy.miwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.List;

public class WordAdapter extends ArrayAdapter<Word> {

    /**Resource Id for the background color to this list of words */
    private int mColorResourceId = R.color.category_numbers;

    public WordAdapter(@NonNull Context context,  @NonNull List<Word> objects, int colorResourceId) {
        super(context, 0, objects);
        this.mColorResourceId = colorResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemvVew = convertView;
        if(listItemvVew == null){
            listItemvVew = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        Word curWord = getItem(position);
        TextView miwokTv = listItemvVew.findViewById(R.id.miwok_text_view);
        miwokTv.setText(curWord.getMiwokTranslation());

        TextView defaultTv = listItemvVew.findViewById(R.id.default_text_view);
        defaultTv.setText(curWord.getDefaultTranslation());

        LinearLayout linearLayout = listItemvVew.findViewById(R.id.word_container);
        int color = ContextCompat.getColor(getContext(), mColorResourceId);

        linearLayout.setBackgroundColor(color);

        ImageView image = listItemvVew.findViewById(R.id.image);

        if(curWord.hasImage()){
            image.setVisibility(View.VISIBLE);
            image.setImageResource(curWord.getImageResourceId());
        }else {
            image.setVisibility(View.GONE);
        }

        return listItemvVew;
    }
}
