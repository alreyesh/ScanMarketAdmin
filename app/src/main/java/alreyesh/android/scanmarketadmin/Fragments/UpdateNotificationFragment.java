package alreyesh.android.scanmarketadmin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import alreyesh.android.scanmarketadmin.R;


public class UpdateNotificationFragment extends Fragment {
    EditText editTitle;
    EditText editTextShort;
    EditText editTextTextComplete;
    Button btnRegisterNoti;
    private FirebaseFirestore db;
    public UpdateNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.fragment_update_notification, container, false);
        db = FirebaseFirestore.getInstance();
        editTitle = v.findViewById(R.id.editTitle);
        editTextShort= v.findViewById(R.id.editTextShort);
        editTextTextComplete = v.findViewById(R.id.editTextTextComplete);
        btnRegisterNoti = v.findViewById(R.id.btnRegisterNoti);

        btnRegisterNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String textshort = editTextShort.getText().toString();
                String textcomplete = editTextTextComplete.getText().toString();
                if(!title.isEmpty() && !textshort.isEmpty()&& !textcomplete.isEmpty()) {
                    notificarNow();
                }else{
                    Toast.makeText(getActivity(),"Ingrese todos los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });



        return  v;
    }

    private void notificarNow() {
        Map<String,Object> updates = new HashMap<>();
        updates.put("titulo",editTitle.getText().toString());
        updates.put("textocorto",editTextShort.getText().toString());
        updates.put("mensaje",editTextTextComplete.getText().toString());
        updates.put("image","a");
        DocumentReference docRef =  db.collection("notificacion").document( "8gi9zHGh5ETw2i9gbHIC");

        docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editTitle.setText("");
                        editTextShort.setText("");
                        editTextTextComplete.setText("");
                        Toast.makeText(getActivity(),"Se Actualizaron los datos", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}