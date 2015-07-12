package glados.project.elec484.glados_me;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar;
import android.net.Uri;


public class GlaDOS_me_p2 extends Activity {
    private ImageButton playBtn;
    private Button processBtn;
    private  ImageButton cancelBtn;
    private int pitch1Change = 0;
    private int pitch2Change = 0;
    private byte[] audioSamples;

    //media recored and player
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gla_dos_me_p2);

        playBtn = (ImageButton)findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        processBtn = (Button)findViewById(R.id.process);
        processBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                process(v);
            }
        });

        cancelBtn = (ImageButton)findViewById(R.id.cancel);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancel(v);
            }
        });

        SeekBar pitchSeek1 = (SeekBar)findViewById(R.id.pitch1);
        final TextView pitchSeek1Value = (TextView)findViewById(R.id.pitch1Value);

        pitchSeek1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                pitch1Change = progress;
                pitchSeek1Value.setText(String.valueOf(pitch1Change));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        SeekBar pitchSeek2 = (SeekBar)findViewById(R.id.pitch2);
        final TextView pitchSeek2Value = (TextView)findViewById(R.id.pitch2Value);

        pitchSeek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                pitch2Change = progress;
                pitchSeek2Value.setText(String.valueOf(pitch2Change));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void cancel(View view) {
        Intent intent = new Intent(GlaDOS_me_p2.this, GlaDOS_me.class);
        startActivity(intent);
    }

    public void play(View view) {
        try{
            if (GlaDOS_me.CURRENT_AUDIO == GlaDOS_me.RECORDED_AUDIO){

                //start up media player
                myPlayer = new MediaPlayer();
                myPlayer.setDataSource(GlaDOS_me.recordedAudio);
                myPlayer.prepare();
                myPlayer.start();

                //button logic: turn off the play button, and enable the stop playing button
                playBtn.setEnabled(false);

                Toast.makeText(getApplicationContext(), "Start play the recording...",
                        Toast.LENGTH_SHORT).show();

            }
            else if (GlaDOS_me.CURRENT_AUDIO == GlaDOS_me.LOADED_AUDIO){

                //startup the media player
                myPlayer = new MediaPlayer();
                myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                myPlayer.setDataSource(getApplicationContext(), GlaDOS_me.loadedAudioFile);
                myPlayer.prepare();
                myPlayer.start();

                Toast.makeText(getApplicationContext(), "Playing audio...",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //This function does the processing of the specified audio file
    public void process(View view){
        if (GlaDOS_me.CURRENT_AUDIO == GlaDOS_me.RECORDED_AUDIO){

        }
        else if (GlaDOS_me.CURRENT_AUDIO == GlaDOS_me.LOADED_AUDIO){


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gla_dos_me_p2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
