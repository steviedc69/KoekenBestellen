package com.example.steven.koekenbestellen;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steven.koekenbestellen.Model.Bestelling;
import com.example.steven.koekenbestellen.Persistence.BestellingContentProvider;
import com.example.steven.koekenbestellen.Persistence.BestellingDB;
import com.example.steven.koekenbestellen.Persistence.Constance;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private TextView chocoTotTxt;
    private TextView vanilleTotTxt;
    private TextView franchiTotTxt;
    private int totaal;
    private int aantalChoco;
    private int aantalVanille;
    private int aantalFranchi;

    private TextView totaalTxt;
    private Button btnNieuw;
    private Button btnOverzicht;
    private BestellingDB db;
    private ArrayList<Bestelling>bestellingen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ButterKnife.inject(this);



        db = new BestellingDB(this);
        db.open();

            bestellingen = new ArrayList<>();

            aantalChoco = 0;
            aantalFranchi = 0;
            aantalVanille = 0;
            totaal = 0;
            getData();


        chocoTotTxt = (TextView)findViewById(R.id.txtchocoaantal);
        vanilleTotTxt = (TextView)findViewById(R.id.txtvanilleaantal);
        franchiTotTxt = (TextView)findViewById(R.id.txtfranchiaantal);
        btnNieuw = (Button)findViewById(R.id.btn_nieuwe_bestelling);
        btnNieuw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNewOrder();
            }
        });
        totaalTxt = (TextView)findViewById(R.id.txttotaalaantal);

        btnOverzicht = (Button)findViewById(R.id.btn_overzicht);
        btnOverzicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOverzicht();
            }
        });
        update();
    }

    private void toOverzicht()
    {
        Intent intent = new Intent(this,OverzichtActivity.class);
        intent.putParcelableArrayListExtra("bestellingen",bestellingen);
        startActivity(intent);
    }
    private void getData()
    {
        Cursor c = getContentResolver().query(BestellingContentProvider.CONTENT_URI,null,null,null,null);
        if(!c.moveToFirst())
        {
            Toast.makeText(this,"No koekjes yet",Toast.LENGTH_LONG).show();
        }
        else
        {
            do {
                Bestelling b = new Bestelling();
                b.setId(c.getInt(c.getColumnIndex(Constance.COLUMN_ID)));
                Log.d(TAG,"bestelling id : "+b.getId());
                b.setNaam(c.getString(c.getColumnIndex(Constance.COLUMN_NAAM)));
                b.setVoornaam(c.getString(c.getColumnIndex(Constance.COLUMN_VOORNAAM)));
                b.setAfleveringAdres(c.getString(c.getColumnIndex(Constance.COLUMN_AFLEVERING)));
                b.setGsm(c.getString(c.getColumnIndex(Constance.COLUMN_GSM)));
                //Log.d(TAG, "column betaald : " + c.getInt(c.getColumnIndex(Constance.COLUMN_BETAALD)));
                if(c.getInt(c.getColumnIndex(Constance.COLUMN_BETAALD))==1)
                {
                    b.setBetaald(true);
                }
                else
                {
                    b.setBetaald(false);
                }

                b.setAantalChoco(c.getInt(c.getColumnIndex(Constance.COLUMN_CHOCO)));
                aantalChoco += b.getAantalChoco();
                b.setAantalVanille(c.getInt(c.getColumnIndex(Constance.COLUMN_VANILLE)));
                aantalVanille += b.getAantalVanille();
                b.setAantalFranchi(c.getInt(c.getColumnIndex(Constance.COLUMN_FRANCH)));
                aantalFranchi += b.getAantalFranchi();
                bestellingen.add(b);

            }
            while(c.moveToNext());
        }
    }

    public void toNewOrder()
    {
        Intent intent = new Intent(this,NieuweBestellingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getChocoTotal()
    {
        /*
        Uri choco = Uri.parse(BestellingContentProvider.CONTENT_URI+"/"+BestellingContentProvider.CHOCO_TOT);
        Cursor c = getContentResolver().query(choco,null,null,null,"id");
        int aantal = 0;
        if(!c.moveToFirst()){
            Log.e(TAG,"cursor empty");
        }
        else
        {
            do {
                aantal = c.getInt(c.getColumnIndex(Constance.COLUMN_CHOCO_TOTAL));
            }while (c.moveToNext());
        }
        return aantal;
        */
        return db.getChocoCountTot();
    }

    private int getVanilleTotal()
    {
        /*
        Uri choco = Uri.parse(BestellingContentProvider.CONTENT_URI+"/"+BestellingContentProvider.VANILLE_TOT);
        Cursor c = getContentResolver().query(choco,null,null,null,"id");
        int aantal = 0;
        if(!c.moveToFirst()){
            Log.e(TAG,"cursor empty");
        }
        else
        {
            do {
                aantal = c.getInt(c.getColumnIndex(Constance.COLUMN_VANILLE_TOTAL));
            }while (c.moveToNext());
        }
        return aantal;
        */
        return db.getVanilleCountTot();
    }

    private int getFranchiTotal()
    {
        /*
        Uri franch = Uri.parse(BestellingContentProvider.CONTENT_URI+"/"+BestellingContentProvider.FRANCHI_TOT);
        Cursor c = getContentResolver().query(franch,null,null,null,"id");
        int aantal = 0;
        if(!c.moveToFirst()){
            Log.e(TAG,"cursor empty");
        }
        else
        {
            do {
                aantal = c.getInt(c.getColumnIndex(Constance.COLUMN_FRANCHI_TOTAL));
            }while (c.moveToNext());
        }
        return aantal;
        */
        return db.getFranchiCountTot();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("choco",aantalChoco);
        outState.putInt("vanille",aantalVanille);
        outState.putInt("franchi",aantalFranchi);
    }

    public void update()
    {
        totaal = aantalChoco+aantalFranchi+aantalVanille;
        chocoTotTxt.setText(""+aantalChoco);
        vanilleTotTxt.setText(""+aantalVanille);
        franchiTotTxt.setText(""+aantalFranchi);
        totaalTxt.setText(""+totaal);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart");
        bestellingen = new ArrayList<>();
        aantalChoco = 0;
        aantalFranchi = 0;
        aantalVanille = 0;
        totaal = 0;
        getData();
        update();
        Log.d(TAG,"bestelling count : "+bestellingen.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause");

    }
}
