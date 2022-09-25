package com.example.voice_assistance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;

    private FloatingActionButton fab_mic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab_mic = findViewById(R.id.mic_fab);

        fab_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micFabClicked();
            }
        });

        initializeTextToSpeech();
        initializeSpeechRecognizer();

    }


    private void micFabClicked() {

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        } else {
            // Permission has already been granted
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            speechRecog.startListening(intent);
        }
    }

    private void initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecog = SpeechRecognizer.createSpeechRecognizer(this);
            speechRecog.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result_arr = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    processResult(result_arr.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void processResult(String result_message) {
        result_message = result_message.toLowerCase();

        if (result_message.contains("what")) {
            if (result_message.contains("is your name")) {
                speak("My Name Is Ropo. Nice to meet you!");
            } else if (result_message.contains("current time")) {
                String time_now = DateUtils.formatDateTime(this, new Date().getTime(), DateUtils.FORMAT_SHOW_TIME);
                speak("The time is now: " + time_now);
            } else if (result_message.contains("can you do")) {
                speak("I am here to entertain you");
            } else if (result_message.contains("hacktober") || result_message.contains("hacktoberfest")) {
                String strHack = "Hacktoberfest is a month-long celebration of open source software run by DigitalOcean in partnership with GitHub and Twilio. Hacktoberfest is open to everyone in our global community!";
                speak(strHack);
                Toast.makeText(this, strHack, Toast.LENGTH_LONG).show();
            } else if (result_message.contains("earth")) {
                speak("The earth is a sphere. As are all other planets and celestial bodies");
            } else {
                String s1 = result_message;
                Toast.makeText(this, s1, Toast.LENGTH_SHORT).show();
                speak("Sorry I didn't understand your point");
            }

        }
        else if (result_message.contains("hello") || result_message.contains("hi") || result_message.contains("hey")) {
            speak("Hello Master, I am here to entertain you");
        }
        else if (result_message.contains("browser")) {
            speak("Opening a browser right away master.");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
            startActivity(intent);
        } else if (result_message.contains("open instagram")) {
            speak("Opening Instagram");
            Intent intent17 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://Instagram.com/"));
            startActivity(intent17);
        } else if (result_message.contains("open facebook")) {
            speak("Opening Facebook");
            Intent intent9 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"));
            startActivity(intent9);
        } else if (result_message.contains("who are you")) {
            speak("your personal assistance is here");
        } else if (result_message.contains("open browser")) {
            speak("Opening a browser right away master.");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
            startActivity(intent);
        } else {
            String s1 = result_message;
            Toast.makeText(this, s1, Toast.LENGTH_SHORT).show();
            speak("Sorry I didn't understand your point");
        }
    }

    private void initializeTextToSpeech() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (tts.getEngines().size() == 0) {
                    Toast.makeText(MainActivity.this, getString(R.string.tts_no_engines), Toast.LENGTH_LONG).show();
                    finish();
                } else {

                    tts.setLanguage(Locale.US);
                    speak("Lets Start our conversation");
                }
            }
        });
    }

    private void speak(String message) {
        if (Build.VERSION.SDK_INT >= 21) {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


}