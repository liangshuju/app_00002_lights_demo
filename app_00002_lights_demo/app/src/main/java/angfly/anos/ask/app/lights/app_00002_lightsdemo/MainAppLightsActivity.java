package angfly.anos.ask.app.lights.app_00002_lightsdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Set;

public class MainAppLightsActivity extends Activity {

    private Button mLightButton = null;

    boolean flashing = false;

    final private int LED_NOTIFICATION_ID = 123;

    private Handler mLightHandler = new Handler();

    private LightRunnable mLightRunnable = new LightRunnable();

    private SeekBar mLcdBackLightsSeekBar = null;

    class LightRunnable implements Runnable {
        @Override
        public void run() {
            if (flashing) {
                flashingLight();
            } else {
                ClearLed();
            }
        }
    }

    private void ClearLed() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(LED_NOTIFICATION_ID);
    }

    private void flashingLight() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification mNotification = new Notification();
        mNotification.flags = Notification.FLAG_SHOW_LIGHTS;
        mNotification.ledARGB = 0xFF0000FF;
        mNotification.ledOnMS = 100;
        mNotification.ledOffMS = 100;
        mNotificationManager.notify(LED_NOTIFICATION_ID, mNotification);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app_lights);

        mLightButton = (Button) findViewById(R.id.button_flash);
        mLcdBackLightsSeekBar = findViewById(R.id.id_seekBar_lcd_backlights);

        touchBackLightsSeekBar();
        setLedFlash();
    }

    private void touchBackLightsSeekBar() {
        try {
            // 关闭自动调整背光的功能
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

            // 根据系统背光值，设置滑动块的值
            int mBackLights = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            mLcdBackLightsSeekBar.setProgress(mBackLights*255/100);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        mLcdBackLightsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int backlights = mLcdBackLightsSeekBar.getProgress();
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, backlights*255/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setLedFlash() {
        mLightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashing = !flashing;
                if (flashing) {
                    mLightButton.setText("Stop Flashing the Light");
                } else {
                    mLightButton.setText("Flashing Light at 20s");
                }

                mLightHandler.postDelayed(mLightRunnable, 20000);
            }
        });
    }

}
