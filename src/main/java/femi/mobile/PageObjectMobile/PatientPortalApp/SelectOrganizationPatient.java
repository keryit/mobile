package femi.mobile.PageObjectMobile.PatientPortalApp;

import femi.core.utils.Locales;

public interface SelectOrganizationPatient {

    void selectOrganization(String organizationName);

    boolean isOrganizationScreenOpened();

    void clickNextButton();

    void selectLanguage(Locales locales);
}
