package com.axxezo.MobileReader;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by axxezo on 12/01/2017.
 */

public class cardsSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> cardList;

    public cardsSpinnerAdapter(Context context, int textViewResourceId,
                               ArrayList<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.cardList = values;
    }

    public int getCount(){
        return cardList.size();
    }

    public String getItem(int position){
        return cardList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(cardList.get(position));

        return view;
    }

    //View of Spinner on dropdown Popping

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(cardList.get(position));
        view.setHeight(60);

        return view;
    }

}
