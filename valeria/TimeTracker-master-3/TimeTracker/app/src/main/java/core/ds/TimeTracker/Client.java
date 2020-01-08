/**
 * Client: Main class used to test the hardcoded tasks for the first iteration of Software Design
 * lab project. The main function presents an interactive menu where the user can test the milestone 1
 * and milestone 2, as well as saving and loading at a later time.*/
package core.ds.TimeTracker;

import android.util.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader; // To store user input
import java.io.IOException; // To handle input/output exceptions
import java.io.InputStreamReader; // Stream from user to program
import java.lang.*; // TODO: I don't know.
import java.util.logging.Level;

import static java.lang.Thread.sleep;

/**
 * Main class. In  this class we made our menu and test.
 *
 * @author
 * @version
 */

public class Client {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String [] args) {
        // Variables for interactive menu
        InputStreamReader isr = new InputStreamReader(System.in); // Creates input stream
        BufferedReader stdin = new BufferedReader(isr); // Interface to the user (Standard input)
        String userInput = null; // Variable to store the user input
        boolean quit = false; // Flag to quit the application

        long timeUnit = 2000; // Time in milliseconds of the rate at which the Clock will count the seconds

        CTimeTrackerEngine TimeTracker = CTimeTrackerEngine.getInstance(); // First and only instance of CTimeTrackerEngine
        TimeTracker.setTimeUnit(timeUnit); // Set the above mentioned timeUnit of the Clock
        CClock Clock = CClock.getInstance(); // First and only instance of CClock
        Clock.start(); // Start the Clock in its own Thread
        Clock.addPropertyChangeListener(TimeTracker); // Add TimeTracker as Listener to the steps of the Clock

        // === Start of the question for the second milestone ===
        /* Per tal de facilitar l'avaluacio de la generacio dels informes de la Fita 2, heu d'implementar la
        seguent prova. S'ha de crear una estructura de projectes i tasques tal que: */
        // P1 i P2 son projectes arrel
        TimeTracker.addActivity("P1", new CProject("P1", "Project 1") ); // Create P1
        TimeTracker.addActivity("P2", new CProject("P2", "Project 2") ); // Create P1
        // P1.2 es subprojecte de P1
        CProject P1 = (CProject) TimeTracker.getActivity("P1"); // Get a reference to P1
        P1.appendActivity( new CProject("P1.2", "Project P1.2") ); // Create T3 in P1
        // El projecte P1 conte les tasques T1 i T2.
        P1.appendActivity( new CTask("T1", "Task 1")); // Task T1
        P1.appendActivity( new CTask("T2", "Task 2")); // Task T2
        // El projecte P1.2 conte la tasca T4. El projecte
        CProject P1_2 = (CProject) P1.getActivity("P1.2");
        P1_2.appendActivity( new CTask("T4", "Task 4")); // Task T4
        // P2 conte la tasca T3.
        CProject P2 = (CProject) TimeTracker.getActivity("P2"); // Get a reference to P2
        P2.appendActivity( new CTask("T3", "Task 3")); // Task T3
        /* Una vegada s'ha iniciat l'aplicacio i creat l'estructura de projectes i tasques anterior, comptant
        el temps cada dos segons, heu de provar (veure figura 5): */

        // 2. Indicar que el temps es comptara cada dos segons i per tant que la sortida de la aplicacio
        // s'actualitzara o mostrara tambe amb aquesta periodicitat.
        System.out.println("The project tree will be displayed every "
                + timeUnit/1000
                + " seconds.");

        // Main do-while loop for the interactive menu
        do {
            System.out.println(" 0 - Test 1.1 \n 1 - Test 1.2 \n 2 - Test fita 2 \n "
                    + "T - Limited time task \n "
                    + "P - Preprogrammed time task \n R - Limited preprogrammed time task \n "
                    + "C - Continue \n S - Save \n Q - Exit"); // Menu output
            try {
                userInput = stdin.readLine();  // Try to read user input
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (userInput) {
                case "0":
                    fita1(P1, P2);
                    break;
                case "1":
                    fita2(P1, P2);
                    break;
                case "2":
                    // 1. Comencar a cronometrar les tasques T1, T4.
                    P1.trackTaskStart("T1");
                    P1_2.trackTaskStart("T4");

                    try {
                        logger.info("Waiting 4 seconds...");
                        sleep(4000);
                    } catch (InterruptedException e) { }

                    // 2. Passats 4 segons, parar el cronometrat de la tasca T1. Comencar a cronometrar la tasca T2.
                    P1.trackTaskStop("T1");
                    P1.trackTaskStart("T2");
                    long start = Clock.getTime(); // Begin of the report

                    try {
                        logger.info("Waiting 6...");
                        sleep(6000);
                    } catch (InterruptedException e) { }

                    // 3. Passats 6 segons, parar el cronometrat de les tasques T2, T4. Comencar a cronometrar la tasca T3.
                    P1.trackTaskStop("T2");
                    P1_2.trackTaskStop("T4");
                    P2.trackTaskStart("T3");

                    try {
                        logger.info("Waiting 4 seconds...");
                        sleep(4000);
                    } catch (InterruptedException e) { }

                    // 4. Passats 4 segons, parar el cronometrat de la tasca T3. Comencar a cronometrar la tasca T2.
                    P2.trackTaskStop("T3");
                    P1.trackTaskStart("T2");
                    long end = Clock.getTime(); // End of the report

                    try {
                        logger.info("Waiting 2 seconds...");
                        sleep(2000);
                    } catch (InterruptedException e) { }

                    // 5. Passats 2 segons, comencar a cronometrar la tasca T3.
                    P2.trackTaskStart("T3");

                    try {
                        logger.info("Waiting 4 seconds...");
                        sleep(4000);
                    } catch (InterruptedException e) { }

                    // 6. Passats 4 segons, parar el cronometrat de les tasques T2, T3.
                    P1.trackTaskStop("T2");
                    P2.trackTaskStop("T3");

                    // 7. Generar un informe des del segon 4 ns al segon 14.
                    CVisitorFormatter formatter = new CVisitorFormatterText(start, end);
                    CVisitorReporter reporter = new CVisitorReporterDetailed(formatter);
                    TimeTracker.generateReport(reporter);
                    break;
                case "A":
                case "a":
                    // 1. Comencar a cronometrar les tasques T1, T4.
                    P1.trackTaskStart("T1");
                    
                    P1.trackTaskStart("T1");
                    P1.trackTaskStop("T1");
                    break;
                case "T":
                case "t":
                    // Create a task that will run for 10 seconds until closing
                    CTask limitedTask = new CTaskLimitedTime(new CTask("T4", "Limited time task"), 10000 );
                    P2.appendActivity(limitedTask);
                    P2.trackTaskStart("T4");
                    try {
                        logger.info("Waiting 10 seconds to test CTaskLimitedTime");
                        sleep(10000);
                    } catch (InterruptedException e) { }
                    break;
                case "P":
                case "p":
                    // Create a task that will run in 10 seconds from now
                    CTask programmedTask = new CTaskProgrammed(new CTask("T5", "Preprogrammed task"), Clock.getTime()+10000);
                    P2.appendActivity(programmedTask);
                    try {
                        logger.info("Waiting 10 seconds to test CTaskProgrammed");
                        sleep(10000);
                    } catch (InterruptedException e) { }

                    break;
                case "R":
                case "r":
                    // Create a task that will run in 10 seconds from now
                    CTask programmed = new CTaskProgrammed(new CTask("T5", "Preprogrammed task"), Clock.getTime()+5000);
                    CTask limited = new CTaskLimitedTime(programmed, 10000);
                    P2.appendActivity(limited);
                    try {
                        logger.info("Waiting 5 seconds to run Programed and time limited task for 10 seconds");
                        sleep(10000);
                    } catch (InterruptedException e) { }
                    break;
                case "C":
                case "c":
                    TimeTracker.load();
                    P1 = (CProject) TimeTracker.getActivity("P1");
                    P2 = (CProject) P1.getActivity("P2");
                    break;
                case "S":
                case "s":
                    TimeTracker.save();
                    break;
                case "Q":
                case "q":
                    quit = true;
                    break;
                default:
                    logger.error("Invalid option or something went wrong!");
                    break;
            }

        } while (!quit); // End of while-loop

        Clock.removePropertyChangeListener(TimeTracker);
        Clock.turnOff();
    }

    public static void fita1(CProject P1, CProject P2) {
        // 3. Comencar a cronometrar la tasca T3. A partir d'aquest moment s'ha de s'ha de mostrar
        //com es va comptant el temps imprimint cada 2 segons com a les taules 1 a 3. No cal que
        //us hi feu en el format de la taula, nomes que les columnes estiguin ben indendates per
        //poder llegir-ne el contingut. No cal sobreescriure la taula si no voleu, simplement anar-la
        //reimprimint.
        System.out.println("3. Comencar a cronometrar la tasca T3.");
        P1.trackTaskStart("T3");

        // 4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de veure ara la taula 1.
        try {
            System.out.println("4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de veure ara la taula 1.");
            sleep(3000);
        } catch (InterruptedException e) {

        }
        P1.trackTaskStop("T3");

        // 5. Esperar 7 segons mes.
        try {
            System.out.println("5. Esperar 7 segons mes.");
            sleep(7000);
        } catch (InterruptedException e) {

        }

        // 6. Engegar el cronometre per la tasca T2.
        P2.trackTaskStart("T2");

        // 7. Esperar 10 segons i llavors parar el cronometratge de T2.
        try {
            System.out.println("7. Esperar 10 segons i llavors parar el cronometratge de T2.");
            sleep(10000);
        } catch (InterruptedException e) {

        }
        P2.trackTaskStop("T2");

        // 8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.
        P1.trackTaskStart("T3");
        try {
            System.out.println("8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.");
            sleep(2000);
        } catch (InterruptedException e) {

        }
        P1.trackTaskStop("T3");
    }

    public static void fita2(CProject P1, CProject P2) {
        // 3. Comencar a cronometrar la tasca T3.
        System.out.println("3. Comencar a cronometrar la tasca T3.");
        P1.trackTaskStart("T3");

        // 4. Passats 4 segons, comencar a cronometrar la tasca T2.
        try {
            System.out.println("4. Passats 4 segons, comencar a cronometrar la tasca T2.");
            sleep(4000);
        } catch (InterruptedException e) {

        }
        P2.trackTaskStart("T2");

        // 5. Passats 2 segons, parar el cronometrat de la tasca T3.
        try {
            System.out.println("5. Passats 2 segons, parar el cronometrat de la tasca T3.");
            sleep(2000);
        } catch (InterruptedException e) {

        }
        P1.trackTaskStop("T3");

        // 6. Passats 2 segons, comencar a
        // cronometrar la tasca T1.
        try {
            System.out.println("6. Passats 2 segons, comencar a cronometrar la tasca T1.");
            sleep(2000);
        } catch (InterruptedException e) {

        }
        P2.trackTaskStart("T1");

        // 7. Passats 4 segons, parar el
        // cronometrat de la tasca T1.
        try {
            System.out.println("7. Passats 4 segons, parar el cronometrat de la tasca T1. ");
            sleep(4000);
        } catch (InterruptedException e) {

        }
        P2.trackTaskStop("T1");

        // 8. Passats 2 segons, parar el
        // cronometrat de la tasca T2.
        try {
            System.out.println("8. Passats 2 segons, parar el cronometrat de la tasca T2.");
            sleep(2000);
        } catch (InterruptedException e) {

        }
        P2.trackTaskStop("T2");

        // 9. Esperar sense fer res 4 segons i
        // despres cronometrar T3 durant 2 segons.
        try {
            System.out.println("9. Esperar sense fer res 4 segons i despres cronometrar T3 durant 2 segons.");
            sleep(4000);
        } catch (InterruptedException e) {

        }

        P1.trackTaskStart("T3");
        try {
            System.out.println("2 segons...");
            sleep(4000);
        } catch (InterruptedException e) {

        }
        P1.trackTaskStop("T3");
    }
}

