package cn.happy.miwork;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ColorsFragment() {
        // Required empty public constructor
    }

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
                Log.e("NumberFragment","暂时失去焦点");
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
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NumberFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorsFragment newInstance(String param1, String param2) {
        ColorsFragment fragment = new ColorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

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

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter itemsAdapter = new WordAdapter(getActivity(),words,R.color.category_colors);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaResource();

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

                    mMediaPlayer = MediaPlayer.create(getActivity(),words.get(position).getMiwokAudio());

                    // 获取到播放焦点
//                    mAudioManager.registerMediaButtonEventReceiver(Remote);
                    mMediaPlayer.start();
                    mMediaPlayer.setOnCompletionListener(completionListener);
                }

            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
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