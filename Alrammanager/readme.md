This project is demo for Doze mode.
    - In this project the main aim is to give basic idea about Doze mode and how it affect the app 
      behaviour in general.
    - This application uses alarm manager to set off an alarm at 10 sec after starting time of app.
    - since device isn't in doze mode the application works as desirable.
    - Now to test Doze mode type following command to terminal.
        - $ adb shell dumpsys deviceidle force-idle
    - this will force your adb to idle mode.
    - now to test Doze mode open application.
    - turn on alarm.
    - screen off.
    - type above command in terminal.
    - observe in logcat for incoming notification.
    - since device is idle the alarm will not fired.
    - as soon as user turn screen on.
    - the notification arrives.
    - Now to disable Doze mode write following:
        - $ adb shell dumpsys deviceidle unforce
     