package femi.mobile.PageObjectMobile.PatientPortalApp;

import io.appium.java_client.MobileElement;

public interface LoginScreenPatient {

    boolean isLoginScreenOpened();

    boolean isLoginScreenWithPhoneNumberOpened();

    void putPatientPolicyId(String policyId);

    MobileElement getPolicyIdField();

    void putPatientPhone(String phoneNumber);

    MobileElement getPatientPhoneField();

    void clickNextBtn();

    boolean isLogoPresent();

    boolean isTermsAndConditionsFormAppears();

    void confirmTermAndConditions();

    void declineTermsAndConditions();

    boolean waitDeclineTermsAndConditionsPopUp();

    void confirmDeclineTermsAndConditionOnPopUp();

    void cancelDeclineTermsAndConditionsOnPopUp();
}
