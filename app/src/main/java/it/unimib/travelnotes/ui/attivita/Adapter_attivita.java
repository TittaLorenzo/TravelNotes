package it.unimib.travelnotes.ui.attivita;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.Model.Viaggio;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.ui.attivita.AttivitaFragment;

public class Adapter_attivita extends RecyclerView.Adapter<Adapter_attivita.AttivitaViewHolder> {
    Attivita[] attivita;
    AttivitaFragment context;

    public Adapter_attivita(Attivita[] attivita, AttivitaFragment activity) {
        this.attivita= attivita;
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
        final Attivita attivitaList = attivita[position];
        holder.attivitaNome.setText(attivitaList.getNome());
        holder.descrizione.setText(attivitaList.getDescrizione());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public class AttivitaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView attivitaNome;
        TextView descrizione;

        public AttivitaViewHolder(@NonNull View itemView) {
            super(itemView);
            attivitaNome = itemView.findViewById(R.id.nome_attivita);
            descrizione = itemView.findViewById(R.id.descrizione);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }
    @Override
    public int getItemCount() {
        if (attivita != null){
            return attivita.length;
        }
        return 0;

    }
}

