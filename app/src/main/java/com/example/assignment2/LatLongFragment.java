package com.example.assignment2;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.assignment2.databinding.FragmentLatlongBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LatLongFragment extends Fragment {
    //variable declaration
    private FragmentLatlongBinding databinding;
    private static final String TAG = "LatLongFragment";//tag for debugger
    DatabaseHelper dbhelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        dbhelper = new DatabaseHelper(requireContext());
        databinding = FragmentLatlongBinding.inflate(inflater, container, false);
        return databinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //add button is clicked
        databinding.addLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = null;
                String lat = databinding.latInputBar.getText().toString();//get latitude input value
                String longi = databinding.longInputBar.getText().toString();//get longitude input value
                //toast message for the adding functionality
                String msg1 = "Data has been added";
                String msg2 = "Error adding data";
                String msg3 = "Please fill the latitude and longitude value in the box";

                if(!lat.isEmpty() && !longi.isEmpty()){//lat and longi value is not empty
                    address = getAddressFromLatLong(Double.parseDouble(lat), Double.parseDouble(longi));//get the address according to the value
                    if(!address.isEmpty()) {//no address is found in database
                        long result = dbhelper.addLocation(address, lat, longi);//add the location to database
                        if (result > 0) {//if added
                            Toast.makeText(requireContext(), msg1, Toast.LENGTH_LONG).show();//toast message success
                            Log.d(TAG,"data added");//debugger for add data
                        } else {
                            Toast.makeText(requireContext(), msg2, Toast.LENGTH_LONG).show();//toast message error
                            Log.d(TAG,"error adding");//debugger for no add data
                        }
                    }
                }else{
                    //toast and debugger for no data added by empty lat and long input value
                    Toast.makeText(requireContext() , msg3, Toast.LENGTH_LONG).show();
                    Log.d(TAG,"no data");
                }
            }
        });
        //find button is clicked
        databinding.findLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the input value of longi and lat
                String lat = databinding.latInputBar.getText().toString();
                String longi = databinding.longInputBar.getText().toString();
                String address = getAddressDB(lat, longi);//get address based on lat and longi
                //toast message
                String msg1 = "There is no latitude and longitude find in the database";
                if(!address.isEmpty()){
                    //set the visibility of the address view to display the address value
                    databinding.address.setText(address);
                    databinding.address.setVisibility(View.VISIBLE);
                    databinding.addressLabel.setVisibility(View.VISIBLE);
                }else{
                    //toast and debugger if no lat and longi found in database
                    Toast.makeText(requireContext() , msg1, Toast.LENGTH_LONG).show();
                    Log.d(TAG,"no latitude and longitude found");

                }
            }
        });

        //enter button is clicked to switch to address menu
        databinding.switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LatLongFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }

    // Retrieve address from the database given the latitude and longitude
    public String getAddressDB(String lat, String longi){
        Cursor data = dbhelper.getAddress(lat, longi);
        String address = "";
        if(data.moveToNext()){
            address = data.getString(1);
        }
        return address;
    }

    // Retrieve address using geocoding given the latitude and longitude
    public String getAddressFromLatLong(double lat, double longi){

        if (Geocoder.isPresent()){//geocoder is found
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                if ((-90 <= lat && lat < 90) && (-180 <= longi && longi <= 180)){// lat value from -90 to 90 and longi from -180 to 180
                    List<Address> ls = geocoder.getFromLocation(lat,  longi, 1);//maximum 1 lat and long for each location address
                    String address = "";
                    for (Address addr: ls) {
                        address = addr.getAddressLine(0);//get the address
                    }
                    return address;
                }
            } catch (IOException e) {//exception thread
                e.printStackTrace();
            }}
        return "";
    }
}