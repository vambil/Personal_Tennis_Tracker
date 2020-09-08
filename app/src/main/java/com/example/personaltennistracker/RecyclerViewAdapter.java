package com.example.personaltennistracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private ArrayList<String> images = new ArrayList<>(); //TODO add actual images locations!
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();
    private ArrayList<String> durations = new ArrayList<>();
    private ArrayList<String> tips = new ArrayList<>();
    private Context context;

    public RecyclerViewAdapter(ArrayList<String> titles, ArrayList<String> dates, ArrayList<String> durations, ArrayList<String> tips, Context context) {
        this.titles = titles;
        this.dates = dates;
        this.durations = durations;
        this.tips = tips;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.practice_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //TODO add in images
        holder.practiceTitle.setText(titles.get(position));
        holder.practiceDate.setText(dates.get(position));
        holder.practiceDuration.setText(durations.get(position));
        holder.practiceTips.setText(tips.get(position));

        holder.cardView.setContentPadding(20,40,20,20);

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Edit button - "+titles.get(position), Toast.LENGTH_SHORT).show();
                //TODO redirect to edit page
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Delete button - "+titles.get(position), Toast.LENGTH_SHORT).show();
                //TODO redirect to delete page
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView practiceTitle;
        TextView practiceDate;
        TextView practiceDuration;
        TextView practiceTips;
        MaterialButton editBtn;
        MaterialButton deleteBtn;
        MaterialCardView cardView;
        LinearLayout cardLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.practiceCardImg);
            practiceTitle = itemView.findViewById(R.id.practiceCardTitle);
            practiceDate = itemView.findViewById(R.id.practiceCardDate);
            practiceDuration = itemView.findViewById(R.id.practiceCardDuration);
            practiceTips = itemView.findViewById(R.id.practiceCardTips);
            editBtn = itemView.findViewById(R.id.practiceCardEditBtn);
            deleteBtn = itemView.findViewById(R.id.practiceCardDeleteBtn);
            cardView = itemView.findViewById(R.id.practiceCardView);
            cardLayout = itemView.findViewById(R.id.card_layout);
        }
    }
}
