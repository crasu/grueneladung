grueneladung [![Build Status](https://travis-ci.org/crasu/grueneladung.png?branch=master)](https://travis-ci.org/crasu/grueneladung)
============
1.  install android sdk
2.  set ANDROID_HOME and at tools and plaform_tools to path
3.  compile with maven

Development commands
============
*  Switch power on and off:

    adb emu "power ac on" ; adb emu "power ac off"

* Connect to emulator:
    
    telnet localhost 5554

* Switch orientation
    
    adb emu "sensor set acceleration 9:0:0" 
    sleep 5
    adb emu "sensor set acceleration 0:9:0"
