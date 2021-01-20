package femi.core.Data;

import femi.core.utils.FileUtils;

public class SOAPOperations {

    public String getPreparedCreateC2PAppointmentSOAPRequest(SOAPData c2pSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateC2PSOAPRequest.xml");
        c2cSoapReq = getProperlyPreparedC2PPatientDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PProviderDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PConsultationDataRequest(c2cSoapReq, c2pSOAPData);

        return c2cSoapReq;
    }

    public String getPreparedCreateC2CAppointmentSOAPRequest(SOAPData c2CSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateC2CSOAPRequest.xml");
        c2cSoapReq = getProperlyPreparedC2CPatientDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedC2CProviderDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedCustomerDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedC2CConsultationDataRequest(c2cSoapReq, c2CSOAPData);

        return c2cSoapReq;
    }

    public String getCreateToDepartmentC2PAppointmentSOAPRequest(SOAPData c2pSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateToDepartmentC2PSoapRequest.xml");
        c2cSoapReq = getProperlyPreparedC2PPatientDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PDepartmentDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PConsultationDataRequest(c2cSoapReq, c2pSOAPData);

        return c2cSoapReq;
    }

    public String getCreateToDepartmentC2CAppointmentSOAPRequest(SOAPData c2cSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateToDepartmentC2CSoapRequest.xml");
        c2cSoapReq = getProperlyPreparedC2CPatientDataRequest(c2cSoapReq, c2cSOAPData);
        c2cSoapReq = getProperlyPreparedC2CDepartmentDataRequest(c2cSoapReq, c2cSOAPData);
        c2cSoapReq = getProperlyPreparedCustomerDataRequest(c2cSoapReq, c2cSOAPData);
        c2cSoapReq = getProperlyPreparedC2CConsultationDataRequest(c2cSoapReq, c2cSOAPData);

        return c2cSoapReq;
    }

    public String getPreparedUpdateC2PAppointmentSOAPRequest(SOAPData c2pSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateC2PSOAPRequest.xml");
        c2cSoapReq = getProperlyPreparedC2PPatientDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PProviderDataRequest(c2cSoapReq, c2pSOAPData);
        c2cSoapReq = getProperlyPreparedC2PConsultationDataRequest(c2cSoapReq, c2pSOAPData);


        return c2cSoapReq;
    }

    public String getPreparedUpdateC2CAppointmentSOAPRequest(SOAPData c2CSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\CreateC2CSOAPRequest.xml");
        c2cSoapReq = getProperlyPreparedC2CPatientDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedC2CProviderDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedCustomerDataRequest(c2cSoapReq, c2CSOAPData);
        c2cSoapReq = getProperlyPreparedC2CConsultationDataRequest(c2cSoapReq, c2CSOAPData);

        return c2cSoapReq;
    }

    public String getPreparedCancelC2PAppointmentSOAPRequest(SOAPData c2pSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\C2PCancelRequest.xml");
        return c2cSoapReq.replace("${patientOrgId}", c2pSOAPData.getPatientOrgId())
                .replace("${referenceId}", c2pSOAPData.getReferenceId());
    }

    public String getPreparedCancelC2CAppointmentSOAPRequest(SOAPData c2CSOAPData) {
        String c2cSoapReq = FileUtils.readFile("soap\\C2CCancelRequest.xml");
        return c2cSoapReq.replace("${customerOrgId}", c2CSOAPData.getCustomerOrgId())
                .replace("${referenceId}", c2CSOAPData.getReferenceId());
    }

    private String getProperlyPreparedC2PConsultationDataRequest(final String stringForUpdate, SOAPData c2CSOAPData) {
        return stringForUpdate.replace("${startTime}", c2CSOAPData.getStartTime())
                .replace("${referenceId}", c2CSOAPData.getReferenceId())
                .replace("${endTime}", c2CSOAPData.getEndTime());
    }

    private String getProperlyPreparedC2CConsultationDataRequest(final String stringForUpdate, SOAPData c2CSOAPData){
        return stringForUpdate.replace("${startTime}", c2CSOAPData.getStartTime())
                .replace("${referenceId}",c2CSOAPData.getReferenceId());
    }

    private String getProperlyPreparedC2CPatientDataRequest(final String stringForUpdate, SOAPData c2CSOAPData){
        return stringForUpdate.replace("${patientFirstName}", c2CSOAPData.getPatientFirstName())
                .replace("${patientLastName}", c2CSOAPData.getPatientLastName())
                .replace("${patientPolicyId}", c2CSOAPData.getPatientPolicyId())
                .replace("${patientEmail}", c2CSOAPData.getPatientEmail());
    }

    private String getProperlyPreparedC2PPatientDataRequest(final String stringForUpdate, SOAPData c2pSOAPData) {
        return stringForUpdate.replace("${patientFirstName}", c2pSOAPData.getPatientFirstName())
                .replace("${patientLastName}", c2pSOAPData.getPatientLastName())
                .replace("${patientPolicyId}", c2pSOAPData.getPatientPolicyId())
                .replace("${patientEmail}", c2pSOAPData.getPatientEmail())
                .replace("${patientOrgId}", c2pSOAPData.getPatientOrgId())
                .replace("${patientDepartmentId}", c2pSOAPData.getPatientDepartment());
    }

    private String getProperlyPreparedC2PProviderDataRequest(final String stringForUpdate, SOAPData c2pSOAPData) {
        return stringForUpdate.replace("${providerOrgId}", c2pSOAPData.getProviderOrgId())
                .replace("${providerDepartment}", c2pSOAPData.getProviderDepartment())
                .replace("${providerLicenseId}", c2pSOAPData.getProviderLicenseId())
                .replace("${providerEmail}", c2pSOAPData.getProviderEmail())
                .replace("${providerFirstName}", c2pSOAPData.getProviderFirstName())
                .replace("${providerLastName}", c2pSOAPData.getProviderLastName());
    }

    private String getProperlyPreparedC2PDepartmentDataRequest(final String stringForUpdate, SOAPData c2pSOAPData) {
        return stringForUpdate.replace("${providerOrgId}", c2pSOAPData.getProviderOrgId())
                .replace("${providerDepartment}", c2pSOAPData.getProviderDepartment());
    }

    private String getProperlyPreparedC2CProviderDataRequest(final String stringForUpdate, SOAPData c2CSOAPData){
        return stringForUpdate.replace("${providerOrgId}", c2CSOAPData.getProviderOrgId())
                .replace("${providerDepartment}", c2CSOAPData.getProviderDepartment())
                .replace("${providerLicenseId}", c2CSOAPData.getProviderLicenseId())
                .replace("${providerEmail}", c2CSOAPData.getProviderEmail());
    }

    private String getProperlyPreparedC2CDepartmentDataRequest(final String stringForUpdate, SOAPData c2CSOAPData){
        return stringForUpdate.replace("${providerOrgId}", c2CSOAPData.getProviderOrgId())
                .replace("${providerDepartment}", c2CSOAPData.getProviderDepartment());

    }

    private String getProperlyPreparedCustomerDataRequest(final String stringForUpdate, SOAPData c2pSOAPData) {
        return stringForUpdate.replace("${customerOrgId}", c2pSOAPData.getCustomerOrgId())
                .replace("${customerDepartment}", c2pSOAPData.getCustomerDepartment())
                .replace("${customerLicenseId}", c2pSOAPData.getCustomerLicenseId())
                .replace("${customerEmail}", c2pSOAPData.getCustomerEmail());
    }
}
