
public class HealthIncident  {


    private int time;
    private String disease;
    private double latitude;
    private double longitude;
    private String location;
    private double infection_rate;
    private int population_affected;
    private double severity;
    private String reporting_agency;

    private Position<HealthIncident> severityPosition;

   
    public HealthIncident(int time, String disease, double latitude, double longitude,
                          String location, double infection_rate, int population_affected,
                          double severity, String reporting_agency) {
        this.time = time;
        this.disease = disease;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.infection_rate = infection_rate;
        this.population_affected = population_affected;
        this.severity = severity;
        this.reporting_agency = reporting_agency;
        
     
        this.severityPosition = null;
    }

  

    public int getTime() {
        return time; 
    }

    public String getDisease() {
        return disease; 
    }

    public double getLatitude() {
        return latitude; 
    }

    public double getLongitude() {
        return longitude; 
    }

    public String getLocation() {
        return location; 
    }

    public double getSeverity() {
        return severity; 
    }

   
    public double getInfectionRate() {
        return infection_rate;
    }

    public int getPopulationAffected() {
        return population_affected;
    }

    public String getReportingAgency() {
        return reporting_agency;
    }

  
    public void setSeverityPosition(Position<HealthIncident> pos) {
        this.severityPosition = pos;
    }

  
    public Position<HealthIncident> getSeverityPosition() {
        return severityPosition;
    }
   
    
}