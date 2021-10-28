# Android-MIDI-Recognizer
안드로이드 폰에서 전자피아노 MIDI Output을 입력받는 어플리케이션 응용하면 리듬게임, DDR, 신디사이저등에 활용가능

안드로이드에서도 midi 라이브러리가 있긴한데 java eclipse에서 만들던걸 그대로 가져오기위해서 javax.sound를 따로 구해서 사용함

https://source.android.com/devices/audio/midi?hl=ko

https://github.com/philburk/android-midisuite

위 깃헙 주소가 진짜 정리가 잘되있는데 너무 어려워서 코드가 쉬운 javax.sound.midi 불러와서 사용함 

# How To Use

사용하기 위해선 요런 케이블이 필요함 사진은 usb midi - c타입 케이블 

![image](https://user-images.githubusercontent.com/66546156/132150207-02d33f98-be59-40e2-92bf-284912a17670.png)

집에서 굴러다니는 usb midi 케이블(프린터 케이블과 동일함 usb b cable이라고 불리는듯)과 usb type c gender 연결해서 사용 

Galaxy s10 으로는 잘 호환됨 피아노는 CASIO CDP-S90


kshoji 님의 usb-midi-driver, android용 javax.sound,midi 를 사용

https://github.com/kshoji/USB-MIDI-Driver

https://github.com/kshoji/javax.sound.midi-for-Android

wiki의 설명대로 gradle과 메니페스트에 추가해야함

내부 코드는 stackoverflow 의 sjlevine29 코드 사용  
https://stackoverflow.com/users/912795/sjlevine29  
https://stackoverflow.com/questions/6937760/java-getting-input-from-midi-keyboard  



세션 시작부에 usb 초기화 수행해줘야함. 액티비티 끝나고는 ondestroy로 닫아야함

        usbMidiSystem = new UsbMidiSystem(this);
        usbMidiSystem.initialize();

스레드 돌리는 용으로 asynctask이용

output 되는 data는 다음과 같음 
            //device.getDeviceInfo()  : 기기 이름
            //aMsg[0] = velocity
            //aMsg[1] = note
            //aMsg[2] = pressed much
            
음계만 뽑고싶다면 aMsg만 사용하면 되고 신디사이저용으로 개발할경우 aMsg[0] velocity를 크기(세기), aMsg[2] 를 입력시간으로 하면된다


# AsyncTask : midiRecord.java

public class RecordAudio extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {



            MidiDevice device;
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

            while(started){
                for (int i = 0; i < infos.length; i++) {
                    try {
                        device = MidiSystem.getMidiDevice(infos[i]);
                        // does the device have any transmitters?
                        // if it does, add it to the device list
                        System.out.println(infos[i]);

                        // get all transmitters
                        List<Transmitter> transmitters = device.getTransmitters();
                        // and for each transmitter

                        for (int j = 0; j < transmitters.size(); j++) {
                            // create a new receiver
                            transmitters.get(j).setReceiver(
                                    // using my own MidiInputReceiver
                                    new MidiInputReceiver(device.getDeviceInfo().toString()));
                        }

                        Transmitter trans = device.getTransmitter();
                        trans.setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));

                        // open each device
                        device.open();
                        // if code gets this far without throwing an exception
                        // print a success message
                        System.out.println(device.getDeviceInfo() + " Was Opened");
                        //device 이름이 호출됨 

                        publishProgress();
                    } catch (MidiUnavailableException e) {
                        Log.e("AudioRecord", "Recording Failed");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...params) {

            ((TextView) ((Activity)mContext).findViewById(R.id.tx1)).setText(mds1);
            ((TextView) ((Activity)mContext).findViewById(R.id.tx2)).setText(mds2);
            ((TextView) ((Activity)mContext).findViewById(R.id.tx3)).setText(mds3);

        }
    }


# UI

![image](https://user-images.githubusercontent.com/66546156/132149515-7c449879-cfad-4f36-865a-b952c9c843e9.png)

UI는 심플하게 구성함
