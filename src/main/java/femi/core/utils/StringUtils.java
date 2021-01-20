package femi.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String getLinkFrom(String emailBody) {
        Document doc = Jsoup.parse(emailBody);
        Element link = doc.select("a").first();
        return link.attr("href");
    }

    public static boolean isEmailContainsText(String emailBody, String expectedRegexp){
        boolean flag = false;
        Pattern pattern = Pattern.compile(expectedRegexp, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(emailBody);
        if(matcher.find()) flag =true;
        return flag;
    }

    public static String setClinicianAndPatientNames(String text, String expectedClinicianName, String expectedPatientName){
        return text.replace("<clinicianName>", expectedClinicianName).replace("<patientName>", expectedPatientName);
    }

    public static String getPatientFullName(){
        return EnvironmentUtils.getEnvironmentDependentValue("firstName") + " " + EnvironmentUtils.getEnvironmentDependentValue("lastName");
    }

    public static String getPatientEmail(){
        return EnvironmentUtils.getEnvironmentDependentValue("email");
    }

    public static String getClinicianFullNameByType(UserRoleType userRoleType){
        String clinicianName = "";
        switch(userRoleType) {
            case CUSTOMER:
                clinicianName = EnvironmentUtils.getEnvironmentDependentValue("existingCustomerFirstName") +
                        " " + EnvironmentUtils.getEnvironmentDependentValue("existingCustomerLastName");
                break;
            case PROVIDER:
                clinicianName = EnvironmentUtils.getEnvironmentDependentValue("existingProviderFirstName") +
                        " " + EnvironmentUtils.getEnvironmentDependentValue("existingProviderLastName");
                break;
            case PATIENT:
                clinicianName = EnvironmentUtils.getEnvironmentDependentValue("firstName") +
                        " " + EnvironmentUtils.getEnvironmentDependentValue("lastName");
                break;
            case C2P_PROVIDER:
                clinicianName = EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderFirstName") +
                        " " + EnvironmentUtils.getEnvironmentDependentValue("existingC2PProviderLastName");
                break;
        }
        return clinicianName;
    }

    public static String getClinicianEmailByType(UserRoleType userRoleType){
        String clinicianEmail = "";
        switch(userRoleType) {
            case CUSTOMER:
                clinicianEmail = EnvironmentUtils.getEnvironmentDependentValue("existingCustomerEmail");
                break;
            case PROVIDER:
                clinicianEmail = EnvironmentUtils.getEnvironmentDependentValue("existingProviderEmail");
                break;
        }
        return clinicianEmail;
    }

    public static String setParametersToAdmin(String textFromEmail) {
        textFromEmail = textFromEmail.replace("<tmpAdminFirstName>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminFirstName"));
        textFromEmail = textFromEmail.replace("<tmpAdminLastName>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminLastName"));
        textFromEmail = textFromEmail.replace("<tmpClinicianFirstName>", EnvironmentUtils.getEnvironmentDependentValue("tmpClinicianFirstName"));
        textFromEmail = textFromEmail.replace("<tmpClinicianLastName>", EnvironmentUtils.getEnvironmentDependentValue("tmpClinicianLastName"));
        textFromEmail = textFromEmail.replace("<adminOrg>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminOrganizationName"));
        textFromEmail = textFromEmail.replace("<AC_URL>", EnvironmentUtils.getEnvironmentDependentValue("AC_HE_URI"));
        textFromEmail = textFromEmail.replace("<firstTimeEmail>", EnvironmentUtils.getEnvironmentDependentValue("existingTmpAdminEmail"));

        return textFromEmail;
    }

    public static void main(String[] args) {
        String bodyForTest = "\n" +
                "\t\t<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\t\t<html xmlns=\"http://www.w3.org/1999/xhtml\" dir=\"rtl\">\n" +
                "\t\t\t<head>\n" +
                "\t\t\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "\t\t\t\t<title>דוקטור אונליין</title>\n" +
                "\t\t\t\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n" +
                "\t\t\t\t<style type=\"text/css\">\n" +
                "\t\t\t\t\ttable {border-collapse:collapse; margin:0 auto;}\n" +
                "\t\t\t\t\t@media only screen and (max-width:600px){\n" +
                "\t\t\t\t\t\t.mobile-only, td[class=\"mobile-only\"], table[class=\"mobile-only\"], img[class=\"mobile-only\"], div[class=\"mobile-only\"], tr[class=\"mobile-only\"] {\n" +
                "\t\t\t\t\t\t\t\tdisplay:block !important;\n" +
                "\t\t\t\t\t\t\t\twidth:auto !important;\n" +
                "\t\t\t\t\t\t\t\toverflow:visible !important;\n" +
                "\t\t\t\t\t\t\t\theight:auto !important;\n" +
                "\t\t\t\t\t\t\t\tmax-height:inherit !important;\n" +
                "\t\t\t\t\t\t\t\tfont-size:15px !important;\n" +
                "\t\t\t\t\t\t\t\tline-height:21px !important;\n" +
                "\t\t\t\t\t\t\t\tvisibility:visible !important;\n" +
                "\t\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\ttable[class=\"mobile-width\"] {\n" +
                "\t\t\t\t\t\t\twidth:90% !important;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\ttable[class=\"full-width\"] {\n" +
                "\t\t\t\t\t\t\twidth:100% !important;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\timg[class=\"mobile-image\"] {\n" +
                "\t\t\t\t\t\t\tdisplay:inline-block !important;\n" +
                "\t\t\t\t\t\t\theight:auto !important;\n" +
                "\t\t\t\t\t\t\tmax-height:inherit !important;\n" +
                "\t\t\t\t\t\t\toverflow:visible !important;\n" +
                "\t\t\t\t\t\t\tvisibility:visible !important;\n" +
                "\t\t\t\t\t\t\twidth:100% !important;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t.table-mobile {\n" +
                "\t\t\t\t\t\t\tdisplay: table!important;\n" +
                "\t\t\t\t\t\t\twidth: 100%!important;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t\t.font-mobile {\n" +
                "\t\t\t\t\t\t\tfont-size: 22px!important;\n" +
                "\t\t\t\t\t\t}\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t</style>\n" +
                "\t\t\t</head>\n" +
                "\t\t\t<body bgcolor=\"#f3f3f3\" style=\"margin: 0; padding: 0; background-color:#f3f3f3; margin:0; padding:0; -webkit-font-smoothing:antialiased; width:100% !important; -webkit-text-size-adjust:none; background-image: url('img/bg_icons.png'); background-repeat: repeat;\" topmargin=\"0\">\n" +
                "\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t<td style=\"padding-bottom:20px;\">\n" +
                "\t\t\t\t\t\t\t<!-- begin email -->\n" +
                "\t\t\t\t\t\t\t<!-- logo -->\n" +
                "\t\t\t\t\t\t\t\t<table class=\"mobile-width\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"padding-top:20px; padding-bottom:20px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<img alt=\"Online Doctor Logo\" src=\"https://s3.eu-west-2.amazonaws.com/femiglobal/FEM-2152/online_doctor_logo.png\" border=\"0\" height=\"50\" width=\"150\" hspace=\"0\" vspace=\"0\" style=\"display:block; vertical-align:middle;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t<!-- END logo -->\n" +
                "\t\t\t\t\t\t\t<!-- content -->\n" +
                "\t\t\t\t\t\t\t\t<table class=\"mobile-width\" align=\"center\" bgcolor=\"#f9f9f9\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f9f9f9;border-radius: 15px; box-shadow: 0 1px 5px 0 rgba(0, 0, 0, 0.2);\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<td style=\"padding-bottom: 15px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<!-- email header -->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<table class=\"full-width\" align=\"center\" bgcolor=\"#007AC2\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #007AC2;border-radius: 15px 15px 0 0;\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#ffffff; font-family:arial, sans-serif; font-size:26px; line-height:31px; padding-top: 15px; padding-bottom: 15px; text-decoration:none; font-weight:600;\" valign=\"top\">ברוכים הבאים לדוקטור אונליין</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<!-- END email header -->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<!-- welcome message -->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<table class=\"full-width\" align=\"center\" bgcolor=\"#F9F9F9\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #F9F9F9;\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td  dir=\"rtl\" align=\"right\" style=\"color:#282828; font-family:arial, sans-serif; font-size:20px; line-height:23px; padding-top: 15px; padding-bottom: 15px; text-decoration:none; font-weight:400;\" valign=\"top\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tשיחת ייעוץ עם QA Auto-provider תואמה עבורך ל03/07/2018 ב10:20, בנוגע למטופל John Salivan. לחצו <a href=\"https://u2fz4.app.goo.gl/4WBGtzd5qbHXBFJx9\" target=\"_blank\">כאן</a> לפרטים נוספים.\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<!-- END welcome message -->\n" +
                "\t\t\t\t\t\t\t\t\t\t<!-- feedback -->\n" +
                "\t\t\t\t\t\t\t\t\t\t<table class=\"full-width\" align=\"center\" bgcolor=\"#F9F9F9\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #F9F9F9;\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td align=\"right\" style=\"color:#282828; font-family:arial, sans-serif; font-size:18px; line-height:23px; padding-top: 15px; padding-bottom: 15px; text-decoration:none; font-weight:400;\" valign=\"top\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tלכל שאלה ניתן לפנות ל <a href=\"mailto:feedback@femiglobal.com?Subject=Questions%20about%20Online%20Doctor\" target=\"_top\" style=\"text-decoration:underline; color:#007AC2;\">feedback@femiglobal.com</a>. נשמח לעזור!\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<br/>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<br/>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tבשמחה,<br/>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tהחבר'ה בפמי פרימיום.\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"mobile-spacer\" width=\"20\">&nbsp;</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t<!-- END feedback -->\n" +
                "\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t<!-- END content -->\n" +
                "\t\t\t\t\t\t\t<!-- footer -->\n" +
                "\t\t\t\t\t\t\t<table class=\"mobile-width\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#282828; font-family:arial, sans-serif; font-size:14px; line-height:16px; padding-top: 20px; padding-bottom: 10px; text-decoration:none; font-weight:400;\" valign=\"top\">הותאם עבור:</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td align=\"center\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<img alt=\"Online Doctor Logo\" src=\"https://s3.eu-west-2.amazonaws.com/femiglobal/FEM-2152/femihealth_logo_rtl.png\" border=\"0\" height=\"80\" width=\"300\" hspace=\"0\" vspace=\"0\" style=\"display:block; vertical-align:middle;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#8D8D8D; font-family:arial, sans-serif; font-size:14px; line-height:16px; padding-top: 15px; padding-bottom: 15px; text-decoration:none; font-weight:400;\" valign=\"top\">פותח על ידי <b style=\"color: #007AC2;\">פמי פרימיום בע\"מ</b></td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t<td align=\"center\" style=\"color:#8D8D8D; font-family:arial, sans-serif; font-size:14px; line-height:16px; padding-top: 15px; padding-bottom: 15px; text-decoration:none; font-weight:400;\" valign=\"top\">המשביר 1, קומה שנייה &#9679; חולון, ישראל &#9679; 8235478</td>\n" +
                "\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t<!-- END footer -->\n" +
                "\t\t\t\t\t\t\t<!-- END email -->\n" +
                "\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</body>\n" +
                "\t\t</html>\n" +
                "\t\n";

        String regexp = "שיחת ייעוץ עם QA Auto-provider תואמה עבורך ל\\d{2}/\\d{2}/\\d{4} ב\\d{2}:\\d{2}, בנוגע למטופל John Salivan";
        System.out.println(isEmailContainsText(bodyForTest,regexp));
    }
}
