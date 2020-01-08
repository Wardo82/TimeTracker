package core.ds.optionsmenu.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import core.ds.optionsmenu.Adapters.CActivityHorizontalRecyclerAdapter;
import core.ds.optionsmenu.Model.CActivity;
import core.ds.optionsmenu.Model.CClock;
import core.ds.optionsmenu.Model.CProject;
import core.ds.optionsmenu.Model.CTask;
import core.ds.optionsmenu.Model.CTimeTrackerEngine;
import core.ds.optionsmenu.R;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    public static final String TIMETRACKER_TASK_NAME = "timetracker.TASKNAME";
    public static final String TIMETRACKER_PROJECT_NAME = "timetracker.PROJECTNAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        /*
        // Time in milliseconds of the rate at which the Clock will count the seconds
        long timeUnit = 2000;
        // First and only instance of CTimeTrackerEngine
        CTimeTrackerEngine TimeTracker = CTimeTrackerEngine.getInstance();
        TimeTracker.setTimeUnit(timeUnit);
        // First and only instance of CClock
        CClock Clock = CClock.getInstance();
        Clock.start(); // Start the Clock in its own Thread
        // Add TimeTracker as Listener to the steps of the Clock
        Clock.addPropertyChangeListener(TimeTracker);

        // === Start of tree creation ===
        // P1 i P2 son projectes arrel
        // Create P1
        TimeTracker.addActivity("P1", new CProject("P1", "Project 1"));
        // Create P2
        TimeTracker.addActivity("P2", new CProject("P2", "Project 2"));
        // Create P3
        TimeTracker.addActivity("P3", new CProject("P3", "Project 3"));
        // Create P4
        TimeTracker.addActivity("P4", new CProject("P4", "Project 4"));
        // Create P5
        TimeTracker.addActivity("P5", new CProject("P5", "Project 5"));
        // Create P6
        TimeTracker.addActivity("P6", new CProject("P6", "Project 6"));
        // Create P7
        TimeTracker.addActivity("P7", new CProject("P7", "Project 7"));
        // Create P8
        TimeTracker.addActivity("P8", new CProject("P8", "Project 8"));
        // P1.2 es subprojecte de P1
        CProject P1 = (CProject) TimeTracker.getActivity("P1");
        // Create T3 in P1
        P1.appendActivity(new CProject("P1.2", "Project P1.2"));
        // El projecte P1 conte les tasques T1 i T2.
        P1.appendActivity(new CTask("T1", "Task 1")); // Task T1
        P1.appendActivity(new CTask("T2", "Task 2")); // Task T2
        // El projecte P1.2 conte la tasca T4. El projecte
        CProject P1_2 = (CProject) P1.getActivity("P1.2");
        P1_2.appendActivity(new CTask("T4", "Task 4")); // Task T4
        // P2 conte la tasca T3.
        CProject P2 = (CProject) TimeTracker.getActivity("P2");
        P2.appendActivity(new CTask("T3", "Task 3")); // Task T3

        fita1(P1, P2);*/

        Intent mainProjectActivity = new Intent(MainActivity.this,
                MainProjectsActivity.class);
        startActivity(mainProjectActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    public static void fita1(final CProject P1, final CProject P2) {
        // 3. Comencar a cronometrar la tasca T3. A partir d'aquest moment s'ha
        // de s'ha de mostrar com es va comptant el temps imprimint cada 2 segons
        // com a les taules 1 a 3. No cal que us hi feu en el format de la taula,
        // nomes que les columnes estiguin ben indendates per poder llegir-ne el
        // contingut. No cal sobreescriure la taula si no voleu, simplement anar-la
        // reimprimint.
        System.out.println("3. Comencar a cronometrar la tasca T3.");
        P1.trackTaskStart("T1");
        // 4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de
        // veure ara la taula 1.
        try {
            System.out.println("4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de veure ara la taula 1.");
            sleep(300);
        } catch (InterruptedException e) {

        }
        P1.trackTaskStop("T1");

        // 5. Esperar 7 segons mes.
        try {
            System.out.println("5. Esperar 7 segons mes.");
            sleep(700);
        } catch (InterruptedException e) {

        }

        // 6. Engegar el cronometre per la tasca T2.
        P2.trackTaskStart("T3");

        // 7. Esperar 10 segons i llavors parar el cronometratge de T2.
        try{
            System.out.println("7. Esperar 10 segons i llavors parar el cronometratge de T2.");
            sleep(1000);
        }catch (InterruptedException e) {

        }
        P2.trackTaskStop("T3");

        // 8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.
        P1.trackTaskStart("T1");
        try {
            System.out.println("8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.");
            sleep(200);
        }catch (InterruptedException e){

        }
        P1.trackTaskStop("T1");
    }
}
