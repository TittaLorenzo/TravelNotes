package it.unimib.travelnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import it.unimib.travelnotes.Model.Viaggio;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    Viaggio[] viaggio;
    Context context;

    public TravelAdapter(Viaggio[] viaggio, TravelList activity) {
        this.viaggio = viaggio;
        this.context = activity;
    }
/*
    public interface OnItemClickListener {
        void onItemClick(Viaggio viaggio);
    }
    private List <Viaggio> travelList;
    private final OnItemClickListener mOnItemClickListener;
    //private String [] localDataSet;
    public TravelAdapter(Viaggio[] travelList, TravelListActivity onItemClickListener) {
        this.travelList = travelList;
        this.mOnItemClickListener = onItemClickListener;
    }
*/

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.travel,parent,false);
        TravelViewHolder viewHolder = new TravelViewHolder(view);
        return viewHolder;
    }

    //da modificare con il database
    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        //TravelViewHolder.getTextView(localDataSet[position]);
        final Viaggio travelList = viaggio[position];
        holder.travelDeparture.setText(travelList.getPartenzaAndata());
        holder.travelDestination.setText(travelList.getDestinazioneAndata());
        //holder.travelTime.setText((int) travelList.getDurataAndata());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                APRIRE IL VIAGGIO
                Intent intent = new Intent(this, NewTravel.class);
                 */
            }
        });
        /*
        Viaggio viaggio = travelList.get(position);
        holder.travelDestination.setText(viaggio.getDestinazioneAndata());
        */
    }

    @Override
    public int getItemCount() {
        return viaggio.length;

    }

    public class TravelViewHolder extends RecyclerView.ViewHolder{

        TextView travelDeparture;
        TextView travelDestination;
       // TextView travelTime;


        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            travelDeparture = itemView.findViewById(R.id.departure);
            travelDestination = itemView.findViewById(R.id.destination);
            //travelTime = itemView.findViewById(R.id.time);
            /*MODIFICARE CON DATA E ORA
            textViewName = itemView.findViewById(R.id.date);
            textViewDate = itemView.findViewById(R.id.time);
             */

        }


    }
}

