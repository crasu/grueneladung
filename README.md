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

* Release:

    1. Increase versions in AndroidMainifest.xml
    2. run mvn clean -Prelease package
    3. upload new apk

* Tweaks
    * If the emulator runs slow on linux set devices.system.cpu.cpufreq.ondemand.up_threshold = 85 in /etc/sysctl.conf
    * Cannot find android sdk error in intellij => recreate project
