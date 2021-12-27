package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.travelnotes.Model.Viaggio;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    Viaggio[] viaggi;
    Context context;
    String rDeparture[];
    String rDestination[];



    public TravelAdapter(Viaggio[] viaggi, TravelList activity) {
        this.viaggi = viaggi;
        this.context = activity;
    }



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
        final Viaggio travelList = viaggi[position];
        holder.travelDeparture.setText(travelList.getPartenzaAndata());
        holder.travelDestination.setText(travelList.getDestinazioneAndata());
        //holder.travelTime.setText((int) travelList.getDurataAndata());

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent = new Intent(this, Activity_travel_view.class);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(viaggi!= null){
            return viaggi.length;
        }
        return 0;

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Activity_travel_view.class);
                    intent.putExtra("partenza", rDeparture[getAdapterPosition()]);
                    intent.putExtra("arrivo", rDestination[getAdapterPosition()]);
                    context.startActivity(intent);
                }
            });

        }


    }
}

