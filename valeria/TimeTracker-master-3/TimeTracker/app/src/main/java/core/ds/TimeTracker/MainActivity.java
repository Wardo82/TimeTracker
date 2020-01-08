package core.ds.TimeTracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuration);

        long timeUnit = 2000;
        CTimeTrackerEngine TimeTracker = CTimeTrackerEngine.getInstance(); // First and only instance of CTimeTrackerEngine
        TimeTracker.setTimeUnit(timeUnit); // Set the above mentioned timeUnit of the Clock
        CClock Clock = CClock.getInstance(); // First and only instance of CClock
        Clock.start(); // Start the Clock in its own Thread
        Clock.addPropertyChangeListener(TimeTracker); // Add TimeTracker as Listener to the steps of the Clock

        TimeTracker.addActivity("P1", new CProject("P1", "Project 1") ); // Create P1
        CProject P1 = (CProject) TimeTracker.getActivity("P1"); // Get a reference to P1


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.configuration);
        NavigationView navigationView = findViewById(R.id.conf_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_configuration_open, R.string.navigation_configuration_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Esto es para que salga algo al principio pero no hará falta:
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DeleteFragment()).commit();
            navigationView.setCheckedItem(R.id.delete);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.delete:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new DeleteFragment()).commit();
                break;

            case R.id.add:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddFragment()).commit();
                break;

            case R.id.report:
                Toast.makeText(this,"Report", Toast.LENGTH_SHORT).show();
                break;
            case R.id.orderby:
                Toast.makeText(this,"Order By", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true; //return true means no item selected
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
}