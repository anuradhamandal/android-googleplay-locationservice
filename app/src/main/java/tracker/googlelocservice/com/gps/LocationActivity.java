package tracker.googlelocservice.com.gps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import tracker.googlelocservice.com.googlelocservice.R;

public class LocationActivity extends AppCompatActivity {

    ToggleButton locationService;
    SharedPreferences finder_sharedprefs = null;
    public static final String FINDERPREFERENCES = "FINDEGPSTRACKING_RPREFS";
    private Boolean trackingStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationService = (ToggleButton) findViewById(R.id.toggleButton);

        finder_sharedprefs = getSharedPreferences(FINDERPREFERENCES, MODE_PRIVATE);
        trackingStatus = finder_sharedprefs.getBoolean("testLocationService_status", false);

        if(trackingStatus){
            locationService.setChecked(true);
        } else locationService.setChecked(false);


        locationService.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){

//                    AlarmRecurrer loc = new AlarmRecurrer(getApplicationContext());
//                    loc.setRecurringAlarmSchedule(getApplicationContext());
                    AlarmManager alarmManager = (AlarmManager) getApplicationContext()
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent(getApplicationContext(), SchedulerEventReceiver.class); // explicit
                    // intent
                    PendingIntent intentExecuted = PendingIntent.getBroadcast(getApplicationContext(), 0, i,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    Calendar now = Calendar.getInstance();
                    now.add(Calendar.SECOND, 20);
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                            now.getTimeInMillis(), 300000, intentExecuted);

                    ComponentName receiver = new ComponentName(getApplicationContext(), SchedulerEventReceiver.class);
                    PackageManager pm = getApplicationContext().getPackageManager();

                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();

                    finder_sharedprefs.edit().putBoolean("testLocationService_status", true).commit();
                    Toast.makeText(getApplicationContext(),"Tracking is on",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Toggle Status:  "+isChecked,Toast.LENGTH_SHORT).show();
                }
                if (isChecked == false){

//                    Intent locationService = new Intent(getApplicationContext(), LocationService.class);
//                    getApplicationContext().stopService(locationService);

//                    AlarmRecurrer loc = new AlarmRecurrer(getApplicationContext());
//                    loc.stopRecurringAlarmSchedule(getApplicationContext());

                    Intent intent = new Intent(getApplicationContext(), SchedulerEventReceiver.class);
                    PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.cancel(sender);
                    Toast.makeText(getApplicationContext(), "Cancelled alarm", Toast.LENGTH_SHORT).show();

                    ComponentName receiver = new ComponentName(getApplicationContext(), SchedulerEventReceiver.class);
                    PackageManager pm = getApplicationContext().getPackageManager();

                    pm.setComponentEnabledSetting(receiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Toast.makeText(getApplicationContext(), "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();


                    Intent locationService = new Intent(getApplicationContext(), LocationService.class);
                    getApplicationContext().stopService(locationService);

                    finder_sharedprefs.edit().putBoolean("testLocationService_status", false).commit();
                    Toast.makeText(getApplicationContext(),"Tracking is off",Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Toggle Status:  "+isChecked,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
