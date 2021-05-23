package cn.happy.miwork;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaResource();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("red","weṭeṭṭi",
                R.drawable.color_red,R.raw.color_red));
        words.add(new Word("green","chokokki",
                R.drawable.color_green,R.raw.color_green));
        words.add(new Word("brown","ṭakaakki",
                R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi",
                R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black","kululli",
                R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white","kelelli",
                R.drawable.color_white,R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisə",
                R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭə",
                R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        WordAdapter itemsAdapter = new WordAdapter(this,words,R.color.category_colors);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 用户快速点击 无法监听到结束回调方法 所以在每次播放之前 释放资源
                releaseMediaResource();
                mediaPlayer = MediaPlayer.create(ColorsActivity.this,words.get(position).getMiwokAudio());

                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(completionListener);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaResource();
    }

    /**
     * 清空 MediaPlayer 来释放资源
     */
    private void releaseMediaResource(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            // 空出资源 去播放下一个音频
            mediaPlayer = null;
        }
    }
}