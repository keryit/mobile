package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.utils.Locales;

public interface MainScreenPatient {

    void openMenu();

    void doSignOut();

    void openSettings();

    void changeLanguage(Locales locales);

    void openAppointmentsFromMenu();

    void waitForSignOutPopUp();

}
