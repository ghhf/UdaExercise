package cn.happy.miwork;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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
        words.add(new Word("Where are you going?","minto wuksus",
                R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnə oyaase'nə",
                R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...",
                R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michəksəs?",
                R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.","kuchi achit",
                R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","əənəs'aa?",
                R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I’m coming.","həə’ əənəm",
                R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.","əənəm",
                R.raw.phrase_im_coming));
        words.add(new Word("Let’s go.","yoowutis",
                R.raw.phrase_lets_go));
        words.add(new Word("Come here.","ənni'nem",
                R.raw.phrase_come_here));
        WordAdapter itemsAdapter = new WordAdapter(this,words,R.color.category_phrases);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaResource();

                MediaPlayer mediaPlayer = MediaPlayer.create(PhrasesActivity.this,words.get(position).getMiwokAudio());
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