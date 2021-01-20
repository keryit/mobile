package femi.core.Data;

public class AppointmentData {
    private String date;
    private String timeSlot;
    private String patientPolicyId;
    private String clinicianCustomerFirstName;
    private String clinicianCustomerLastName;
    private String clinicianProviderFirstName;
    private String clinicianProviderLastName;
    private String patientFirstName;
    private String patientLastName;
    private String status;
    private String patientAdditionalInfo;
    private String providerOrganizationName;
    private String providerDepartmentName;
    private String customerOrganizationName;

    public String getCustomerOrganizationName() {
        return customerOrganizationName;
    }

    public void setCustomerOrganizationName(String customerOrganizationName) {
        this.customerOrganizationName = customerOrganizationName;
    }

    public String getProviderOrganizationName() {
        return providerOrganizationName;
    }

    public void setProviderOrganizationName(String providerOrganizationName) {
        this.providerOrganizationName = providerOrganizationName;
    }

    public String getProviderDepartmentName() {
        return providerDepartmentName;
    }

    public void setProviderDepartmentName(String providerDepartmentName) {
        this.providerDepartmentName = providerDepartmentName;
    }

    public String getPatientAdditionalInfo() {
        return patientAdditionalInfo;
    }

    public void setPatientAdditionalInfo(String patientAdditionalInfo) {
        this.patientAdditionalInfo = patientAdditionalInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClinicianProviderFirstName() {
        return clinicianProviderFirstName;
    }

    public void setClinicianProviderFirstName(String clinicianProviderFirstName) {
        this.clinicianProviderFirstName = clinicianProviderFirstName;
    }

    public String getClinicianProviderLastName() {
        return clinicianProviderLastName;
    }

    public void setClinicianProviderLastName(String clinicianProviderLastName) {
        this.clinicianProviderLastName = clinicianProviderLastName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPatientPolicyId() {
        return patientPolicyId;
    }

    public void setPatientPolicyId(String patientPolicyId) {
        this.patientPolicyId = patientPolicyId;
    }

    public String getClinicianCustomerFirstName() {
        return clinicianCustomerFirstName;
    }

    public void setClinicianCustomerFirstName(String clinicianCustomerFirstName) {
        this.clinicianCustomerFirstName = clinicianCustomerFirstName;
    }

    public String getClinicianCustomerLastName() {
        return clinicianCustomerLastName;
    }

    public void setClinicianCustomerLastName(String clinicianCustomerLastName) {
        this.clinicianCustomerLastName = clinicianCustomerLastName;
    }

    public String getPatientFirstName() {
        return patientFirstName;
    }

    public void setPatientFirstName(String patientFirstName) {
        this.patientFirstName = patientFirstName;
    }

    public String getPatientLastName() {
        return patientLastName;
    }

    public void setPatientLastName(String patientLastName) {
        this.patientLastName = patientLastName;
    }

    public String getFullPatientName(){
        return this.patientFirstName + " " + this.patientLastName;
    }

    public String getFullClinicianCustomerName(){
        return this.clinicianCustomerFirstName + " " + this.clinicianCustomerLastName;
    }

    public String getFullClinicianProviderName(){
        return this.clinicianProviderFirstName + " " + this.clinicianProviderLastName;
    }

    @Override
    public String toString() {
        String resStr = "AppointmentData{";
        if(date != null) resStr += "date='" + date + '\'';
        if(timeSlot != null) resStr += ", timeSlot='" + timeSlot + '\'';
        if(status != null) resStr += ",status='" + status + '\'';
        if(patientPolicyId != null) resStr += ", patientPolicyId='" + patientPolicyId + '\'';
        if(patientFirstName != null) resStr += ", patientFirstName='" + patientFirstName + '\'';
        if(patientLastName != null) resStr += ", patientLastName='" + patientLastName + '\'';
        if(clinicianCustomerFirstName != null) resStr += ", clinicianCustomerFirstName='" + clinicianCustomerFirstName + '\'';
        if(clinicianCustomerLastName != null) resStr += ", clinicianCustomerLastName='" + clinicianCustomerLastName + '\'';
        if(clinicianProviderFirstName != null) resStr += ", clinicianProviderFirstName='" + clinicianProviderFirstName + '\'';
        if(clinicianProviderLastName != null) resStr += ", clinicianProviderLastName='" + clinicianProviderLastName + '\'';
        if(providerOrganizationName != null) resStr += ", providerOrganizationName='" + providerOrganizationName + '\'';
        if(providerDepartmentName != null) resStr += ", providerDepartmentName='" + providerDepartmentName + '\'';
        if(customerOrganizationName != null) resStr += ", customerOrganizationName='" + customerOrganizationName + '\'';
        resStr += "}";

        return resStr;
    }
}
