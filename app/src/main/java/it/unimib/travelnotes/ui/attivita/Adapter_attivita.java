package it.unimib.travelnotes.ui.attivita;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.unimib.travelnotes.Model.Attivita;
import it.unimib.travelnotes.R;

public class Adapter_attivita extends RecyclerView.Adapter<Adapter_attivita.AttivitaViewHolder> {
    private List<Attivita> attivitaList;
    private AttivitaFragment context;
    private Attivita attivita;

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
        holder.attivitaNome.setText(attivita.getNome());
        holder.descrizione.setText(attivita.getDescrizione());

    }
    public void removeItem(int position) {
        attivitaList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Attivita attivita, int position) {
        attivitaList.add(attivita);
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
        TextView attivitaNome;
        TextView descrizione;

        public AttivitaViewHolder(@NonNull View itemView) {
            super(itemView);
            attivitaNome = itemView.findViewById(R.id.nome_attivita);
            descrizione = itemView.findViewById(R.id.descrizione);
        }


        @Override
        public void onClick(View v) {

        }
    }


}

