package it.unimib.travelnotes.ui.attivita;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.ui.newactivityevent.NewActivityEvent;

public class Adapter_attivita extends RecyclerView.Adapter<Adapter_attivita.AttivitaViewHolder> {
    private List<Attivita> attivitaList;
    private AttivitaFragment context;
    private Attivita attivita;

    String pattern = "MM/dd/yyyy \n HH:mm";
    DateFormat df = new SimpleDateFormat(pattern);


    public Adapter_attivita(List<Attivita> attivita, AttivitaFragment activity) {
        this.attivitaList= attivita;
        this.context = activity;
    }



    @NonNull
    @Override
    public AttivitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.attivita,parent,false);
        Adapter_attivita.AttivitaViewHolder viewHolder = new Adapter_attivita.AttivitaViewHolder(view);
        return viewHolder;
    }
    //da modificare con il database
    @Override
    public void onBindViewHolder(@NonNull Adapter_attivita.AttivitaViewHolder holder, int position) {
        //AttivitaViewHolder.getTextView(localDataSet[position]);
        attivita = attivitaList.get(position);
        holder.idAttivita =attivita.getAttivitaId();
        Date dataInizio = attivita.getDataInizio();
        String stringaDataInizio = df.format(dataInizio);
        holder.dataInizio.setText(stringaDataInizio);
        Date dataFine = attivita.getDataFine();
        String stringaDataFine = df.format(dataFine);
        holder.dataFine.setText(stringaDataFine);
        holder.attivitaNome.setText(attivita.getNome());
        holder.descrizione.setText(attivita.getDescrizione());
        holder.posizione.setText(attivita.getPosizione());

    }
    public void removeItem(int position) {
        attivitaList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Attivita att, int position) {
        attivitaList.add(position, att);
        notifyItemInserted(position);
    }

    public List<Attivita> getData() {
        return attivitaList ;
    }
    @Override
    public int getItemCount() {
        if (attivitaList != null){
            return attivitaList.size();
        }
        return 0;

    }


    public class AttivitaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        String idAttivita;
        TextView attivitaNome;
        TextView descrizione;
        TextView dataInizio;
        TextView dataFine;
        TextView posizione;
        ImageButton modificaAttivita;


        public AttivitaViewHolder(@NonNull View itemView) {
            super(itemView);
            attivitaNome = itemView.findViewById(R.id.nome_attivita);
            descrizione = itemView.findViewById(R.id.descrizione);
            posizione= itemView.findViewById(R.id.pozione);
            dataInizio = itemView.findViewById(R.id.data_inizio);
            dataFine = itemView.findViewById(R.id.data_fine);

            modificaAttivita =  itemView.findViewById(R.id.button_modifica_attivita);
            modificaAttivita.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = (new Intent(view.getContext(), NewActivityEvent.class));
                    intent.putExtra("modifica_attivita", true);
                    intent.putExtra("idAttivita",idAttivita);
                    view.getContext().startActivity(intent);



                }
            });
            posizione.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String posizioneStr = posizione.getText().toString();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("geo:0,0?q=" + Uri.encode(posizioneStr)));

                    if (i.resolveActivity(v.getContext().getPackageManager()) != null) {
                        v.getContext().startActivity(i);
                    }
                }
            });
        }


        @Override
        public void onClick(View v) {

        }
    }


}

