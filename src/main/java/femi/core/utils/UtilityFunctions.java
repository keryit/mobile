package femi.core.utils;

import net.serenitybdd.core.pages.WebElementFacade;
import org.junit.Assert;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This class contains reusable functions that should be shared between all entities
 */
public class UtilityFunctions {

    public static boolean isStringSet(String param) {
        // doesn't ignore spaces, but does save an object creation.
        return param != null && param.length() != 0;
    }

    public static String generateDateValueForSOAP() {
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).plusMinutes(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return ldt.format(formatter);
    }

    public static String generateDateForTomorrowValueForSOAP() {
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).plusDays(1).plusMinutes(6);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateTime = ldt.format(formatter);

        return dateTime;
    }

    public static String generateDateValueForSOAP(int minutesToShift) {
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Jerusalem")).plusMinutes(minutesToShift);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateTime = ldt.format(formatter);

        return dateTime;
    }

    public static String getCurrentDate() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return ldt.format(formatter);
    }

    public static String getDateForSetToDB() {
        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Jerusalem"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ldt.format(formatter);
    }

    public static String getCurrentTime() {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return ldt.format(formatter);
    }

    public static String getDateAWeekAgo() {
        LocalDateTime ldt = LocalDateTime.now().minusMonths(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return ldt.format(formatter);
    }

    /**
     * Generate random integer value
     *
     * @return randomNum - randomly generated value
     */
    public static String generateUniqueTelephoneNumber(String phoneNumber) {

        Random rand = new Random();
        int max = 1000;
        int min = 1;
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return phoneNumber + randomNum;
    }

    public static List<String> getMissedValuesInDropDown(WebElementFacade elementFacade, String value) {
        List<String> expectedValues = Arrays.asList(value.split(","));
        List<String> availableDropDownValues = elementFacade.getSelectOptions();
        List<String> failsContainer = expectedValues.stream().filter(
                elem -> !availableDropDownValues.contains(elem)).collect(Collectors.toList());
        return failsContainer;
    }

    public static void failIfFoundMismatches(String failMessage, List<String> failsContainer) {
        if (failsContainer.size() > 0) {
            Assert.assertTrue(failMessage + failsContainer.toString(), false);
        }
    }

    public static String getTomorrowDate() throws ParseException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return tomorrow.format(formatter);
    }

    public static String getCurrentDay() {
        LocalDateTime tomorrow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return tomorrow.format(formatter);
    }

    public static String getCurrentMonth() {
        LocalDateTime month = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM");
        return month.format(formatter);
    }

    public static String getNextDay() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");
        return tomorrow.format(formatter);
    }

    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Double convertObjToDouble(Object obj) {
        Double convertedObj;
        if (obj instanceof Long) {
            convertedObj = ((Long) obj).doubleValue();
        } else {
            convertedObj = (Double) obj;
        }
        return convertedObj;
    }

    public static String generateUniqueReferenceId() {
        return String.valueOf(System.currentTimeMillis() + Math.random());
    }

}