package cn.happy.miwork;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Array 数组  长度不可变，不是类, 不能通过方法修改或引用元素，可以存储基本数据类型和对象
 * ArrayList 数组列表  长度可变，是类， 可以通过方法修改或引用元素， 只能存储对象类型
 *
 */
public class NumbersActivity extends AppCompatActivity {

    private static final String TAG = NumbersActivity.class.getSimpleName();

    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releaseMediaResource();
        }
    };
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT){
                // temporary loss of audio focus
                // pause audio file
                Log.e(TAG,"暂时是失去焦点");
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);

            }else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                // 失去之后 重新获取焦点 Gain audio focus back again(after having lost it earlier)
                //resume playing the audio file
                mMediaPlayer.start();


            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                //permanent loss of audio focus
                //stop the MediaPlayer andr release resources
                releaseMediaResource();

            }else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                // temporary loss of audio focus, can 'duck" or lower volume if applicable
                // pause the audio file.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    AudioAttributes playbackAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build();

    @RequiresApi(api = Build.VERSION_CODES.O)
    AudioFocusRequest mAudioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(playbackAttributes)
            .setAcceptsDelayedFocusGain(true)
            .setOnAudioFocusChangeListener(mOnAudioFocusChangeListener)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one","lutti",
                R.drawable.number_one,R.raw.number_one));
        words.add(new Word("two","otiiko",
                R.drawable.number_two,R.raw.number_two));
        words.add(new Word("three","tolooksu",
                R.drawable.number_three,R.raw.number_three));
        words.add(new Word("four","oyyisa",
                R.drawable.number_four,R.raw.number_four));
        words.add(new Word("five","massokka",
                R.drawable.number_five,R.raw.number_five));
        words.add(new Word("six","temmoka",
                R.drawable.number_six,R.raw.number_six));
        words.add(new Word("seven","kenekaku",
                R.drawable.number_seven,R.raw.number_seven));
        words.add(new Word("eight","kawinta",
                R.drawable.number_eight,R.raw.number_eight));
        words.add(new Word("nine","wo'e",
                R.drawable.number_nine,R.raw.number_nine));
        words.add(new Word("ten","na'aacha",
                R.drawable.number_ten,R.raw.number_ten));

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        WordAdapter itemsAdapter = new WordAdapter(this,words,R.color.category_numbers);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaResource();

                mMediaPlayer = MediaPlayer.create(NumbersActivity.this,words.get(position).getMiwokAudio());

                int result;
                // Android 8.0 之后
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    result = mAudioManager.requestAudioFocus(mAudioFocusRequest);
                }else {
                    result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                            AudioManager.STREAM_MUSIC,
                            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                }

                if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
                    // 获取到播放焦点
//                    mAudioManager.registerMediaButtonEventReceiver(Remote);
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(completionListener);


                }

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
        if(mMediaPlayer != null){
            mMediaPlayer.release();
            // 空出资源 去播放下一个音频
            mMediaPlayer = null;

            //释放 Audio 资源
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                mAudioManager.abandonAudioFocusRequest(mAudioFocusRequest);
            }else {
                mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

            }
        }
    }
}