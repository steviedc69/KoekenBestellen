package com.example.steven.koekenbestellen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.steven.koekenbestellen.Model.Bestelling;

import java.util.ArrayList;

/**
 * Created by Steven on 6/02/15.
 */
public class BestellingAdapter extends BaseAdapter {

    private ArrayList<Bestelling> bestellingen;
    private Context context;

    public BestellingAdapter(ArrayList<Bestelling> bestellingen,Context context)
    {
        this.bestellingen = bestellingen;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bestellingen.size();
    }

    @Override
    public Object getItem(int position) {
        return bestellingen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bestellingen.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(R.layout.list_item_bestelling,parent,false);
            holder = new ViewHolder();
            holder.naam = (TextView)row.findViewById(R.id.list_item_naam_txt);
            holder.aantalChoco = (TextView)row.findViewById(R.id.list_item_choco_txt);
            holder.aantalVanille = (TextView)row.findViewById(R.id.list_item_vanille_txt);
            holder.aantalFranchi = (TextView)row.findViewById(R.id.list_item_franchi_txt);
            holder.betaaldBox = (CheckBox)row.findViewById(R.id.list_item_betaald_check);

            row.setTag(holder);

        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        Bestelling b = bestellingen.get(position);
        holder.naam.setText(b.getVoornaam()+" "+b.getNaam());
        holder.aantalChoco.setText("Chocolade : "+b.getAantalChoco());
        holder.aantalVanille.setText("Vanille : "+b.getAantalVanille());
        holder.aantalFranchi.setText("Franchipane : "+b.getAantalFranchi());
        holder.betaaldBox.setChecked(b.isBetaald());
        holder.betaaldBox.setEnabled(false);
        return row;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}
    class ViewHolder{

        TextView naam;
        TextView aantalChoco;
        TextView aantalVanille;
        TextView aantalFranchi;
        CheckBox betaaldBox;

 }
