package it.unimib.travelnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.unimib.travelnotes.Model.Viaggio;

public class MyAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Viaggio> list;

    String dateTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;


    public MyAdapter(Context context, ArrayList<Viaggio> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position){
        Viaggio viaggio;
       if (list.size() == 1) {
             viaggio = list.get(0);
        }else{
        viaggio = list.get(position);
        }
        if(viaggio.getDurataRitorno() != 0 ){
            return 1;
        }
        else
            return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View view = LayoutInflater.from(context).inflate(R.layout.travel,parent,false);
            TravelViewHolder holder = new TravelViewHolder(view);
            return holder;
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.travel_a,parent,false);
            TravelAViewHolder holder = new TravelAViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(this.getItemViewType(position)==0){
            Viaggio viaggio = list.get(position);
            TravelAViewHolder viewHolder = (TravelAViewHolder) holder;
            viewHolder.idViaggio= viaggio.getViaggioId();
            viewHolder.travelDeparture.setText(viaggio.getPartenzaAndata());
            viewHolder.travelDestination.setText(viaggio.getDestinazioneAndata());


            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDate.setText(dateTime);

            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDateArrival.setText(dateTime);

            //viewHolder.travelDate.setText(viaggio.getDataAndata().toString());

        }
        else{
            Viaggio viaggio = list.get(position);
            TravelViewHolder viewHolder = (TravelViewHolder) holder;
            viewHolder.idViaggio= viaggio.getViaggioId();
            viewHolder.travelDeparture.setText(viaggio.getPartenzaAndata());
            viewHolder.travelDestination.setText(viaggio.getDestinazioneAndata());
            viewHolder.travelDepartureR.setText(viaggio.getPartenzaRitorno());
            viewHolder.travelDestinationR.setText(viaggio.getDestinazioneRitorno());

            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDate.setText(dateTime);

            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDateArrival.setText(dateTime);

            //viewHolder.travelDate.setText(viaggio.getDataAndata().toString());

            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDateR.setText(dateTime);
            //viewHolder.travelDateR.setText(viaggio.getDataAndata().toString());

            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy \n HH:mm");
            dateTime = simpleDateFormat.format(viaggio.getDataAndata()).toString();
            viewHolder.travelDateRArrival.setText(dateTime);
        }
    }



    @Override
    public int getItemCount() {
        return list.size();
    }


    public class TravelViewHolder extends RecyclerView.ViewHolder{

        String idViaggio;

        TextView travelDeparture;
        TextView travelDestination;
        TextView travelDepartureR;
        TextView travelDestinationR;
        TextView travelDate;
        TextView travelDateArrival;
        TextView travelDateR;
        TextView travelDateRArrival;
        // TextView travelTime;


        public TravelViewHolder(@NonNull View itemView) {
            super(itemView);
            travelDeparture = itemView.findViewById(R.id.departure);
            travelDestination = itemView.findViewById(R.id.destination);
            travelDepartureR = itemView.findViewById(R.id.departure2);
            travelDestinationR = itemView.findViewById(R.id.destination2);
            travelDate = itemView.findViewById(R.id.departureTime);
            travelDateArrival= itemView.findViewById(R.id.arrivalTime);
            travelDateR = itemView.findViewById(R.id.departureTime2);
            travelDateRArrival= itemView.findViewById(R.id.arrivalTime2);
            //travelTime = itemView.findViewById(R.id.time);
            /*MODIFICARE CON DATA E ORA
            textViewName = itemView.findViewById(R.id.date);
            textViewDate = itemView.findViewById(R.id.time);
*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Activity_travel_view.class);
                    intent.putExtra("viaggioId", idViaggio);
                    intent.putExtra("a_r", true);
                    //intent.putExtra("partenzaR", rDeparture[getAdapterPosition()]);
                    //intent.putExtra("arrivoR", rDestination[getAdapterPosition()]);
                    context.startActivity(intent);
                }
            });

        }



    }

    public class TravelAViewHolder extends RecyclerView.ViewHolder{

        String idViaggio;

        TextView travelDeparture;
        TextView travelDestination;
        TextView travelDate;
        TextView travelDateArrival;
        // TextView travelTime;


        public TravelAViewHolder(@NonNull View itemView) {
            super(itemView);
            travelDeparture = itemView.findViewById(R.id.departure);
            travelDestination = itemView.findViewById(R.id.destination);
            travelDate = itemView.findViewById(R.id.departureTime);
            travelDateArrival= itemView.findViewById(R.id.arrivalTimeS);
            //travelTime = itemView.findViewById(R.id.time);
            /*MODIFICARE CON DATA E ORA
            textViewName = itemView.findViewById(R.id.date);
            textViewDate = itemView.findViewById(R.id.time);
*/

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Activity_travel_view.class);
                    intent.putExtra("viaggioId", idViaggio);
                    intent.putExtra("a_r", false);
                    //intent.putExtra("partenzaR", rDeparture[getAdapterPosition()]);
                    //intent.putExtra("arrivoR", rDestination[getAdapterPosition()]);
                    context.startActivity(intent);
                }
            });

        }



    }




}
