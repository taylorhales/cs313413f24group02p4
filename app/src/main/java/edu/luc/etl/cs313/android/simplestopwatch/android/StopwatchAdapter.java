package edu.luc.etl.cs313.android.simplestopwatch.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.content.Context; // for accessing the application context
import android.media.AudioAttributes; // for configuring the audio playback attributes
import android.media.MediaPlayer; // for playing the notification sound
import android.media.RingtoneManager; // for accessing the default notification sound URI
import android.net.Uri;  // for handling the URI of the notification sound
import java.io.IOException; // for handling IOExceptions that might occur with MediaPlayer
import android.media.AudioManager; // for managing audio streams
import android.media.ToneGenerator;  // for generating simple tones for the beep

import java.util.Locale;

import edu.luc.etl.cs313.android.simplestopwatch.R;
import edu.luc.etl.cs313.android.simplestopwatch.common.Constants;
import edu.luc.etl.cs313.android.simplestopwatch.common.StopwatchModelListener;
import edu.luc.etl.cs313.android.simplestopwatch.model.ConcreteStopwatchModelFacade;
import edu.luc.etl.cs313.android.simplestopwatch.model.StopwatchModelFacade;

/**
 * A thin adapter component for the stopwatch.
 *
 * @author laufer
 */
public class StopwatchAdapter extends Activity implements StopwatchModelListener {

    private static String TAG = "stopwatch-android-activity";

    /**
     * The state-based dynamic model.
     */
    private StopwatchModelFacade model;

    protected void setModel(final StopwatchModelFacade model) {
        this.model = model;
    }

    /** Plays the default notification sound. */
    public void playDefaultNotification(){
        // get the URI for the default notification sound with RingtoneManager
        final Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // create new instance of MediaPlayer for playing the notification
        final MediaPlayer mediaPlayer = new MediaPlayer();

        // get application context for avoiding memory leaks with long-lived references
        final Context context = getApplicationContext();

        try{
            // set data source for the MediaPlayer with the default notification sound URI
            mediaPlayer.setDataSource(context, defaultRingtoneUri);

            // configure audio attributes for the MediaPlayer
            mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM) // the usage is for alarms
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)  // content type sound for UI feedback
                    .build());

            // prepare MediaPlayer for playback, synchronously loads the data for playback
            mediaPlayer.prepare();

            // set a listener to release MediaPlayer resources when playback is completed
            mediaPlayer.setOnCompletionListener(MediaPlayer::release);

            // start playing the alarm sound
            mediaPlayer.start();
        } catch (final IOException ex){
            // if an IOException occurs, wrap it in a RuntimeException and throw it
            throw new RuntimeException(ex);
        }
    }

    /** plays the beep sound that happens before decrementing */
    public void playBeep(){
        // ToneGenerator instance with specific stream type & volume
        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        // play a short beep tone
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
        // release ToneGenerator after a delay to ensure the tone completes
        new Handler(Looper.getMainLooper()).postDelayed(toneGenerator::release, 200);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inject dependency on view so this adapter receives UI events
        setContentView(R.layout.activity_main);
        // inject dependency on model into this so model receives UI events
        this.setModel(new ConcreteStopwatchModelFacade());
        // inject dependency on this into model to register for UI updates
        model.setModelListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        model.start();
    }

    // TODO remaining lifecycle methods

    /**
     * Updates the seconds and minutes in the UI.
     * @param time
     */
    public void onTimeUpdate(final int time) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView tvS = findViewById(R.id.seconds);
            final var locale = Locale.getDefault();
            tvS.setText(String.format(locale,"%02d", time % Constants.SEC_PER_MIN));
        });
    }

    /**
     * Updates the state name in the UI.
     * @param stateId
     */
    public void onStateUpdate(final int stateId) {
        // UI adapter responsibility to schedule incoming events on UI thread
        runOnUiThread(() -> {
            final TextView stateName = findViewById(R.id.stateName);
            stateName.setText(getString(stateId));
        });
    }

    // forward event listener methods to the model
    public void onAction(final View view) {
        model.onAction();
    }

    public void onLapReset(final View view)  {
        model.onLapReset();
    }

    public void onStartStop(View view) {
        model.onStartStop();
    }

    public void onDecrement(final View view) {model.onDecrement();}
}
