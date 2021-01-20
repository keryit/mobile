package femi.mobile.PageObjectMobile.PatientPortalApp;

import io.appium.java_client.MobileElement;

public interface OTPScreenPatient {

    boolean isOTPOpened();

    void enterOTP(String otpCode);

    MobileElement getOtp();

    void clickVerifyMeOnOtp();

    void clickSendCodeAgain();
}
