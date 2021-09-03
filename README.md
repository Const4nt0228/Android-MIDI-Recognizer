# Android-MIDI-Recognizer
안드로이드 폰에서 전자피아노 MIDI Output을 입력받는 어플리케이션 응용하면 리듬게임, DDR, 신디사이저등에 활용가능

안드로이드에서도 midi 라이브러리가 있긴한데 java eclipse에서 만들던걸 그대로 가져오기위해서 javax.sound를 따로 구해서 사용함

# How To Use
kshoji 님의 usb-midi-driver, android용 javax.sound,midi 를 사용

https://github.com/kshoji/USB-MIDI-Driver

https://github.com/kshoji/javax.sound.midi-for-Android

wiki의 설명대로 gradle과 메니페스트에 추가해야함

세션 시작부에 usb 초기화 수행해줘야함. 액티비티 끝나고는 ondestroy로 닫아야함

        usbMidiSystem = new UsbMidiSystem(this);
        usbMidiSystem.initialize();

스레드 돌리는 용으로 asynktask이용

output 되는 data는 다음과 같음 

            //aMsg[0] = velocity
            //aMsg[1] = note
            //aMsg[2] = pressed much
            
음계만 뽑고싶다면 aMsg만 사용하면 되고 신디사이저용으로 개발할경우 aMsg[0] velocity를 크기(세기), aMsg[2] 를 입력시간으로 하면된다
