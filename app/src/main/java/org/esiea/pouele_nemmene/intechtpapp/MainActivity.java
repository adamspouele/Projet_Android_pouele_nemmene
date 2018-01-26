package org.esiea.pouele_nemmene.intechtpapp;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    final static String BIERS_UPDATE = "BIERS_UPDATE";
    RecyclerView rv = null;

     DatePickerDialog dpd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv_hw = (TextView)findViewById(R.id.tv_hello_world);
        this.rv = (RecyclerView) findViewById(R.id.rv_biere);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        rv.setAdapter(new BiersAdapter(this.getBiersFromFile()));

        startServices();

        IntentFilter intentFilter = new IntentFilter(BIERS_UPDATE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BierUpdate(this),intentFilter);

        String now = DateUtils.formatDateTime(getApplicationContext(), (new Date()).getTime(), DateFormat.FULL);
        tv_hw.setText(now);

        Button btn_hw = (Button)findViewById(R.id.button);

        DatePickerDialog.OnDateSetListener odsl = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String newDate = i2+"/"+i1+"/"+i;
                tv_hw.setText(newDate);

                sendNotification("Modification", "Date modifié en " + newDate);
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

    public void startServices(){
        GetBiersServices.startActionBIERS(this);
    }

    public void goToSecondeActivity(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), SecondeActivity.class);
                startActivity(i);


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("New Thread", "Thread created ===================> "+ Thread.currentThread().getName());
                    }
                }).start();
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

    public void sendNotification(String title, String message){
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

    public JSONArray getBiersFromFile(){
        try {
            InputStream is = new FileInputStream(getCacheDir() + "/" + "bieres.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new JSONArray(new String(buffer, "UTF-8")); // construction du tableau
        } catch (IOException e) {
            e.printStackTrace();
            return new JSONArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
    }

    private class BiersAdapter extends RecyclerView.Adapter<BiersAdapter.BierHolder> {

        private JSONArray bieres;

        public class BierHolder extends RecyclerView.ViewHolder {

            public TextView name;

            public BierHolder(View itemView) {

                super(itemView);
                this.name = itemView.findViewById(R.id.rv_bier_element_name);
            }
        }

        public BiersAdapter(JSONArray jsonArray) {
            super();
            this.bieres = jsonArray;
        }

        public void setNewBiere(JSONArray jsonArray){
            this.bieres = jsonArray;
            notifyDataSetChanged();
        }

        @Override
        public BierHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_bier_element, null, false);

            BierHolder bh = new BierHolder(v);
            return  bh;
        }

        @Override
        public void onBindViewHolder(BierHolder holder, int position) {
            try {
                holder.name.setText(this.bieres.getJSONObject(position).getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return this.bieres.length();
        }
    }

}

