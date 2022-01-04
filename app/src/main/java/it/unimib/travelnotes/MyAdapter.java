/*package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.travelnotes.Model.Viaggio;

public class TravelAdapter extends RecyclerView.Adapter<TravelAdapter.TravelViewHolder> {

    Viaggio[] viaggi;
    Context context;
    ArrayList<Viaggio> list;

    String rDeparture[];
    String rDestination[];



    public TravelAdapter(Context context, ArrayList<Viaggio> list) {
        this.context = context;
        this.list = list;
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
                String s1 = travelList.getPartenzaAndata();
                String s2 = travelList.getDestinazioneAndata();
                Intent intent=new Intent(context, Activity_travel_view.class);
                intent.putExtra("partenza", s1);
                intent.putExtra("arrivo", s2);

            }
        });

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
        //TextView travelDepartureR;
        //TextView travelDestinationR;
       // TextView travelTime;


        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            travelDeparture = itemView.findViewById(R.id.departure);
            travelDestination = itemView.findViewById(R.id.destination);
            //travelDepartureR = itemView.findViewById(R.id.departure2);
            //travelDestinationR = itemView.findViewById(R.id.destination2);
            //travelTime = itemView.findViewById(R.id.time);
            /*MODIFICARE CON DATA E ORA
            textViewName = itemView.findViewById(R.id.date);
            textViewDate = itemView.findViewById(R.id.time);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Activity_travel_view.class);
                    intent.putExtra("partenza", travelDeparture.toString());
                    intent.putExtra("arrivo",  travelDestination.toString());
                    //intent.putExtra("partenzaR", rDeparture[getAdapterPosition()]);
                    //intent.putExtra("arrivoR", rDestination[getAdapterPosition()]);
                    context.startActivity(intent);
                }
            });

        }


    }
}
*/
package it.unimib.travelnotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.travelnotes.Model.Viaggio;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.TravelViewHolder> {

    Context context;

    ArrayList<Viaggio> list;


    public MyAdapter(Context context, ArrayList<Viaggio> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.travel,parent,false);
        return  new TravelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {

        Viaggio viaggio = list.get(position);
        holder.travelDeparture.setText(viaggio.getPartenzaAndata());
        holder.travelDestination.setText(viaggio.getDestinazioneAndata());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TravelViewHolder extends RecyclerView.ViewHolder{

        TextView travelDeparture, travelDestination;

        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);

            travelDeparture = itemView.findViewById(R.id.departure);
            travelDestination = itemView.findViewById(R.id.destination);

        }
    }

}
