For mobile


Run tests from local machine

1. You need to install Appium Studio
2. Open both devices from Appium Studio(status should be opened)
3. SetUp needed devices (android, IOS) on the BaseMobile class (femi/mobile/BaseMobile.java)

@BeforeClass(alwaysRun = true)
    public void start() throws MalformedURLException {

      androidDriver = setUpApp(OS.ANDROID, App.PATIENT_PORTAL, APPIUM_SERVER_URL, APPIUM_PORT, ANDROID_UDID);

      iosDriver = setUpApp(OS.IOS, App.PATIENT_PORTAL, APPIUM_SERVER_URL, APPIUM_PORT, IPHONE_UDID);

     }