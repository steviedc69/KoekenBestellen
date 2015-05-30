package com.example.steven.koekenbestellen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.steven.koekenbestellen.Model.Bestelling;
import com.example.steven.koekenbestellen.Persistence.BestellingContentProvider;
import com.example.steven.koekenbestellen.Persistence.Constance;

import org.w3c.dom.Text;

/**
 * Created by Steven on 7/02/15.
 */
public class BestellingDialog extends Dialog {


    private Bestelling bestelling;
    private TextView afleveringTxt;
    private TextView aantalChocoTxt;
    private TextView aantalVanilleTxt;
    private TextView aantalFranchipaneTxt;
    private TextView gsmTxt;
    private TextView betaaldTxt;
    private TextView totaalTxt;
    //private Context context;
    private Button bewerkBtn;
    private Button verwijderBtn;
    private Button annuleerBtn;
    private OverzichtActivity activity;
    private int pos;

    public BestellingDialog(Context context,Bestelling bestelling,int position) {
        super(context);
        this.bestelling = bestelling;
        this.activity = (OverzichtActivity)context;
        this.pos = position;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        this.setTitle(bestelling.getVoornaam()+" "+bestelling.getNaam());
        this.afleveringTxt = (TextView)findViewById(R.id.dialog_adres_txt);
        this.aantalChocoTxt = (TextView)findViewById(R.id.detail_choco_txt);
        this.aantalVanilleTxt = (TextView)findViewById(R.id.detail_vanille_txt);
        this.aantalFranchipaneTxt = (TextView)findViewById(R.id.detail_franchi_txt);
        this.gsmTxt = (TextView)findViewById(R.id.detail_gsm_txt);
        this.betaaldTxt = (TextView)findViewById(R.id.detail_betaald_txt);
        this.bewerkBtn = (Button)findViewById(R.id.dialog_bewerk_btn);
        this.verwijderBtn = (Button)findViewById(R.id.dialog_verwijder_btn);
        this.annuleerBtn = (Button)findViewById(R.id.dialog_annuleer_btn);
        this.totaalTxt = (TextView)findViewById(R.id.dialog_totaal_txt);

        this.setCancelable(true);

        this.annuleerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.bewerkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bewerkBestelling();
            }
        });
        this.verwijderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verwijderBestelling();
            }
        });

        afleveringTxt.setText(bestelling.getAfleveringAdres());
        aantalChocoTxt.setText("Chocolade : "+bestelling.getAantalChoco());
        aantalVanilleTxt.setText("Vanille : "+bestelling.getAantalVanille());
        aantalFranchipaneTxt.setText("Franchipane : "+bestelling.getAantalFranchi());
        gsmTxt.setText("GSM : "+bestelling.getGsm());
        String betaald = "";
        if(bestelling.isBetaald())
        {
            betaald = "Betaling ok";
        }
        else
        {
            betaald = "Niet betaald";
        }
        betaaldTxt.setText(betaald);
        int tot = (bestelling.getAantalChoco()+bestelling.getAantalFranchi()+bestelling.getAantalVanille());
        totaalTxt.setText("Totaal : "+tot+" x 6euro = "+(tot*6)+".00euro");

    }

    private void bewerkBestelling()
    {
        Intent intent = new Intent(getContext(),NieuweBestellingActivity.class);
        intent.putExtra("bestelling",bestelling);
        getContext().startActivity(intent);
        this.dismiss();
    }

    private void verwijderBestelling()
    {
        String mSelection = Constance.COLUMN_ID+" = ?";
        String[]args = {""+bestelling.getId()};

        int i = getContext().getContentResolver().delete(BestellingContentProvider.CONTENT_URI,mSelection,args);
        this.dismiss();
        this.activity.updateList(pos);
    }
}
