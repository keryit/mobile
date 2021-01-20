package femi.core.Data;

public class SOAPData {
    private String patientFirstName;
    private String patientLastName;
    private String patientPolicyId;
    private String patientEmail;
    private String patientDepartment;
    private String patientOrgId;
    private String referenceId;
    private String startTime;
    private String endTime;
    private String customerOrgId;
    private String customerDepartment;
    private String customerLicenseId;
    private String customerEmail;
    private String providerOrgId;
    private String providerDepartment;
    private String providerLicenseId;
    private String providerEmail;
    private String providerFirstName;
    private String providerLastName;

    public String getProviderFirstName() {
        return providerFirstName;
    }

    public void setProviderFirstName(String providerFirstName) {
        this.providerFirstName = providerFirstName;
    }

    public String getProviderLastName() {
        return providerLastName;
    }

    public void setProviderLastName(String providerLastName) {
        this.providerLastName = providerLastName;
    }

    public String getPatientDepartment() {
        return patientDepartment;
    }

    public void setPatientDepartment(String patientDepartment) {
        this.patientDepartment = patientDepartment;
    }

    public String getPatientOrgId() {
        return patientOrgId;
    }

    public void setPatientOrgId(String patientOrgId) {
        this.patientOrgId = patientOrgId;
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

    public String getPatientPolicyId() {
        return patientPolicyId;
    }

    public void setPatientPolicyId(String patientPolicyId) {
        this.patientPolicyId = patientPolicyId;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCustomerOrgId() {
        return customerOrgId;
    }

    public void setCustomerOrgId(String customerOrgId) {
        this.customerOrgId = customerOrgId;
    }

    public String getCustomerDepartment() {
        return customerDepartment;
    }

    public void setCustomerDepartment(String customerDepartment) {
        this.customerDepartment = customerDepartment;
    }

    public String getCustomerLicenseId() {
        return customerLicenseId;
    }

    public void setCustomerLicenseId(String customerLicenseId) {
        this.customerLicenseId = customerLicenseId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getProviderOrgId() {
        return providerOrgId;
    }

    public void setProviderOrgId(String providerOrgId) {
        this.providerOrgId = providerOrgId;
    }

    public String getProviderDepartment() {
        return providerDepartment;
    }

    public void setProviderDepartment(String providerDepartment) {
        this.providerDepartment = providerDepartment;
    }

    public String getProviderLicenseId() {
        return providerLicenseId;
    }

    public void setProviderLicenseId(String providerLicenseId) {
        this.providerLicenseId = providerLicenseId;
    }

    public String getProviderEmail() {
        return providerEmail;
    }

    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }

    @Override
    public String toString() {
        return "SOAPData{" +
                "patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", patientPolicyId='" + patientPolicyId + '\'' +
                ", patientEmail='" + patientEmail + '\'' +
                ", patientDepartment='" + patientDepartment + '\'' +
                ", patientOrgId='" + patientOrgId + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", customerOrgId='" + customerOrgId + '\'' +
                ", customerDepartment='" + customerDepartment + '\'' +
                ", customerLicenseId='" + customerLicenseId + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", providerOrgId='" + providerOrgId + '\'' +
                ", providerDepartment='" + providerDepartment + '\'' +
                ", providerLicenseId='" + providerLicenseId + '\'' +
                ", providerEmail='" + providerEmail + '\'' +
                ", providerFirstName='" + providerFirstName + '\'' +
                ", providerLastName='" + providerLastName + '\'' +
                '}';
    }
}
