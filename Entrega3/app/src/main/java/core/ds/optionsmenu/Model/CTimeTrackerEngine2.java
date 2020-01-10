package core.ds.optionsmenu.Model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

import core.ds.optionsmenu.Activities.AddActivity;
import core.ds.optionsmenu.Activities.EditActivity;
import core.ds.optionsmenu.Activities.MainActivity;
import core.ds.optionsmenu.Activities.ProjectActivity;
import core.ds.optionsmenu.Activities.TaskActivity;

/**
 *
 */
public class CTimeTrackerEngine2 extends Service implements IUpdatable {

    /**
     * Called when the service is called for the first time. Makes
     * a few initializations.
     * Stablishes the type of intent to which an answer is given (see
     * <li>{@link Receiver}</li>.
     * Creates the handler that updates the UI and creates and starts
     * the clock to keep track of the tasks.
     */
    public final void onCreate() {
        Log.d(TAG, "onCreate");

        IntentFilter filter;
        filter = new IntentFilter();
        // Main activity
        filter.addAction(MainActivity.GIVE_CHILDREN);
        filter.addAction(MainActivity.UPPER_LEVEL);
        filter.addAction(MainActivity.LOWER_LEVEL);
        filter.addAction(MainActivity.START_CHRONO);
        filter.addAction(MainActivity.STOP_CHRONO);
        filter.addAction(MainActivity.SAVE_TREE);
        filter.addAction(MainActivity.STOP_SERVICE);
        // Project activity
        filter.addAction(ProjectActivity.GIVE_CHILDREN);
        filter.addAction(ProjectActivity.LOWER_LEVEL);
        filter.addAction(ProjectActivity.UPPER_LEVEL);
        filter.addAction(ProjectActivity.START_CHRONO);
        filter.addAction(ProjectActivity.STOP_CHRONO);
        filter.addAction(ProjectActivity.ERASE_ELEMENT);
        // Task activity
        filter.addAction(TaskActivity.GIVE_CHILDREN);
        filter.addAction(TaskActivity.UPPER_LEVEL);
        filter.addAction(TaskActivity.START_CHRONO);
        filter.addAction(TaskActivity.STOP_CHRONO);
        // Edit activity
        filter.addAction(EditActivity.EDIT_ELEMENT);
        // Add activity
        filter.addAction(AddActivity.ADD_ELEMENT);

        m_receiver = new Receiver();
        registerReceiver(m_receiver, filter);
        m_uiUpdater = new CUpdater(this, m_refreshPeriod, TAG);

        // Escollir la opció desitjada d'entre ferArbreGran, llegirArbreArxiu i
        // ferArbrePetitBuit. Podríem primer fer l'arbre gran i després, quan
        // ja s'hagi desat, escollir la opció de llegir d'arxiu.
        final int option = ferArbrePetitBuit;
        carregaArbreActivitats(option);
        m_currentParent = m_root;

        Log.d(TAG, "Root has " + m_root.getChildren().size() + " children");

        // Starts the clock
        m_clock = CClockUpdatable.Instance();
        m_clock.start();

    }

    /**
     * Containes the method <code>onReceive</code> where service is provided.
     * Here the Intents from activities are captured and handled.
     */
    private class Receiver extends BroadcastReceiver {
        /**
         * Name of the class to identify when logging or message passing.
         * @see Log
         */
        private final String TAG = this.getClass().getSimpleName();

        /**
         * Handles all Intents sent. It consists on chekcing the type
         * of intent through the action and handles it accordingly.
         * @param context Context
         * @param intent Object that arrives through Broadcast and which
         *               we make use of the attribute action to know what
         *               type of Intent it is and the extras to get important
         *               data.
         */
        @Override
        public final void onReceive(final Context context,
                                    final Intent intent) {
            Log.d(TAG, "onReceive");
            String action = intent.getAction();
            Log.d(TAG, "Action = " + action);
            if ((action.equals(MainActivity.START_CHRONO))
                    || (action.equals(MainActivity.STOP_CHRONO))
                    || (action.equals(ProjectActivity.START_CHRONO))
                    || (action.equals(ProjectActivity.STOP_CHRONO))
                    || (action.equals(TaskActivity.START_CHRONO))
                    || (action.equals(TaskActivity.STOP_CHRONO))) {
                String taskName = intent.getStringExtra("CLICKED_TASK");
                String parentName = intent.getStringExtra("PARENT_NAME");
                CTask clickedTask; // Get the task where the button was clicked
                if (m_currentParent == m_root) {
                    // First get the high level project
                    CProject project = (CProject) ((CProject) m_currentParent)
                            .getActivity(parentName);
                    // Get the activity where the user placed the finger
                    clickedTask = (CTask) project.getActivity(taskName);
                } else {
                    // The parent could be a task
                    if (m_currentParent instanceof CTask) {
                        clickedTask = (CTask) m_currentParent;
                    } else {
                        // Get the activity where the user placed the finger
                        clickedTask = (CTask) ((CProject) m_currentParent)
                                .getActivity(taskName);
                    }
                }
                // Start tracking the task
                if (action.equals(MainActivity.START_CHRONO)
                        || (action.equals(ProjectActivity.START_CHRONO))
                        || (action.equals(TaskActivity.START_CHRONO))) {
                    try {
                        clickedTask.trackTaskStart();
                        CTimeTrackerEngine.getInstance().m_trackedTask
                                .put(clickedTask.getName(), clickedTask);
                        m_trackedTasks.add(clickedTask);
                        m_uiUpdater.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(TAG, "Tried to chronometer task "
                                + clickedTask.getName()
                                + " that is already being tracked.");
                    }
                }
                if (action.equals(MainActivity.STOP_CHRONO)
                        || (action.equals(ProjectActivity.STOP_CHRONO))
                        || (action.equals(TaskActivity.STOP_CHRONO))) {
                    try {
                        clickedTask.trackTaskStop();
                        CTimeTrackerEngine.getInstance().m_trackedTask
                                .remove(clickedTask.getName());
                        m_trackedTasks.remove(clickedTask);
                        if (m_trackedTasks.size() == 0) {
                            sendChildren();
                            m_uiUpdater.stop();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w(TAG, "Tried to stop task "
                                + clickedTask.getName()
                                + " that is already stopped.");
                    }
                }
                Log.d(TAG, "There are " + m_trackedTasks.size()
                        + " tasks being tracked.");
            } else if (action.equals(MainActivity.SAVE_TREE)) {
                save();
                Log.d(TAG, "Tree saved");
            } else if (action.equals(MainActivity.GIVE_CHILDREN)
                    || (action.equals(ProjectActivity.GIVE_CHILDREN))
                    || (action.equals(TaskActivity.GIVE_CHILDREN))) {
                sendChildren();
            } else if (action.equals(MainActivity.UPPER_LEVEL)
                    || (action.equals(ProjectActivity.UPPER_LEVEL))
                    || (action.equals(TaskActivity.UPPER_LEVEL))) {
                m_currentParent = m_currentParent.getProjectParent();
            } else if (action.equals(MainActivity.LOWER_LEVEL)
                    || action.equals(ProjectActivity.LOWER_LEVEL)) {
                // We go down one level and send intervals if is a task
                // and activities if its a project
                String clickedName = intent.getStringExtra(
                        "CLICKED_ACTIVITY_NAME");
                String parentName = intent.getStringExtra("PROJECT_NAME");
                // The activity where the user placed the finger
                CActivity clickedActivity;
                if (m_currentParent == m_root) {
                    // First get the high level project
                    CProject project = (CProject) ((CProject) m_currentParent)
                            .getActivity(parentName);
                    // Get the activity where the user placed the finger
                    clickedActivity = project.getActivity(clickedName);
                    // Get the clicked activity
                    m_currentParent = clickedActivity;
                } else {
                    // Get the activity where the user placed the finger
                    m_currentParent = ((CProject) m_currentParent)
                            .getActivity(clickedName);

                }
            } else if (action.equals(MainActivity.ERASE_ELEMENT)
                    || action.equals(ProjectActivity.ERASE_ELEMENT)
                    || action.equals(TaskActivity.ERASE_ELEMENT)) {
                String swipeName = intent.getStringExtra(
                        "SWIPE_ACTIVITY_NAME");
                String parentName = intent.getStringExtra("PROJECT_NAME");
                CProject parentProject;
                if (m_currentParent == m_root) {
                    // First get the high level project
                    CProject project = (CProject) ((CProject) m_currentParent)
                            .getActivity(parentName);
                    // Get the activity where the user placed the finger
                    project.eraseElement(swipeName);
                } else {
                    if(m_currentParent instanceof CTask) {
                        m_currentParent.eraseElement(swipeName);
                    } else {
                        // Get the activity that the user wants to edit
                        if (swipeName
                                .equals(parentName)) {
                            // In one case the father is above
                            parentProject = m_currentParent.getProjectParent();
                            // This is the children
                        } else {
                            // In the other this is the father
                            parentProject = (CProject) m_currentParent;
                        }
                        parentProject.eraseElement(swipeName);

                        if (parentProject.getProjectParent().isRoot()) {
                            m_currentParent = m_root;
                        } else {
                            m_currentParent = parentProject;
                        }
                    }
                }
                sendChildren();
                Log.d(TAG, "Element deleted");
            } else if (action.equals(MainActivity.STOP_SERVICE)) {
                stopService();
            } else if (action.equals(EditActivity.EDIT_ELEMENT)){
                // Receiver information from activity
                String editActivityName = intent.getStringExtra(
                        "EDIT_ACTIVITY_NAME");
                String projectParentName = intent.getStringExtra(
                        "PROJECT_NAME");
                String newDescription = intent.getStringExtra(
                        "NEW_DESCRIPTION");
                String newName = intent.getStringExtra(
                        "NEW_NAME");
                CProject parentProject;
                CActivity act;
                if (m_currentParent == m_root) {
                    // First get the high level project
                    act = m_root.getActivity(editActivityName);
                    parentProject = m_root;
                } else {
                    // Get the activity that the user wants to edit
                    if (editActivityName
                            .equals(projectParentName)) {
                        // In one case the father is above
                        parentProject = m_currentParent.getProjectParent();
                        // This is the children
                        act = m_currentParent;
                    } else {
                        // In the other this is the father
                        parentProject = (CProject) m_currentParent;
                        // The children is below
                        act = parentProject.getActivity(editActivityName);
                    }
                }
                act.setDescription(newDescription);
                act.setName(newName);
                parentProject.eraseElement(editActivityName);
                parentProject.appendActivity(act);
                m_currentParent = editActivityName.equals(projectParentName)
                        ? parentProject.getActivity(newName) : m_currentParent;
                sendChildren();
                Log.d(TAG, "Element edited");
            } else if (action.equals(AddActivity.ADD_ELEMENT)) {
                // Receiver information from activity
                boolean isTask = intent.getBooleanExtra(
                        "IS_TASK", false);
                String activityName = intent.getStringExtra(
                        "ACTIVITY_NAME");
                String activityDescription = intent.getStringExtra(
                        "ACTIVITY_DESCRIPTION");
                int programmedTime = intent.getIntExtra(
                        "TASK_PROGRAMMED", -1);
                int limitedTime = intent.getIntExtra(
                        "TASK_LIMITED", -1);

                CActivity act;
                if (isTask) {
                    act = new CTask(activityName, activityDescription);
                    if (programmedTime >= 0) {
                        act = new CTaskProgrammed((CTask) act,
                                CClock.getInstance().getTime()
                                        + programmedTime);
                    }
                    if (limitedTime > 0) {
                        act = new CTaskLimitedTime((CTask) act,
                                limitedTime);
                    }
                } else {  // If it's a project
                    act = new CProject(activityName, activityDescription);
                }
                if (m_currentParent instanceof CProject) {
                    ((CProject) m_currentParent)
                            .appendActivity(act);
                }
                sendChildren();
            } else {
                Log.d(TAG, "Unknown action!");
            }
            Log.d(TAG, "End of onReceive");
        }
    }

    /**
     * Receives the Intents sent by other activities. It receives all intents
     * and differentiates them by their "action". See {@link Receiver}
     * for more.
     */
    private Receiver m_receiver;

    /**
     * Not used because our class is not linked to any activity.
     * @param intent Intent
     * @return null
     */
    @Nullable
    @Override
    public IBinder onBind(final Intent intent) {return null; }

    /**
     * We send the children on first present of the activity or on
     * a renewal.
     * @param intent intent
     * @param flags flags
     * @param startId start
     * @return Service
     */
    @Override
    public final int onStartCommand(final Intent intent, final int flags,
                                    final int startId) {
        if ((flags & Service.START_FLAG_RETRY) == 0) {
            Log.d(TAG, "onStartCommand renewal");
        } else {
            Log.d(TAG, "onStartCommand for the first time");
        }
        sendChildren();
        // web android developer: "We want this service
        // to continue running until it is explicitly stopped, so return
        // sticky".
        return Service.START_STICKY;
    }

    /**
     * Forwards <code>sendChildren</code> to update the UI if this has
     * changed (a task is beign tracked).
     */
    public final void update() {
        Log.d(TAG, "In update of CTimeTrackerEngine2");
        if (m_trackedTasks.size() > 0) {
            sendChildren();
        }
    }
    /** Stores the information held in m_activities on the desired destination
     * using the ObjectSaver. */
    public void save() {
        Log.d("TAG", "desa arbre activitats");
        try {
            FileOutputStream fops = openFileOutput(m_fileName,
                    Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fops);
            out.writeObject(m_root);
            out.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "L'arxiu no es troba");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //m_objectSaver.save("Serial");
    }
    /** Loads previous session of time tracking depending on the desired
     * storage.
     * */
    public void load() {
        Log.d("TAG", "carrega arbre d'activitats");
        try {
            FileInputStream fips = openFileInput(m_fileName);
            ObjectInputStream in = new ObjectInputStream(fips);
            m_root = (CProject) in.readObject();
            in.close();
            for (String key : m_root.getActivities().keySet()) {
                CTimeTrackerEngine.getInstance()
                        .addActivity(key,
                                (CProject) m_root.getActivity(key));
            }
            Log.d(TAG, "Arbre llegit d'arxiu");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "L'arxiu no es troba, fem un arbre buit");
            m_root = new CProject("Root", "arrel de projectes");
            // e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //m_objectSaver.load("Serial");
    }

    /**
     * Builds a list of data and sends it in a broadcast message.
     */
    private void sendChildren() {
        Intent answer = new Intent(CTimeTrackerEngine2.HAS_CHILDREN);
        answer.putExtra("parent_activity_is_root",
                (m_currentParent == m_root));
        if (m_currentParent instanceof CProject) {
            ArrayList<CActivity> activityList =
                    new ArrayList<CActivity>(m_currentParent.getChildren());
            answer.putExtra("activity_list", activityList);
            answer.putExtra("ACTIVITY", m_currentParent);
        } else { // It is a task
            ArrayList<CInterval> intervalList =
                    new ArrayList<CInterval>(m_currentParent.getChildren());
            answer.putExtra("interval_list", intervalList);
            answer.putExtra("ACTIVITY", m_currentParent);
        }
        sendBroadcast(answer);
        Log.d(TAG, "HAS_CHILDREN Intent sent from activity "
                + m_currentParent.getClass().getName());
    }

    /**
     * Stops the updater handlers of the UI and the clock. Stops the
     * Intents receiver, the task tracking, saves the to a file and calls
     * <code>stopSelf</code> of this service which is similar to closing
     * the app.
     */
    private void stopService() {
        m_uiUpdater.stop();
        m_clock.stop();
        unregisterReceiver(m_receiver);
        stopTrackingTask(); // Stop tracking all tracked tasks
        save(); // Safe to file
        stopSelf();
        Log.d(TAG, "Service uninstalled.");
    }

    /**
     * Stops tracking all tasks that are being tracked.
     */
    private void stopTrackingTask() {
        for (CTask t : m_trackedTasks) {
            try {
                t.trackTaskStop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Usada a {@link onCreate} i {@link carregaArbreActivitats} per crear un o
     * altre tipus d'arbre de projectes, tasques i intervals.
     */
    private final int llegirArbreArxiu = 0;

    /**
     * Usada a {@link onCreate} i {@link carregaArbreActivitats} per crear un o
     * altre tipus d'arbre de projectes, tasques i intervals.
     */
    private final int ferArbrePetitBuit = 1;

    /**
     * Llegeix l'abre de projectes, tasques i intervals desat en l'arxiu. Aquest
     * arxiu és propi i privat de la aplicació.
     *
     * @param opcio
     *            permet escollir entre 3 tipus d'arbres :
     *            <p>
     *            <ol>
     *            <li>Llegir l'arbre d'arxiu, si existeix, que vol dir si prové
     *            de desar l'arbre existent en una crida anterior al mètode
     *            <code>GestorArbreActivitats#desaArbre</code>. Si no existeix,
     *            fa un arbre que només te el node arrel.</li>
     *            <li>Fer un arbre petit, amb 3 tasques i 2 projectes a més de
     *            l'arrel, i sense intervals.</li>
     *            <li>Fer un arbre gran amb projectes, tasques i intervals, amb
     *            dades aleatòries però consistents. Cada vegada es fa el mateix
     *            arbre, però.</li>
     *            </ol>
     *
     * @see desaArbreActivitats
     */
    public final void carregaArbreActivitats(final int opcio) {
        // TODO : Això fora millor fer-ho en un altre thread per evitar que la
        // lectura d'un arxiu molt gran pugui trigui massa i provoqui que la
        // aplicació perdi responsiveness o pitjor, que aparegui el diàleg
        // ANR = application is not responding, demanant si volem forçar el
        // tancament de la aplicació o esperar.
        // La solució deu ser fer servir una AsyncTask, tal com s'explica a
        // l'article "Painless Threading" de la documentació del Android SDK.
        // Veure'l a la versió local o a
        // developer.android.com/resources/articles/painless-threading.html
        switch (opcio) {
            case llegirArbreArxiu:
                load();
                break;
            case ferArbrePetitBuit:
                // Crea un arbre de "mostra" petit, sense intervals. Per tant, cap
                // tasca ni projecte tenen data inicial, final ni durada.
                m_root = new CProject("Root", "arrel de projectes");
                m_root.isRoot(true);
                CTimeTrackerEngine.getInstance().addActivity(
                        "Enginyeria del software 2",
                        new CProject("Enginyeria del software 2",
                                "primer projecte"));
                CTimeTrackerEngine.getInstance().addActivity(
                        "Sistemas embebidos examen",
                        new CProject("Sistemas embebidos examen",
                        "Estudiar para la recuperación"));
                CProject proj1 = (CProject) CTimeTrackerEngine.getInstance()
                        .getActivity("Enginyeria del software 2");
                CProject proj2 = (CProject) CTimeTrackerEngine.getInstance()
                        .getActivity("Sistemas embebidos examen");
                // Project 1.1
                CProject proj1_1 = new CProject(
                        "Visió artificial", "segon projecte");
                proj1_1.appendActivity(new CProject(
                        "Reinforcement Learning", "Autonomous robot"));
                proj1_1.appendActivity(new CTask(
                        "Ver tutoriales online", "Aprender lo básico"));
                // Project 1
                proj1.appendActivity(proj1_1);
                proj1.appendActivity(new CTask(
                        "Anar a buscar carnet biblio", "tercera tasca"));
                proj1.appendActivity(new CTask(
                        "Instal·lar Eclipse", "primera tasca"));
                proj1.appendActivity(new CTask(
                        "Estudiar patrons", "segona tasca"));
                // Project 2
                proj2.appendActivity(new CProject(
                        "Limpiar la casa", "Dejar todo ordenado antes"
                        + "de que llegue mi hermana"));
                m_root.appendActivity(proj1);
                m_root.appendActivity(proj2);
                Log.d(TAG, "Arbre de mostra petit i sense intervals creat");
                break;
            default:
                // no hi ha més opcions possibles
                assert false : "opció de creació de l'arbre no existent";
        }
    }

    /**
     * The service consists of processing an activity, update it and send the
     * list of children activities or intervals. For this reason we design
     * this intent.
     */
    public static final String HAS_CHILDREN = "Has_children";
    /**
     * Object used for persistence (i.e. Serializing and DB handling)
     */
    private CObjectSaver m_objectSaver = new CObjectSaver();
    /**
     * Name of the class to identify when logging or message passing.
     * @see Log
     */
    private final String TAG = this.getClass().getSimpleName();

    /**
     * Observer that updates the tasks that are being tracked by invoking
     * the update() method.
     */
    private CClockUpdatable m_clock;
    /**
     * Root project used to keep track of high level projects.
     */
    private CProject m_root;
    /**
     * Helps navigate through the tree. Its initial
     * value is the root project, {@link #m_root}
     */
    private CActivity m_currentParent;

    /**
     * Handler that allows us to update the interface when a task is
     * being tracked. It is started and stopped in
     * {@link Receiver#onReceive}.
     */
    private CUpdater m_uiUpdater;
    /**
     * It's a parameter for the constructor of {@link #m_uiUpdater}.
     * Used to refresh the UI in milliseconds.
     */
    private final int m_refreshPeriod = 1000;
    private String m_fileName = "TimeTracker.ser";
    /**
     * List of tasks being tracked.
     */
    private ArrayList<CTask> m_trackedTasks = new ArrayList<CTask>();
}
