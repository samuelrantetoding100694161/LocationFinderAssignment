package com.example.assignment2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.assignment2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    //variable declaration for the AppBarConfiguration and ActivityMainBinding for the fragments
    private AppBarConfiguration newAppBarConfiguration;
    private ActivityMainBinding databinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//onCreate method
        super.onCreate(savedInstanceState);
        databinding = ActivityMainBinding.inflate(getLayoutInflater());//generate binding class
        setContentView(databinding.getRoot());//set content based on the root of the binding
    }

    @Override
    public boolean onSupportNavigateUp() {//navigation controller based on nav_graph.xml
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_latlong);
        return NavigationUI.navigateUp(navController, newAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}