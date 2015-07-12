package glados.project.elec484.glados_me;

import java.io.IOException;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class GlaDOS_me extends Activity {
    //media recored and player
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;

    //path for recorded audio file
    public static String recordedAudio = null;
    //URI for the loaded audio file
    public static Uri loadedAudioFile = null;

    //buttons and text
    private Button startBtn;
    private Button stopBtn;
    private ImageButton playBtn;
    private ImageButton stopPlayBtn;
    private ImageButton processPageBtn;
    private Button browseBtn;

    private TextView text;

    public static int CURRENT_AUDIO = 0x00;

    public static int RECORDED_AUDIO = 0x01;
    public static int LOADED_AUDIO = 0x02;

    public static final String TAG = "StorageClientFragment";

    // A request code's purpose is to match the result of a "startActivityForResult" with
    // the type of the original request.  Choose any value.
    private static final int READ_REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gla_dos_me);

        text = (TextView) findViewById(R.id.text1);


        browseBtn = (Button)findViewById(R.id.browse);
        browseBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                browse(v);
            }
        });


        startBtn = (Button)findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // store it to sd card
                myRecorder = new MediaRecorder();
                myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                recordedAudio = Environment.getExternalStorageDirectory().
                        getAbsolutePath() + "/GlaDOS_ME.3gpp";
                myRecorder.setOutputFile(recordedAudio);
                start(v);
            }
        });

        stopBtn = (Button)findViewById(R.id.stop);
        stopBtn.setEnabled(false);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
            }
        });

        playBtn = (ImageButton)findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        stopPlayBtn = (ImageButton)findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
            }
        });

        processPageBtn = (ImageButton)findViewById(R.id.next);
        processPageBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                processPage(v);
            }
        });
    }
    public void browse(View view){

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a file (as opposed to a list
        // of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers, it would be
        // "*/*".
        intent.setType("audio/*");
        startActivityForResult(intent, READ_REQUEST_CODE);

        CURRENT_AUDIO = LOADED_AUDIO;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.
        myPlayer = new MediaPlayer();

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"
            if (resultData != null) {
                loadedAudioFile = resultData.getData();
            }
        }
    }

    public void start(View view){
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        text.setText("GlaDOS Status: Recording audio");
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        browseBtn.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stop(View view){
        try {
            //stop the recored
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            //button logic
            playBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            startBtn.setEnabled(true);
            browseBtn.setEnabled(true);

            text.setText("GlaDOS Status: Stopping recording");

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();

            CURRENT_AUDIO = RECORDED_AUDIO;

        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }

    public void play(View view) {
        try{
            if (CURRENT_AUDIO == RECORDED_AUDIO){

                //start up media player
                myPlayer = new MediaPlayer();
                myPlayer.setDataSource(recordedAudio);
                myPlayer.prepare();
                myPlayer.start();

                //button logic: turn off the play button, and enable the stop playing button
                playBtn.setEnabled(false);
                stopPlayBtn.setEnabled(true);

                text.setText("GlaDOS Status: Playing audio");

                Toast.makeText(getApplicationContext(), "Start play the recording...",
                        Toast.LENGTH_SHORT).show();

            }
            else if (CURRENT_AUDIO == LOADED_AUDIO){

                //startup the media player
                myPlayer = new MediaPlayer();
                myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                myPlayer.setDataSource(getApplicationContext(), loadedAudioFile);
                myPlayer.prepare();
                myPlayer.start();

                //button logic: turn off the play button, and enable the stop playing button
                playBtn.setEnabled(false);
                stopPlayBtn.setEnabled(true);
                text.setText("GlaDOS Status: Playing audio");

                Toast.makeText(getApplicationContext(), "Playing audio...",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;

                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(false);
                startBtn.setEnabled(true);

                text.setText("GlaDOS Status: Stopping audio playback");

                Toast.makeText(getApplicationContext(), "Stopping audio playback...",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void processPage(View view)
    {
        Intent intent = new Intent(GlaDOS_me.this, GlaDOS_me_p2.class);
        startActivity(intent);
    }
}

