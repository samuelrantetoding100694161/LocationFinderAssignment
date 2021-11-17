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

import com.example.assignment2.databinding.FragmentAddressBinding;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AddressFragment extends Fragment {

    private FragmentAddressBinding databinding;
    private static final String TAG = "AddressFragment";
    DatabaseHelper dbhelper;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        dbhelper = new DatabaseHelper(requireContext());
        databinding = FragmentAddressBinding.inflate(inflater, container, false);

        // Create database with 50 locations (10 pre-defined locations & 40 random locations)
        if(dbhelper.isEmpty()){
            Random rand = new Random();//create random class
            dbhelper.addLocation("32 Commencement Dr", "43.94", "-78.89");
            dbhelper.addLocation("Santiago Bernabeu", "40.45", "-3.69");
            dbhelper.addLocation("1 Bass Pro Mills Dr, Vaughan", "43.83", "-79.54");
            dbhelper.addLocation("Kuta Beach, Bali", "-8.72", "115.17");
            dbhelper.addLocation("419 King St W, Oshawa", "43.89", "-78.88");
            dbhelper.addLocation("2000 Simcoe St N", "43.95", "-78.89");
            dbhelper.addLocation("Borobudur Temple, Java", "-7.61", "110.20");
            dbhelper.addLocation("Silverstone Circuit, ", "52.07", "-1.02");
            dbhelper.addLocation("Golden Gate Beach, San Fransisco", "37.82", "-122.48");
            dbhelper.addLocation("555 W Hastings St, Vancouver", "49.28", "-123.11");
            for (int i = 0; i < 40; i++){//create 40 random locations
                DecimalFormat df=new DecimalFormat("#.##");
                Double latitude = rand.nextDouble()* (rand .nextBoolean() ? -90 : 90);//latitude value can only be from -90 to 90
                Double longitude = rand.nextDouble()* (rand .nextBoolean() ? -180 : 180);//longitude value can only be from -180 to 180
                String address = getAddressFromLatLong(Double.parseDouble(df.format(latitude)), Double.parseDouble(df.format(longitude)));//pass the value of latitude and longitude to retrieve address
                dbhelper.addLocation(address, String.format("%.2f", latitude), String.format("%.2f", longitude));//add the value into the database
            }

        }
        return databinding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {//onViewCreated method
        super.onViewCreated(view, savedInstanceState);
        //add button is clicked
        databinding.addLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = databinding.findInputBar.getText().toString();
                //message for the toast widget
                String msg1 = "Data has been added";
                String msg2 = "There is no address, try another address";
                String msg3 = "Please fill the address value in the box";
                if(!address.isEmpty()){//address box is not empty
                    ArrayList<Double> latlong = getLocationAddressMode(address);
                    if(latlong.size()>0){//size of array is bigger than 0
                        //add location to the database
                        long result = dbhelper.addLocation(address, Double.toString(latlong.get(0)), Double.toString(latlong.get(1)));
                        if (result > 0){
                            Toast.makeText(requireContext() , msg1, Toast.LENGTH_SHORT).show();//toast successful message
                            Log.d(TAG,"add data");//debugger for adding location
                        }
                    }else{
                        Toast.makeText(requireContext() , msg2, Toast.LENGTH_SHORT).show();//toast no address message
                        Log.d(TAG,"no address");//debugger if there is no address
                    }
                }else{
                    Toast.makeText(requireContext() , msg3, Toast.LENGTH_SHORT).show();//toast for empty address
                    Log.d(TAG,"address box is empty");//debugger for empty box of address
                }
            }
        });

        //find button is clicked
        databinding.findLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = databinding.findInputBar.getText().toString();//gettext from the input
                //message for toast widget
                String msg1 = "There is no address in the database";
                ArrayList<String> latlong = getLatLongDB(address);
                if(latlong.size() > 0){//size of array bigger than 0
                    databinding.latitude.setText(latlong.get(0));//set latitude from args 0
                    //change the visibility of the textview to display the value of latitude and longitude
                    databinding.latitudeLabel.setVisibility(View.VISIBLE);
                    databinding.latitude.setVisibility(View.VISIBLE);
                    databinding.longitude.setText(latlong.get(1));//set langitude from args 1
                    databinding.longitudeLabel.setVisibility(View.VISIBLE);
                    databinding.longitude.setVisibility(View.VISIBLE);
                    Log.d(TAG,"latitude and longitude found");//debugger for latitude and longitude found
                }else{
                    Toast.makeText(requireContext() , msg1, Toast.LENGTH_SHORT).show();//toast message for no address
                    Log.d(TAG,"no address");//debugger for no address
                }
            }
        });

        //delete button is clicked
        databinding.deleteLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = databinding.findInputBar.getText().toString();//get the value from the box
                int result = dbhelper.deleteLocation(address);//delete location method
                //message for the toast widget
                String msg1 = "Data has been deleted";
                String msg2 = "There is no data of the address";
                if (result > 0){//if the value is not found, delete is working
                    Toast.makeText(requireContext(), msg1, Toast.LENGTH_SHORT).show();//toast message for delete
                    Log.d(TAG,"delete success");//debugger for delete success
                }else{
                    Toast.makeText(requireContext(), msg2, Toast.LENGTH_SHORT).show();//toast for no address
                    Log.d(TAG,"no address");//debugger for no address deleted
                }
            }
        });
        //enter latitude and longitude switch button is clicked to switch to latitude and longitude menu
        databinding.switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AddressFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        //update button is clicked
        databinding.updateLocbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = databinding.findInputBar.getText().toString();
                //message for the toast widget
                String msg1 = "Data has been updated";
                String msg2 = "There is no " + address + " in the database";
                String msg3 = "There is no data of the address";
                String msg4 = "Fill the address";
                if(!address.isEmpty()) {
                    ArrayList<Double> latlong = getLocationAddressMode(address);
                    if (latlong.size() > 0) {
                        int success = dbhelper.updateLocation(address, String.format("%.2f", latlong.get(0)), String.format("%.2f", latlong.get(1)));
                        if (success > 0) {
                            Toast.makeText(requireContext(), msg1, Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"update success");//debugger for update success
                        } else {
                            Toast.makeText(requireContext(), msg2, Toast.LENGTH_SHORT).show();
                            Log.d(TAG,"no address");//debugger for no address updated
                        }
                    } else {
                        Toast.makeText(requireContext(), msg3, Toast.LENGTH_SHORT).show();
                        Log.d(TAG,"no address");//debugger for no address
                    }
                }else{
                    Toast.makeText(requireContext(), msg4, Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"address box is empty");//debugger empty address box
                }
            }
        });

    }

    @Override
    public void onDestroyView() {//onDestroyView method
        super.onDestroyView();
        databinding = null;//set no databinding
    }

    // Retrieve the latitude and longitude from the database given the address
    public ArrayList<String> getLatLongDB(String address){
        Cursor data = dbhelper.getLatLong(address);
        ArrayList<String> latlong = new ArrayList<>();
        if(data.moveToNext()){
            latlong.add(data.getString(2));
            latlong.add(data.getString(3));
        }
        return latlong;
    }


    // Retrieve latitude and longitude using geocoding given the address
    public ArrayList<Double> getLocationAddressMode(String address){

        if (Geocoder.isPresent()) {//geocoder is found
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            ArrayList<Double> latlong = new ArrayList<>();
            try {
                List<Address> ls= geocoder.getFromLocationName(address,1);//retrieve the lat and long from location name
                for (Address address1: ls) {//get the latitude and longitude of address
                    latlong.add(address1.getLatitude());
                    latlong.add(address1.getLongitude());
                }
                return latlong;//return latitude and longitude
            } catch (IOException e) {//exception thread
                e.printStackTrace();
            }}
        return null;
    }

    // Retrieve address using geocoding given the latitude and longitude
    public String getAddressFromLatLong(double lat, double longi){

        if (Geocoder.isPresent()){//geocoder is found
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                if ((-90 <= lat && lat < 90) && (-180 <= longi && longi <= 180)){// lat from -90 to 90 and longi from -180 to 180
                    List<Address> ls = geocoder.getFromLocation(lat,  longi, 1);// maximum 1 lat and longi for each location
                    String address = "";
                    for (Address address1: ls) {
                        address = address1.getAddressLine(0);//get the address line
                    }
                    return address;
                }
            } catch (IOException e) {//exception thread
                e.printStackTrace();
            }}
        return "";
    }
}