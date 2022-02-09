package it.unimib.travelnotes.ui.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import it.unimib.travelnotes.Model.Utente;
import it.unimib.travelnotes.R;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>  {

    Context context;

    ArrayList<Utente> utenti;
    String s1;

    public UserAdapter(Context context, ArrayList<Utente> utenti) {
        this.utenti = utenti;
        this.context = context;
    }



    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_group_list,parent,false);
        return  new UserAdapter.UserViewHolder(view);
    }

    //da modificare con il database
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Utente utente = utenti.get(position);
        holder.username.setText(Utente.getUsername());
    }

    @Override
    public int getItemCount() {
        return utenti.size();
    }


    public class UserViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView usersurname;
        String s1;



        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
        }



    }

}