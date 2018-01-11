package com.axxezo.MobileReader;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

import static android.R.attr.minHeight;

public class customViewPeople extends RecyclerView.Adapter<customViewPeople.UserViewHolder> implements Filterable {
    private ArrayList<Cards> mDataSet;
    private ArrayList<Cards> filteredmDataSet;
    private ArrayList<Integer> positions;
    private cardsFilter cardsfilter;
    private Context context;

    public customViewPeople(ArrayList<Cards> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.filteredmDataSet = mDataSet;
        for (int i = 0; i < this.mDataSet.size(); i++) {
            positions.add(mDataSet.get(i).getIsInside());
        }
        this.context = context;
    }



    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_row, parent, false);
        UserViewHolder userViewHolder = new UserViewHolder(v);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(final UserViewHolder holder, int position) {
        DatabaseHelper db = DatabaseHelper.getInstance(holder.itemView.getContext());
        Cursor origin_destination = db.select("select (select name from ports where id_mongo='" + mDataSet.get(position).getOrigin() + "'),(select name from ports where id_mongo='" + mDataSet.get(position).getDestination() + "')");
        int manual_sell = Integer.parseInt(db.selectFirst("select is_manual_sell from manifest where id_people='"+mDataSet.get(holder.getAdapterPosition()).getDocument()+"'"));
        holder.people_Name.setText(mDataSet.get(position).getName().trim());
        holder.people_DNI.setText(mDataSet.get(position).getDocument());
//        holder.people_destination.setText(mDataSet.get(position).getDestination());
        holder.textViewExpand.setText("Nombre    :" + mDataSet.get(position).getName() + "\n" + "DNI            :" + mDataSet.get(position).getDocument() + "\n" + "Origen      :" + origin_destination.getString(0) + "\n" + "Destino    :" + origin_destination.getString(1));
        holder.textViewExpand.setBackgroundColor(Color.parseColor("#E6E6E6"));
        if (origin_destination != null)
            origin_destination.close();
        if(manual_sell==1){
            //int id = holder.is_manual_sell.getContext().getResources().getIdentifier(R.drawable.icon_manual_sell);
            //holder.is_manual_sell.setImageResource(R.drawable.icon_manual_sell);
            //Log.e("error","manual sell");
        }
        Log.d("position",position+"");
        switch (mDataSet.get(position).getIsInside()) {
            case 0:
                holder.icon_entry.setText("");
                holder.icon_entry.setBackground(holder.icon_entry.getContext().getResources().getDrawable(R.drawable.circular_textview_blank));
                // holder.spinner_state.setSelection(0);
                break;
            case 1:
                holder.icon_entry.setText("E");
                holder.icon_entry.setBackground(holder.icon_entry.getContext().getResources().getDrawable(R.drawable.circular_textview_embarked));
                // holder.spinner_state.setSelection(1);
                break;
            case 2:
                holder.icon_entry.setText("D");
                holder.icon_entry.setBackground(holder.icon_entry.getContext().getResources().getDrawable(R.drawable.circular_textview_landed));
                // holder.spinner_state.setSelection(2);
                break;
        }
       /* holder.spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(positions.get(holder.getAdapterPosition()) ==mDataSet.get(position).getIsInside()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(holder.itemView.getContext()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Alert message to be shown");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    Log.e("posicion", position + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        else
            return mDataSet.size();
    }


    @Override
    public Filter getFilter() {
        if (cardsfilter == null)
            cardsfilter = new cardsFilter();
        return cardsfilter;
    }

    public Cards getItem(int position) {
        return mDataSet.get(position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView people_DNI, people_Name, people_destination;
        ExpandableTextView textViewExpand;
        TextView icon_entry;
        Spinner spinner_destination;
        ImageView is_manual_sell;

        UserViewHolder(final View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.user_layout);
            people_DNI = (TextView) itemView.findViewById(R.id.people_DNI);
            people_Name = (TextView) itemView.findViewById(R.id.people_name);
            //people_destination = (TextView) itemView.findViewById(R.id.people_destination);
            icon_entry = (TextView) itemView.findViewById(R.id.icon_entry);
            spinner_destination = (Spinner) itemView.findViewById(R.id.spinner_destination);
            textViewExpand = (ExpandableTextView) itemView.findViewById(R.id.textView_expand);
            is_manual_sell=(ImageView) itemView.findViewById(R.id.is_manual_sell);
            //spinner_state = (Spinner) itemView.findViewById(R.id.spinner_state);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public String toString() {
        return "customViewPeople{" +
                "mDataSet=" + mDataSet +
                '}';
    }

    private class cardsFilter extends Filter {
        private customViewPeople adapter;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            DatabaseHelper db = DatabaseHelper.getInstance(context);
            final FilterResults results = new FilterResults();
            ArrayList<Cards> filterList = new ArrayList<Cards>();
            if (constraint.length() == 0) {
                filterList.addAll(filteredmDataSet);
            } else {
                final String[] parts = constraint.toString().split("\\,");
                String filterPatternOrigin = "";
                String filterPatternDestination = "";
                filterPatternOrigin = parts[0].trim();
                filterPatternDestination = parts[1].trim();
                Log.e("origin", filterPatternOrigin);
                Log.e("destination", filterPatternDestination);
                if (filterPatternOrigin.equals("< TODOS >") && filterPatternDestination.equals("< TODOS >"))
                    filterList.addAll(filteredmDataSet);
                else if (parts[0].equals("< TODOS >") || parts[1].equals("< TODOS >")) {

                    for (final Cards cards : filteredmDataSet) {
                        if (parts[0].equals("< TODOS >")) {
                            if (cards.getDestination().contains(filterPatternDestination))
                                filterList.add(cards);
                        } else if (parts[1].equals("< TODOS >")) {
                            if (cards.getOrigin().contains(filterPatternOrigin))
                                filterList.add(cards);
                        }
                    }
                }else if (!parts[0].isEmpty() || !parts[1].isEmpty()) {
                    for (final Cards cards : filteredmDataSet) {
                        if (cards.getOrigin().contains(filterPatternOrigin) && cards.getDestination().contains(filterPatternDestination)) {
                            filterList.add(cards);
                        }
                    }
                }
                //Log.e("LIST","list size: "+filterList.size());
            }
            results.values = filterList;
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataSet = ((ArrayList<Cards>) results.values);
            notifyDataSetChanged();
        }
    }
}
