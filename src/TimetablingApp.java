import Controller.AppController;

/**
 * Main entry point for the UL Timetabling System.
 */
public class TimetablingApp {

    public static void main(String[] args) {
        System.out.println("--- UL Timetabling System ---");
        // Initialize the controller and start the application loop
        AppController controller = new AppController();
        controller.run();
    }
}