package com.example.lcdpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.lcdpro.Microphone.MicrophoneInputStream;
import com.example.lcdpro.SpeakerVerification.Contract.CreateProfileException;
import com.example.lcdpro.SpeakerVerification.Contract.EnrollmentException;
import com.example.lcdpro.SpeakerVerification.Contract.Verification.Enrollment;
import com.example.lcdpro.SpeakerVerification.Contract.Verification.Verification;
import com.example.lcdpro.SpeakerVerification.Contract.Verification.VerificationException;
import com.example.lcdpro.SpeakerVerification.SpeakerVerificationRestClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String SpeechSubscriptionKey = "d0bb19dcf34841f0bf5bebdf64f6687b";
    UUID profileId;

    private Button recognizeButton;
    private Button recognizeIntermediateButton;

    private MicrophoneInputStream microphoneStream;
    private MicrophoneInputStream createMicrophoneStream() {
        if (microphoneStream != null) {
            try {
                microphoneStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            microphoneStream = null;
        }

        microphoneStream = new MicrophoneInputStream();
        return microphoneStream;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recognizeButton = findViewById(R.id.buttonRecognize);
        recognizeIntermediateButton = findViewById(R.id.buttonRecognizeIntermediate);

        SpeakerVerificationRestClient sv = new SpeakerVerificationRestClient("c4a428d44c074a639e911951deb3e4f1");

        recognizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    profileId = sv.createProfile("en-us").verificationProfileId;

                    Log.d("ttt", "profile id: " + profileId.toString());

                    InputStream input = createMicrophoneStream();
                    Enrollment enrollment = sv.enroll(input , profileId);

                    Log.d("ttt", "Enrollment: " + enrollment.enrollmentsCount);

                } catch (CreateProfileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (EnrollmentException e) {
                    e.printStackTrace();
                }
            }
        });

        recognizeIntermediateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream input = createMicrophoneStream();
                try {
                    Verification verification = sv.verify(input , profileId );

                    Log.d("ttt", "verification: " + verification.result.toString());
                } catch (VerificationException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
