package com.example.steven.koekenbestellen;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steven.koekenbestellen.Model.Bestelling;
import com.example.steven.koekenbestellen.Persistence.BestellingContentProvider;
import com.example.steven.koekenbestellen.Persistence.BestellingDB;
import com.example.steven.koekenbestellen.Persistence.Constance;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class NieuweBestellingActivity extends ActionBarActivity implements NumberPicker.OnValueChangeListener{

    private static final String TAG = "NieuweBestellingActiviteit";
    @InjectView(R.id.edit_naam)EditText naamEdit;
    @InjectView(R.id.edit_voornaam)EditText voornaamEdit;
    @InjectView(R.id.edit_aflevering)EditText afleveringAdresEdit;
    @InjectView(R.id.edit_gsm)EditText gsmEdit;
    @InjectView(R.id.npicker_choco)NumberPicker chocoPicker;
    @InjectView(R.id.npicker_vanille)NumberPicker vanillePicker;
    @InjectView(R.id.npicker_franchi)NumberPicker franchiPicker;
    @InjectView(R.id.btn_annuleer)Button annuleerBtn;
    @InjectView(R.id.btn_opslaan)Button opslaanBtn;
    private BestellingDB db;

    @InjectView(R.id.txt_bestelling_totaal_aantal)
    TextView bestellingTotaalAantalTxt;
    @InjectView(R.id.txt_totaal_prijs) TextView totaalBetalenTxt;
    @InjectView(R.id.checkbox_betaald)
    CheckBox betaaldCheckbox;
    private int totaalAantal;
    private int totaalPrijs;
    private boolean isUpdate;
    private Bestelling bestelling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nieuwe_bestelling);
        db = new BestellingDB(this);
        db.open();
        totaalAantal = 0;
        totaalPrijs = 0;
        ButterKnife.inject(this);
        installPickers(chocoPicker);
        installPickers(vanillePicker);
        installPickers(franchiPicker);
        isUpdate = false;
        if(getIntent().hasExtra("bestelling"))
        {

            bestelling = getIntent().getParcelableExtra("bestelling");
            Log.d(TAG,"bestelling id = "+bestelling.getId());
            naamEdit.setText(bestelling.getNaam());
            voornaamEdit.setText(bestelling.getVoornaam());
            afleveringAdresEdit.setText(bestelling.getAfleveringAdres());
            gsmEdit.setText(bestelling.getGsm());
            chocoPicker.setValue(bestelling.getAantalChoco());
            vanillePicker.setValue(bestelling.getAantalVanille());
            franchiPicker.setValue(bestelling.getAantalFranchi());
            betaaldCheckbox.setChecked(bestelling.isBetaald());
            isUpdate = true;
            totaalAantal = bestelling.getAantalChoco()+bestelling.getAantalFranchi()+bestelling.getAantalVanille();
            totaalPrijs = totaalAantal*6;
        }



        annuleerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlert();
            }
        });
        opslaanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opslaan();
            }
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nieuwe_bestelling, menu);
        return true;
    }
    private void openAlert()
    {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Are you sure??")
                .setMessage("Are you really sure??")
                .setCancelable(true)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        returnToMain();
                    }
                }).setNegativeButton("No",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dismiss
                    }
                }).show();
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
    private void installPickers(NumberPicker picker)
    {
        picker.setOnValueChangedListener(this);
        picker.setMinValue(0);
        picker.setMaxValue(10);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        totaalAantal -= oldVal;
        totaalAantal += newVal;
        totaalPrijs = totaalAantal * 6;
        bestellingTotaalAantalTxt.setText(""+totaalAantal);
        totaalBetalenTxt.setText(""+totaalPrijs+".00");
    }

    private boolean checkIfCorrect()
    {
        boolean correct = true;
        if(naamEdit.getText().toString().isEmpty())
        {
            naamEdit.setHint("naam verplicht");
            naamEdit.setHintTextColor(Color.RED);
            correct = false;
        }
        if (voornaamEdit.getText().toString().isEmpty())
        {
            voornaamEdit.setHint("voornaam verplicht");
            voornaamEdit.setHintTextColor(Color.RED);
            correct = false;
        }
        if (afleveringAdresEdit.getText().toString().isEmpty())
        {
            afleveringAdresEdit.setHint("plaats verplicht");
            afleveringAdresEdit.setHintTextColor(Color.RED);
            correct = false;
        }
        if (totaalAantal==0)
        {
            Toast.makeText(this,"Er werd niets besteld",Toast.LENGTH_LONG).show();
            correct = false;
        }
        return correct;
    }

    private void opslaan()
    {
        if(checkIfCorrect())
        {
            String naam = naamEdit.getText().toString();
            String voornaam = voornaamEdit.getText().toString();
            String aflevering = afleveringAdresEdit.getText().toString();

            int aantalChoco = chocoPicker.getValue();
            int aantalVanille = vanillePicker.getValue();
            int franchi = franchiPicker.getValue();
            boolean betaald = betaaldCheckbox.isChecked();

            if(bestelling == null && !isUpdate)
            {
                bestelling = new Bestelling(naam,voornaam,aflevering,aantalVanille,aantalChoco,franchi,betaald);
            }
            else
            {
                bestelling.setNaam(naam);
                bestelling.setVoornaam(voornaam);
                bestelling.setAfleveringAdres(aflevering);
                bestelling.setAantalChoco(aantalChoco);
                bestelling.setAantalVanille(aantalVanille);
                bestelling.setAantalFranchi(franchi);
                bestelling.setBetaald(betaald);
            }

            if (!gsmEdit.getText().toString().isEmpty())
            {
                String gsm = gsmEdit.getText().toString();
                bestelling.setGsm(gsm);
            }
            //OpslaanBackground op = new OpslaanBackground(this);
            //op.execute(bestelling);
            toDBWithUri(bestelling);


        }
    }

    private void toDBWithUri(Bestelling b)
    {
        ContentValues values = new ContentValues();
        values.put(Constance.COLUMN_NAAM,b.getNaam());
        values.put(Constance.COLUMN_VOORNAAM,b.getVoornaam());
        values.put(Constance.COLUMN_AFLEVERING,b.getAfleveringAdres());
        values.put(Constance.COLUMN_CHOCO,b.getAantalChoco());
        values.put(Constance.COLUMN_VANILLE,b.getAantalVanille());
        values.put(Constance.COLUMN_FRANCH,b.getAantalFranchi());
        if(b.getGsm() != null)
        {
            values.put(Constance.COLUMN_GSM,b.getGsm());
        }
        values.put(Constance.COLUMN_BETAALD,b.isBetaald());
        if(isUpdate)
        {
            String mSelection = Constance.COLUMN_ID+" = ?";
            String mSelectionArgs[]= {""+b.getId()};

           int i = getContentResolver().update(BestellingContentProvider.CONTENT_URI,values,mSelection,mSelectionArgs);
        }
        else
        {
            Uri uri = getContentResolver().insert(BestellingContentProvider.CONTENT_URI,values);
            if(b.getGsm() != null)
            {
                sendSMS(b);
            }
        }


        returnToMain();
    }

    private void returnToMain()
    {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }
    private void sendSMS(Bestelling b)
    {
        try{
            String betaald = b.isBetaald() ? "Uw betaling is in orde." : "Uw betaling zal op afleveringsdatum worden afgerekend.";
            String message = "Bedankt voor uw bestelling, "+b.getAantalChoco()+" dozen chocolade wafels, "+b.getAantalVanille()+
                    " dozen vanille wafels, "+b.getAantalFranchi()+" dozen franchipanes. "+" Deze zullen rond 26/02 geleverd worden. Groetjes Steven";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(b.getGsm(),null,message,null,null);
            Toast.makeText(this,"SMS werd verzonden",Toast.LENGTH_LONG).show();

        }catch (Exception ex)
        {
            Toast.makeText(this,"SMS failed",Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


}


