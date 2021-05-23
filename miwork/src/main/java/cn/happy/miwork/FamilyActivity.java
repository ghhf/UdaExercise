package cn.happy.miwork;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

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
        words.add(new Word("father","əpə",
                R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother","əṭa",
                R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("son","angsi",
                R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter","tune",
                R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("older brother","taachi",
                R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("younger brother","chalitti",
                R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("older sister","teṭe",
                R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("younger sister","kolliti",
                R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("grandmother","ama",
                R.drawable.family_grandfather,R.raw.family_grandfather));
        words.add(new Word("grandfather","paapa",
                R.drawable.family_grandmother,R.raw.family_grandmother));

        WordAdapter itemsAdapter = new WordAdapter(this,words,R.color.category_family);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaResource();

                MediaPlayer mediaPlayer = MediaPlayer.create(FamilyActivity.this,words.get(position).getMiwokAudio());
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