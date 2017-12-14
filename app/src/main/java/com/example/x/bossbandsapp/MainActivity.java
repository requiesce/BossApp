package com.example.x.bossbandsapp;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{ //} implements SharedPreferences.OnSharedPreferenceChangeListener {

    private RefreshHandler mRedrawHandler = new RefreshHandler(); // UPDATED CLOCK

    MediaPlayer backgroundMusicPlayer, sound1, sound2, sound3;
    int[] musicArray = {R.raw.kisnou_newdescent}; // can be tweaked to take more tracks
    //ArrayList<MediaPlayer> musicArrayList = new ArrayList<>();
    Button button;
    TextClock clock;
    TextView textViewCounter, textClockSubtitle, textCounterSubtitle;
    private Handler handler;
    Typeface juraBold, juraMedium, juraLight;
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        juraBold = Typeface.createFromAsset(getAssets(), "Jura-Bold.ttf");
        juraMedium = Typeface.createFromAsset(getAssets(), "Jura-Medium.ttf");
        juraLight = Typeface.createFromAsset(getAssets(), "Jura-Light.ttf");

        mRedrawHandler.sleep(0); // UPDATED CLOCK

        // CHECK PREFERENCE CHANGE LISTENER
      //  loadPreferences();

        // BACKGROUND MUSIC PLAYER
        backgroundMusicPlayer = MediaPlayer.create(MainActivity.this, musicArray[0]);
        backgroundMusicPlayer.setLooping(true);

        // SOUNDS
        sound1 = MediaPlayer.create(this, R.raw.sound_yay);
        sound2 = MediaPlayer.create(this, R.raw.sound_magic);
        sound3 = MediaPlayer.create(this, R.raw.sound_scream);

        // OLD CLOCK START; OBSOLETE
        //clockStart();

        // COUNTDOWN
        countDownStart();

        // ANIMATED BUTTON
        button();


    }

    private class RefreshHandler extends Handler {  // UPDATED CLOCK
        @Override
        public void handleMessage(Message msg) {
            TimeZone timeZone = TimeZone.getTimeZone("UTC");
            Calendar cal = Calendar.getInstance(timeZone);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.UK);
            simpleDateFormat.setTimeZone(timeZone);
            String printClock = simpleDateFormat.format(cal.getTime());
            ((TextView) findViewById(R.id.textViewClockUpdated)).setText(printClock);

            sleep(1000);
        }
        private void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }



    // CREATE MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // INFLATE + POPULATE MENU
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // HANDLE MENU
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // RESPOND TO MENU ITEM PRESS
        switch (item.getItemId()) {
            case R.id.action_search:
                // OPEN SEARCH ALERT DIALOG
                searchFunction();
                return true;
            case R.id.action_maps:
                // OPEN MAPS ACTIVITY
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.action_music:
                // PLAY + PAUSE BACKGROUND MUSIC
                if (backgroundMusicPlayer.isPlaying())
                {
                    backgroundMusicPlayer.pause();
                }
                else
                {
                    backgroundMusicPlayer.start();
                }
                return true;
            case R.id.action_settings:
                // OPEN SETTINGS ACTIVITY
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // SEARCH
    public void searchFunction(){
        AlertDialog.Builder builder =  new AlertDialog.Builder(MainActivity.this); //  R.style.AlertDialogTheme);
        TextView text = new TextView(this);
        text.setText(getString(R.string.wiki));
        text.setTypeface(juraLight);
        text.setTextSize(13);
        text.setTextColor(Color.LTGRAY);
        builder.setCustomTitle(text);
        builder.setMessage("");
        searchInput = new EditText(this);
        searchInput.setTextColor(Color.LTGRAY);
        searchInput.setTypeface(juraMedium);
        builder.setView(searchInput);


        builder.setPositiveButton(getString(R.string.button_search), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = searchInput.getText().toString();


                // REMOVE BLANK LINE INPUT
                if (text.contains("\n")){
                    text = text.replace("\n", "").replace("\r", "");
                }

                // DISPLAY SEARCH QUERY FOR VIA TOAST
                if (!text.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.dialog_searching) + text, Toast.LENGTH_SHORT);

                    // SET TOAST TYPEFACE
                    LinearLayout toastLayout = (LinearLayout) toast.getView();
                    TextView toastTextView = (TextView) toastLayout.getChildAt(0);
                    toastTextView.setTypeface(juraBold);

                    // SET TOAST BACKGROUND COLOUR
                    View view = toast.getView();
                    view.setBackgroundColor(getResources().getColor(R.color.primaryDarkTrans));
                    //view.setMinimumWidth();
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
                    toast.show();
                    sound1.start();

                    // OPTION 1
                    // OPEN WEBVIEW IN APP VIA WEBSEARCHER ACTIVITY
                    Intent intent = new Intent(MainActivity.this, WebSearcher.class);
                    intent.putExtra("passedQuery", text);
                    startActivity(intent);
                }
                // OPTION 2
                // OPEN WEB BROWSER
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://runescape.wikia.com/wiki/Special:Search?search=" + text));
                //startActivity(browserIntent);
            }
        });
        builder.setNegativeButton(getString(R.string.button_cancel), new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.dialog_canceled), Toast.LENGTH_SHORT);
                View view = toast.getView();

                // SET TOAST BACKGROUND COLOUR
                view.setBackgroundColor(getResources().getColor(R.color.primaryDarkTrans));

                // SET TOAST TYPEFACE
                LinearLayout toastLayout = (LinearLayout) toast.getView();
                TextView toastTextView = (TextView) toastLayout.getChildAt(0);
                toastTextView.setTypeface(juraBold);

                // SET TOAST POSITION + BACKGROUND BEHAVIOUR
                toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);

                // SHOW TOAST
                toast.show();

                sound3.start();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();


        // CHANGE ALERT DIALOG BACKGROUND COLOUR
        alert.getWindow().setBackgroundDrawableResource(R.color.primaryDarkTrans);

        // CHANGE ALERT DIALOG BUTTON TYPEFACE
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(juraLight);
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(juraLight);

        // CHANGE ALERT DIALOG BUTTON TEXT COLOUR
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.LTGRAY);
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.LTGRAY);

        // ADD ALERT DIALOG BUTTON BACKGROUND COLOUR
        //alert.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources().getColor(R.color.primaryDarkTrans));
        //alert.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(getResources().getColor(R.color.primaryDarkTrans));

        // ADD ALERT DIALOG BUTTON PADDING
        //alert.getButton(AlertDialog.BUTTON_POSITIVE).setPadding(5,5,5,5);
        //alert.getButton(AlertDialog.BUTTON_NEGATIVE).setPadding(5,5,5,5);
    }

    // OLD CLOCK; OBSOLETE
    /*public void clockStart(){
        clock = (TextClock) findViewById(R.id.clock);
        clock.setTimeZone("UTC");
        clock.setFormat24Hour("hh:mm:ss");
        textClockSubtitle = (TextView) findViewById(R.id.textviewClock);
        textClockSubtitle.setTypeface(juraBold);
    }*/

    // COUNTDOWN
    public void countDownStart() {
        textCounterSubtitle = (TextView) findViewById(R.id.textviewCountdown);
        textCounterSubtitle.setTypeface(juraBold);
        handler = new Handler();
        Runnable runnable;
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 1000);
                Calendar calendar = new GregorianCalendar();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                int dayOfWeek = (calendar.get(Calendar.DAY_OF_WEEK)+ 2) % 7;
                int hourOfWeek = (calendar.get(Calendar.HOUR)) + (24 * dayOfWeek);
                int hTill = (6 - (hourOfWeek % 7) + 2) % 7;
                int mTill = (60 - calendar.get(Calendar.MINUTE));
                int sTill = (60 - calendar.get(Calendar.SECOND));

                // PREVENT 60 MINUTES / 60 SECONDS FROM DISPLAYING
                if (mTill == 60) {
                    hTill++;
                    mTill = 0;
                }
                if (sTill > 0) {
                    mTill--;
                }


                // FORMAT & DISPLAY COUNTDOWN IN TEXTVIEW
                String timestr = "";
                String hFormat = String.format(Locale.UK, "%02d", hTill);
                String mFormat = String.format(Locale.UK, "%02d", mTill);
                String sFormat = String.format(Locale.UK, "%02d", sTill);
                timestr = hFormat + ":" + mFormat + ":" + sFormat;
                textViewCounter = (TextView) findViewById(R.id.textView);
                textViewCounter.setText(timestr);

                // WHEN COUNTDOWN < 20 MINUTES
                if (hTill == 0 & mTill == 20 & sTill < 3) {
                    // FLASH TIME
                    Animation animation = new AlphaAnimation(1.0f, 0.0f);
                    animation.setDuration(500);
                    textViewCounter.startAnimation(animation);

                    // VIBRATE
                    Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(1000); // 1 SECOND

                    // SEND NOTIFICATION
                    sound2.start();
                    eventNotification(getString(R.string.notification_message_wbs_soon));
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }

    // EVENT NOTIFICATION
    public void eventNotification(String passedMessage){
        // ADD NOTIFICATION CHANNELS FOR OREO+
        // if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
        // }
        // else {
            // NOTIFICATIONS FOR EARLIER VERSIONS OF ANDROID
        Random randomGenerator = new Random();
        int randomiser = randomGenerator.nextInt(4) + 1;
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setDefaults((NotificationCompat.DEFAULT_ALL))
                .setSmallIcon(R.drawable.logo_clear);

        //////////////// FOR DEMO ONLY ////////////////
        sound2.start();
        // SET TO SAME IMG FOR DEMO
        if (randomiser == 1) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        }
        else if (randomiser == 2) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        }
        else if (randomiser == 3) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));}
        else {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        }
        ////////////////  ////////////////  ////////////////

        /*if (randomiser == 1) {
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
        }
        else if (randomiser == 2) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_domie));
        }
        else if (randomiser == 3) {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_kas));}
        else {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_proc));
        }*/
        notificationBuilder.setContentTitle(getString(R.string.notification_title_bossalert))
                .setContentText(passedMessage);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
        // }
    }

    // LOGIN BUTTON
    public void button(){
        button = (Button) findViewById(R.id.button);
        button.setTypeface(juraBold);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ALPHA ANIMATION
                Animation animationAlpha = new AlphaAnimation(1.0f, 0.0f);
                animationAlpha.setDuration(1000);
                Animation animationScale = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                button.startAnimation(animationAlpha);
                button.startAnimation(animationScale);

                // OPEN WEBVIEWER ACTIVITY
                Intent intent = new Intent(MainActivity.this, WebViewer.class);
                startActivity(intent);



                // TESTING PURPOSES //////////////// ////////////////

                // EVENT NOTIFICATION FOR DEMO ONLY ////////////////
                eventNotification(getString(R.string.notification_message_wbs_soon));

                // VIBRATE
                //Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                //vibrator.vibrate(1000);
            }
        });

    }


}