package com.example.midisound_vanilla;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import jp.kshoji.javax.sound.midi.UsbMidiSystem;

public class MainActivity extends AppCompatActivity {

    public boolean started = false;
    String mds1, mds2, mds3;
    Button StartBTN;
    TextView t1,t2,t3;

    private midiRecord md2;
    private midiRecord.RecordAudio recordTask;

    UsbMidiSystem usbMidiSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usbMidiSystem = new UsbMidiSystem(this);
        usbMidiSystem.initialize();

        StartBTN = findViewById(R.id.btn1);
        t1 = findViewById(R.id.tx1);
        t2 = findViewById(R.id.tx2);
        t3 = findViewById(R.id.tx3);

        md2= new midiRecord(this);




        StartBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(md2.started){
                    StartBTN.setText("Start");
                    md2.started=false;
                    recordTask.cancel(true);


                }else{
                    StartBTN.setText("Stop");
                    md2.started=true;
                    recordTask = md2.new RecordAudio();
                    recordTask.execute();
                }
                /*MidiThread2 thread = new MidiThread2();
                thread.start();*/
            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();

        if (usbMidiSystem != null) {
            usbMidiSystem.terminate();
        }
    }

}//class