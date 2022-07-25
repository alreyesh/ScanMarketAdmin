package alreyesh.android.scanmarketadmin.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import alreyesh.android.scanmarketadmin.Activities.MainActivity;
import alreyesh.android.scanmarketadmin.Model.Order;
import alreyesh.android.scanmarketadmin.R;
import alreyesh.android.scanmarketadmin.Utils.Util;


public class HomeFragment extends Fragment {
    Button btnScan;
    TextView txtResultado;
    SharedPreferences prefs;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        prefs = Util.getSP(getContext());
        btnScan = view.findViewById(R.id.btnScan);
        txtResultado = view.findViewById(R.id.txtResultado);
        txtResultado.setText("Resultado");
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanOptions options = new ScanOptions();
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("Scan a barcode");
                options.setCameraId(0);  // Use a specific camera of the device
                options.setBeepEnabled(true);
                options.setBarcodeImageEnabled(true);
                options.setOrientationLocked(false);

                barcodeLauncher.launch(options);
            }
        });


        return view;
    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                    txtResultado.setText("Resultado");
                } else {
                    Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    txtResultado.setText(result.getContents());
                        changeResult(result.getContents());
                }
            });

    private void changeResult(String contents) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("json",contents);
        editor.apply();
        CartFragment cartFragment = new CartFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.content_frame, cartFragment)
                .commit();



      /*  JsonParser parser = new JsonParser();
        List  producto = new ArrayList();
        JsonArray gsonArr = parser.parse(contents).getAsJsonArray();
        for (JsonElement obj : gsonArr){
            JsonObject gsonObj = obj.getAsJsonObject();
            String user = gsonObj.get("user").getAsString();
            String date = gsonObj.get("date").getAsString();

            JsonArray  productos = gsonObj.get("productos").getAsJsonArray();

            for(JsonElement pro : productos){
                producto.add(pro.getAsString());
            }

       */

            // list
        /*    String cod
            String name
                    String link
                            String cantidad
                                    String subtotal

        }

         */




    }

}