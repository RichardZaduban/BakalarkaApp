package com.example.bakalarka10;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private static final String TAG = "Adapter";
    private LayoutInflater layoutInflater;
    private ArrayList<Event> events;
    private Context context;

    Adapter(Context ct, ArrayList<Event> events){
        this.context = ct;
        this.events = events;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle, textDescription;
        Button button;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.cardtitle);
            textDescription = itemView.findViewById(R.id.description);
           // button = itemView.findViewById(R.id.deleteButton);
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
        viewHolder.textDescription.setText(events.get(pos).description);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }


}
