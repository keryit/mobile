package femi.core.Data;

public enum AppointmentStatus {
    OVERDUE("Overdue"),
    OVERDUE_IN_HE("באיחור"),
    CREATED("Created"),
    MISSING_INFO("Missing Info"),
    IN_PROGRESS("In Progress"),
    UNANSWERED("Unanswered"),
    DISCONNECTED("Disconnected"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    MISSING_SUMMARY("Missing Summary");

    private String status;

    AppointmentStatus(String status){
        this.status = status;
    }

    public String getName(){
        return status;
    }
}
