package it.unimib.travelnotes.ui.group;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.R;
import it.unimib.travelnotes.ui.attivita.Adapter_attivita;
import it.unimib.travelnotes.ui.attivita.AttivitaFragment;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>  {

    private List<Utente> utenti;
    private GroupFragment context;
    private Utente utente;

    public UserAdapter(List<Utente> users, GroupFragment group) {
        this.utenti = users;
        this.context = group;
    }



    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_group_list,parent,false);
        UserAdapter.UserViewHolder viewHolder = new UserAdapter.UserViewHolder(view);
        return  new UserAdapter.UserViewHolder(view);
    }

    //da modificare con il database
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

        Utente utente = utenti.get(position);
        holder.username.setText(utente.getUsername());
        holder.email.setText(utente.getEmail());
    }

    @Override
    public int getItemCount() {
        return utenti.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView email;



        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email_user);
        }



    }

}