package org.esiea.pouele_nemmene.intechtpapp;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

     DatePickerDialog dpd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv_hw = (TextView)findViewById(R.id.tv_hello_world);

        String now = DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL);
        tv_hw.setText(now);

        Button btn_hw = (Button)findViewById(R.id.button);

        DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String newDate = i2+"/"+i1+"/"+i;
                tv_hw.setText(newDate);

                notification_test("Modification", "Date modifié en " + newDate);
            }
        };

        dpd = new DatePickerDialog(this, odsl, 2018, 2, 10);

        btn_hw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show();
                Toast.makeText(getApplicationContext(), "Selection de date", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_navigate = (Button)findViewById(R.id.button_navigate);
        Button btn_showLondonOnMap = (Button)findViewById(R.id.button_map_london);
        goToSecondeActivity(btn_navigate);
        showLondresOnMap(btn_showLondonOnMap);
    }

    public void goToSecondeActivity(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), SecondeActivity.class);
                startActivity(i);
            }
        });
    }

    public void showLondresOnMap(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Londres")));
            }
        });
    }

    public void notification_test(String title, String message){
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);
        notifBuilder.setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentTitle(title)
                .setContentText(message);

        int mNotificationId = 1;
        NotificationManager notifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifyManager.notify(mNotificationId, notifBuilder.build());
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(getApplicationContext(), getString(R.string.menu_item1), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(getApplicationContext(), getString(R.string.menu_item2), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.help:
                Toast.makeText(getApplicationContext(), getString(R.string.menu_help), Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
