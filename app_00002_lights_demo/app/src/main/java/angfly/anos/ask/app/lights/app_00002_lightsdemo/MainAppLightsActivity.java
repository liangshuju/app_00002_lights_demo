package angfly.anos.ask.app.lights.app_00002_lightsdemo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

public class MainAppLightsActivity extends Activity {

    private Button mLightButton = null;

    boolean flashing = false;

    final private int LED_NOTIFICATION_ID = 123;

    private Handler mLightHandler = new Handler();

    private LightRunnable mLightRunnable = new LightRunnable();

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

        //mLightHandler.postDelayed(mLightRunnable, 2000);

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
