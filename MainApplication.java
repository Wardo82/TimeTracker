package core.DS.timetracker;

import java.lang.*;

import static java.lang.Thread.sleep;

public class MainApplication {

    public static void main(String [] args) {
        long timeUnit = 2000; // Time in milliseconds
        String fita = "fita 2";

        CTimeTrackerEngine TimeTracker = CTimeTrackerEngine.getInstance();
        TimeTracker.setTimeUnit(timeUnit);
        CClock Clock = CClock.getInstance(); //
        Clock.start();
        Clock.addPropertyChangeListener(TimeTracker);

        //  1. Iniciar la aplicacio i crear l'estructura de projectes i tasques anterior.
        TimeTracker.addActivity("P1", new CProject("P1", "Project 1") );
        CProject P1 = (CProject) TimeTracker.getActivity("P1");
        P1.appendObject( new CTask("T3", "Task 3") );
        P1.appendObject( new CProject("P2", "Project 2") );
        CProject P2 = (CProject) P1.getObject("P2");
        P2.appendObject( new CTask("T1", "Task 1"));
        P2.appendObject( new CTask("T2", "Task 2"));

        // 2. Indicar que el temps es comptara cada dos segons i per tant que la sortida de la aplicacio
        // s'actualitzara o mostrara tambe amb aquesta periodicitat.
        System.out.println("The project tree will be displayed every "+timeUnit/1000+" seconds.");

        if (fita.equals("fita 1")){
            // 3. Comencar a cronometrar la tasca T3. A partir d'aquest moment s'ha de s'ha de mostrar
            //com es va comptant el temps imprimint cada 2 segons com a les taules 1 a 3. No cal que
            //us hi feu en el format de la taula, nomes que les columnes estiguin ben indendates per
            //poder llegir-ne el contingut. No cal sobreescriure la taula si no voleu, simplement anar-la
            //reimprimint.
            P1.trackTaskStart("T3");

            // 4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de veure ara la taula 1.
            try{
                System.out.println("4. Esperar 3 segons i parar el cronometre de la tasca T3. Haurem de veure ara la taula 1.");
                sleep(3000);
            }catch (InterruptedException e) {

            }
            P1.trackTaskStop("T3");

            // 5. Esperar 7 segons mes.
            try{
                System.out.println("5. Esperar 7 segons mes.");
                sleep(7000);
            }catch (InterruptedException e) {

            }

            // 6. Engegar el cronometre per la tasca T2.
            P2.trackTaskStart("T2");

            // 7. Esperar 10 segons i llavors parar el cronometratge de T2.
            try{
                System.out.println("7. Esperar 10 segons i llavors parar el cronometratge de T2.");
                sleep(10000);
            }catch (InterruptedException e) {

            }
            P2.trackTaskStop("T2");

            // 8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.
            P1.trackTaskStart("T3");
            try {
                System.out.println("8. Cronometrar altre cop T3 durant 2 segons. Haurem de veure ara la taula 2.");
                sleep(2000);
            }catch (InterruptedException e){

            }
            P1.trackTaskStop("T3");

        }else{

            // 3. Comencar a cronometrar la tasca T3.
            System.out.println("3. Comencar a cronometrar la tasca T3.");
            P1.trackTaskStart("T3");

            // 4. Passats 4 segons, comencar a cronometrar la tasca T2.
            try {
                System.out.println("4. Passats 4 segons, comencar a cronometrar la tasca T2.");
                sleep(4000);
            }catch (InterruptedException e){

            }
            P2.trackTaskStart("T2");

            // 5. Passats 2 segons, parar el cronometrat de la tasca T3.
            try {
                System.out.println("5. Passats 2 segons, parar el cronometrat de la tasca T3.");
                sleep(2000);
            }catch (InterruptedException e){

            }
            P1.trackTaskStop("T3");

            // 6. Passats 2 segons, comencar a cronometrar la tasca T1.
            try {
                System.out.println("6. Passats 2 segons, comencar a cronometrar la tasca T1.");
                sleep(2000);
            }catch (InterruptedException e){

            }
            P2.trackTaskStart("T1");

            // 7. Passats 4 segons, parar el cronometrat de la tasca T1.
            try {
                System.out.println("7. Passats 4 segons, parar el cronometrat de la tasca T1. ");
                sleep(4000);
            }catch (InterruptedException e){

            }
            P2.trackTaskStop("T1");

            // 8. Passats 2 segons, parar el cronometrat de la tasca T2.
            try {
                System.out.println("8. Passats 2 segons, parar el cronometrat de la tasca T2.");
                sleep(2000);
            }catch (InterruptedException e){

            }
            P2.trackTaskStop("T2");

            // 9. Esperar sense fer res 4 segons i despres cronometrar T3 durant 2 segons.
            try {
                System.out.println("9. Esperar sense fer res 4 segons i despres cronometrar T3 durant 2 segons.");
                sleep(4000);
            }catch (InterruptedException e){

            }

            P1.trackTaskStart("T3");
            try {
                System.out.println("2 segons...");
                sleep(4000);
            }catch (InterruptedException e){

            }
            P1.trackTaskStop("T3");
        }

        Clock.removePropertyChangeListener(TimeTracker);
        Clock.turnOff();
    }
}

