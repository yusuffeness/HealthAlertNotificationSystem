import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Locale;

public class HealthAlertNotification {

    private static LinkedPositionalList<Watcher> watcherList;
    private static LinkedQueue<HealthIncident> incidentQueue;
    private static LinkedPositionalList<HealthIncident> severityOrderedList;

    private static boolean allFlag = false;
    private static String watcherFileName;
    private static String healthFileName;
    
    public static void main(String[] args) {
        try {
            parseArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Kullanım: java HealthAlertNotification [--all] <watcherFile> <healthFile>");
            return;
        }

        watcherList = new LinkedPositionalList<>();
        incidentQueue = new LinkedQueue<>();
        severityOrderedList = new LinkedPositionalList<>();

        try {
            runSimulation();
        } catch (FileNotFoundException e) {
            System.err.println("HATA: Dosya bulunamadı: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Simülasyon sırasında beklenmedik bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void parseArgs(String[] args) throws IllegalArgumentException {
        if (args.length == 2) {
            watcherFileName = args[0];
            healthFileName = args[1];
        } else if (args.length == 3 && args[0].equals("--all")) {
            allFlag = true;
            watcherFileName = args[1];
            healthFileName = args[2];
        } else {
            throw new IllegalArgumentException("Geçersiz argüman sayısı.");
        }
    }

    private static void runSimulation() throws FileNotFoundException {
        
        try (Scanner watcherScanner = new Scanner(new File(watcherFileName)).useLocale(Locale.US);
             Scanner healthScanner = new Scanner(new File(healthFileName)).useLocale(Locale.US)) {

            int currentTime = 0;

            String nextWatcherLine = watcherScanner.hasNextLine() ? watcherScanner.nextLine() : null;
            String nextHealthLine = healthScanner.hasNextLine() ? healthScanner.nextLine() : null;

            int nextWatcherTime = (nextWatcherLine != null) ? Integer.parseInt(nextWatcherLine.split(" ")[0]) : Integer.MAX_VALUE;
            int nextHealthTime = (nextHealthLine != null) ? Integer.parseInt(nextHealthLine.split(" ")[0]) : Integer.MAX_VALUE;

            while (nextWatcherLine != null || nextHealthLine != null) {

                int nextEventTime = Math.min(nextWatcherTime, nextHealthTime);

                while (currentTime < nextEventTime) {
                    removeOldIncidents(currentTime);
                    currentTime++;
                }
                
                if (nextWatcherTime == currentTime) {
                    processWatcherEvent(nextWatcherLine);
                    nextWatcherLine = watcherScanner.hasNextLine() ? watcherScanner.nextLine() : null;
                    nextWatcherTime = (nextWatcherLine != null) ? Integer.parseInt(nextWatcherLine.split(" ")[0]) : Integer.MAX_VALUE;
                }

                if (nextHealthTime == currentTime) {
                    processHealthIncident(nextHealthLine);
                    nextHealthLine = healthScanner.hasNextLine() ? healthScanner.nextLine() : null;
                    nextHealthTime = (nextHealthLine != null) ? Integer.parseInt(nextHealthLine.split(" ")[0]) : Integer.MAX_VALUE;
                }
            }
        }
    }

    private static void processWatcherEvent(String line) {
        String[] tokens = line.split(" ");
        String command = tokens[1];
        
        switch (command) {
            case "add":
                double latAdd = Double.parseDouble(tokens[2]);
                double lonAdd = Double.parseDouble(tokens[3]);
                String nameAdd = tokens[4];
                handleAdd(nameAdd, latAdd, lonAdd);
                break;
            case "delete":
                String nameDel = tokens[2];
                handleDelete(nameDel);
                break;
            case "query-highest":
                handleQueryHighest();
                break;
            case "query-disease":
                String disease = tokens[2];
                handleQueryDisease(disease);
                break;
            case "query-region":
                double latReg = Double.parseDouble(tokens[2]);
                double lonReg = Double.parseDouble(tokens[3]);
                double radius = Double.parseDouble(tokens[4]);
                handleQueryRegion(latReg, lonReg, radius);
                break;
        }
    }

    private static void processHealthIncident(String line) {
        String[] tokens = line.split(" ");
        
        int time = Integer.parseInt(tokens[0]);
        String disease = tokens[1];
        double latitude = Double.parseDouble(tokens[2]);
        double longitude = Double.parseDouble(tokens[3]);
        String location = tokens[4];
        double infection_rate = Double.parseDouble(tokens[5]);
        int population_affected = Integer.parseInt(tokens[6]);
        double severity = Double.parseDouble(tokens[7]);
        String reporting_agency = tokens[8];

        HealthIncident newIncident = new HealthIncident(time, disease, latitude, longitude,
                location, infection_rate, population_affected, severity, reporting_agency);

        insertHealthIncident(newIncident);
    }

    private static void insertHealthIncident(HealthIncident incident) {
        
        incidentQueue.enqueue(incident);
        
        Position<HealthIncident> newPos;
        Position<HealthIncident> insertionPoint = null;

        for (Position<HealthIncident> pos : severityOrderedList.positions()) {
            if (pos.getElement().getSeverity() < incident.getSeverity()) {
                insertionPoint = pos;
                break;
            }
        }

        if (insertionPoint == null) {
            newPos = severityOrderedList.addLast(incident);
        } else {
            newPos = severityOrderedList.addBefore(insertionPoint, incident);
        }

        incident.setSeverityPosition(newPos);
        
        for (Position<Watcher> pos : watcherList.positions()) {
            Watcher w = pos.getElement();
            
            double dist = calculateDistance(w.getLatitude(), w.getLongitude(), 
                                           incident.getLatitude(), incident.getLongitude());
            
            if (dist < 2 * incident.getSeverity()) {
                System.out.println("(Disease: " + incident.getDisease() + ") at " + 
                                   incident.getLocation() + " is close to " + w.getName());
            }
        }
        
        if (allFlag) {
            System.out.println("(Disease: " + incident.getDisease() + ") at " + 
                               incident.getLocation() + " is inserted into incident-queue");
        }
    }

    private static void removeOldIncidents(int currentTime) {
        int cutoffTime = currentTime - 6; 

        while (!incidentQueue.isEmpty() && incidentQueue.first().getTime() <= cutoffTime) {
            HealthIncident oldIncident = incidentQueue.dequeue();
            Position<HealthIncident> pos = oldIncident.getSeverityPosition();
            if (pos != null) {
                severityOrderedList.remove(pos);
            }
        }
    }

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }

    private static void handleAdd(String name, double latitude, double longitude) {
        Watcher newWatcher = new Watcher(name, latitude, longitude);
        watcherList.addLast(newWatcher);
        System.out.println(name + " is added to the watcher-list");
    }

    private static void handleDelete(String name) {
        Position<Watcher> watcherToRemove = null;
        
        for (Position<Watcher> pos : watcherList.positions()) {
            if (pos.getElement().getName().equals(name)) {
                watcherToRemove = pos;
                break;
            }
        }
        
        if (watcherToRemove != null) {
            watcherList.remove(watcherToRemove);
            System.out.println(name + " is removed from the watcher-list");
        }
    }

    private static void handleQueryHighest() {
        if (severityOrderedList.isEmpty()) {
            System.out.println("No records");
        } else {
            HealthIncident h = severityOrderedList.first().getElement();
            System.out.println("Most severe health incident in past 6 hours:");
            System.out.println("(Disease: " + h.getDisease() + ") Severity: " + 
                               h.getSeverity() + " at " + h.getLocation());
        }
    }

    private static void handleQueryDisease(String disease) {
        
        LinkedQueue<HealthIncident> tempQueue = new LinkedQueue<>();
        
        while (!incidentQueue.isEmpty()) {
            HealthIncident h = incidentQueue.dequeue();
            
            if (h.getDisease().equals(disease)) {
                System.out.println("(Disease: " + h.getDisease() + ") Severity: " + 
                                   h.getSeverity() + " at " + h.getLocation());
            }
            
            tempQueue.enqueue(h);
        }
        
        while (!tempQueue.isEmpty()) {
            incidentQueue.enqueue(tempQueue.dequeue());
        }
    }

    private static void handleQueryRegion(double latitude, double longitude, double radius) {
        
        LinkedQueue<HealthIncident> tempQueue = new LinkedQueue<>();

        while (!incidentQueue.isEmpty()) {
            HealthIncident h = incidentQueue.dequeue();
            
            double dist = calculateDistance(latitude, longitude, h.getLatitude(), h.getLongitude());
            if (dist <= radius) {
                System.out.println("(Disease: " + h.getDisease() + ") Severity: " + 
                                   h.getSeverity() + " at " + h.getLocation());
            }

            tempQueue.enqueue(h);
        }
        
        while (!tempQueue.isEmpty()) {
            incidentQueue.enqueue(tempQueue.dequeue());
        }
    }
}
