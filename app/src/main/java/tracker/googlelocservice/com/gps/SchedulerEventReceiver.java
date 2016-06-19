package tracker.googlelocservice.com.gps;

/**
 * Created by root on 6/7/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class SchedulerEventReceiver extends BroadcastReceiver {
    private static final String APP_TAG = "assetviewer.findergpstracking.com.findergpstracker";


    @Override
    public void onReceive(Context context, Intent intent) {

        // TODO Auto-generated method stub
        try {
            PhoneStateListener phl = new PhoneStateListener() {
                public void onCallStateChanged(int state, String incomingNumber) {
                    switch (state) {
                        case TelephonyManager.CALL_STATE_RINGING:
                            // Do something
                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            // Do something
                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            // Do something
                            break;
                        default:
                            Log.d(APP_TAG, "Unknown phone state=" + state);
                    }
                }
            };

            Toast.makeText(context, "SchedulerBroadcast is running", Toast.LENGTH_SHORT).show();
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork.isConnected();
            if (activeNetwork != null && isConnected) {
                Intent eventService = new Intent(context, LocationService.class);
                context.startService(eventService);
            }

        } catch (Exception e) {

        }

    }
}
