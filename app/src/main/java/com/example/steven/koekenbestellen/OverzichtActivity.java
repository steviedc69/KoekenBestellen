package com.example.steven.koekenbestellen;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.steven.koekenbestellen.Model.Bestelling;
import com.example.steven.koekenbestellen.Persistence.BestellingContentProvider;
import com.example.steven.koekenbestellen.Persistence.Constance;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class OverzichtActivity extends Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = "OverzichtActivity";
    //@InjectView(R.id.list_bestellingen)
    ListView bestellingenListView;
    private ArrayList<Bestelling>bestellings;
    private BestellingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overzicht);
        //ButterKnife.inject(this);
        bestellingenListView = (ListView)findViewById(R.id.list_bestellingen);
        if(savedInstanceState != null)
        {
            bestellings = savedInstanceState.getParcelableArrayList("bestellingen");
        }
        else
        {
            fillData();
        }

        adapter = new BestellingAdapter(bestellings,this);
        bestellingenListView.setAdapter(adapter);
        bestellingenListView.setOnItemClickListener(this);

    }

    public void fillData()
    {
        bestellings = getIntent().getParcelableArrayListExtra("bestellingen");
        if(bestellings==null)
        {
            Cursor c = getContentResolver().query(BestellingContentProvider.CONTENT_URI,null,null,null,null);
            if(!c.moveToFirst())
            {
                Toast.makeText(this, "No koekjes yet", Toast.LENGTH_LONG).show();
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
                    int i = c.getInt(c.getColumnIndex(Constance.COLUMN_BETAALD));
                    if(i == 1)
                    {
                        b.setBetaald(true);
                    }
                    else
                    {
                        b.setBetaald(false);
                    }
                    b.setAantalChoco(c.getInt(c.getColumnIndex(Constance.COLUMN_CHOCO)));

                    b.setAantalVanille(c.getInt(c.getColumnIndex(Constance.COLUMN_VANILLE)));

                    b.setAantalFranchi(c.getInt(c.getColumnIndex(Constance.COLUMN_FRANCH)));

                    bestellings.add(b);

                }
                while(c.moveToNext());
            }
        }

    }
    public void updateList(int position)
    {
       bestellings.remove(position);
       adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(bestellings!=null)
        {
            outState.putParcelableArrayList("bestellingen",bestellings);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_overzicht, menu);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.d(TAG,"clicked "+position);
        BestellingDialog dialog = new BestellingDialog(this,bestellings.get(position),position);
        dialog.show();
    }
}
