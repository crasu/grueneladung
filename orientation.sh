adb emu "power ac on" ; adb emu "power ac off"
adb emu "sensor set acceleration 9:0:0"
sleep 2
adb emu "sensor set acceleration 0:9:0"

