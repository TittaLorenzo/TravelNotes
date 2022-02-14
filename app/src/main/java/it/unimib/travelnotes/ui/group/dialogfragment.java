package it.unimib.travelnotes.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;



import it.unimib.travelnotes.R;


public class dialogfragment extends DialogFragment {
    Button add_user;
    EditText editInput;
    private GruppoViaggioViewModel mGruppoViaggioViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_group, null);
        add_user = view.findViewById(R.id.add_user_2);

        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInput = new EditText(getActivity().getApplicationContext());
                String resEmail = editInput.getText().toString();

                mGruppoViaggioViewModel.aggiungiAlGruppo(resEmail.trim());

                getDialog().dismiss();
            }
        });


        return view;
    }
}