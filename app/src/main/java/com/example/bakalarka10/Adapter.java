package com.example.bakalarka10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private static final String TAG = "Adapter";
    private ArrayList<Event> events;
    private ArrayList<Event> eventsFull;
    private Context context;



    Adapter(Context ct, ArrayList<Event> events){

        this.context = ct;
        this.events = events;
        eventsFull = new ArrayList<>(events);


    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle, textDescription, startTime, endTime;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.cardtitle);
            textDescription = itemView.findViewById(R.id.description);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);

        }

    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int pos) {
        viewHolder.textTitle.setText(events.get(pos).title);
        Log.d(TAG, "onBindViewHolder: "+ events.get(pos).title);
        viewHolder.textDescription.setText(events.get(pos).description);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Log.d(TAG, "onBindViewHolder: " +"To:      "+df.format(events.get(pos).end));

            viewHolder.startTime.setText("From: "+df.format(events.get(pos).start));

            viewHolder.endTime.setText("To:      "+df.format(events.get(pos).end));



    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Event> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(eventsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Event item: eventsFull) {
                    if (item.title.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            events.clear();
            events.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public String deleteEvent(int position){


        String deletedEventid = events.get(position).id;

        String deletedEventTitle = events.get(position).title;

        Log.d(TAG, "deleteEvent: "+ deletedEventid);
        Log.d(TAG, "deleteEvent: "+deletedEventTitle);


        events.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, events.size());

        return deletedEventid;

    };

}
