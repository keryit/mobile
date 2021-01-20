package femi.core.utils.helpers.postgre;

import femi.core.utils.EnvironmentUtils;
import femi.core.utils.Locales;
import femi.core.utils.UtilityFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yecht.Data;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostgreDBHelper {
    static Logger LOGGER = LoggerFactory.getLogger(PostgreDBHelper.class);
    private static String OTP = "otp";

    public Connection connect(String url, String user, String password) {
        Connection conn = null;
        try {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(url, user, password);
            LOGGER.info("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
        }
        return conn;
    }

    private Connection connectToDB(){        
        Connection connection = null;
        try {
            String host = EnvironmentUtils.getValueByXMLKeyFromTestResources("DB_HOST");
            String port = EnvironmentUtils.getValueByXMLKeyFromTestResources("DB_PORT");
            String dbName = EnvironmentUtils.getValueByXMLKeyFromTestResources("DB_NAME");
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            String user = EnvironmentUtils.getValueByXMLKeyFromTestResources("DB_USER");
            String pass = EnvironmentUtils.getValueByXMLKeyFromTestResources("DB_PASSWORD");
            connection = this.connect(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public String getValidOtp(String userIdentifier) throws SQLException {
        String otp = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from authentication_otp where user_identifier ='" + userIdentifier + "' and valid=true order by last_modified desc");
            while (rs.next()) {
                otp = rs.getString(OTP);
                LOGGER.info("OTP for user " + userIdentifier + ": " + otp);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) connection.close();
        }
       return otp;
    }

    public void markAsDeletedActiveConsultationsAndTerminateSessionForUser(String email){
        markAsDeletedConsultationsForUser(email);
        terminateAllSessionsForUser(email);
    }

    public void setProperLocaleForUser(String email, Locales locale){
        String query = "update user_info " +
                "set value = '" + locale.toString() +"'" +
                "where user_id = (select user_id " +
                "                 from user_list " +
                "                 where email = '" + email +"') " +
                "and key = 'locale';";
        runUpdateQuery(query);
    }

    public void setProperLocaleById(String Id, Locales locale){
        String query = "update user_info " +
                "set value = '" + locale.toString() + "'" +
                "where user_id ='" + Id + "'" +
                "and key = 'locale';";
        runUpdateQuery(query);
    }

    public void markAsDeletedConsultationsForUser(String email){
        String query = "update consultation c\n" +
                "set status ='DELETED'\n" +
                "where c.consultation_id in (select a.consultation_id \n" +
                "                              from appointment_details a\n" +
                "                              where a.appointment_id in (select ap.appointment_id \n" +
                "                                                          from appointment ap \n" +
                "                                                           where owner_id in (select l.user_id " +
                "                                                           from user_list l " +
                "                                                           where l.email like '%" + email +"%')))\n" +
                "      and c.status ='IN_PROGRESS'";
        runUpdateQuery(query);
    }

    public void markAsDeletedCustomerAppointments(String email){
        String query = "update appointment\n" +
                "set status ='DELETED'\n" +
                "where owner_id in (select user_id from user_list where email like '%" +
                email + "%') " +
                "      and status in ('COMPLETED', 'CREATED', 'CANCELLED')";
        runUpdateQuery(query);
    }

    public void markAsDeletedAppointments(String email, String policyId) {

        String userId = getUserIdByPolicyIdFromDb(email, policyId);
        LOGGER.info("policyId is " + policyId);

        String query = "update appointment\n" +
                "set status ='DELETED'\n" +
                "where owner_id = '" + userId +"'" +
                "      and status in ('COMPLETED', 'CREATED', 'CANCELLED')";
        runUpdateQuery(query);

    }

    public void change30MinLateAppointments(String customerId) throws SQLException, ParseException {
        String query = "update appointment\n" +
                "set start_time ='" + minus30MinFromStartTime(getStartTimeAppointmentFromDB(customerId)) + "'\n" +
                "where owner_id='" + customerId + "' and status='CREATED'";
        runUpdateQuery(query);
    }

    public void lockUser(String email, String orgId) throws SQLException, ParseException {
        String query = "update authentication_credentials\n" +
                "set failed_attempts ='10', last_failed_attempt='" + UtilityFunctions.getDateForSetToDB() + "'\n" +
                "where email='" + email + "' and org_id='" + orgId + "'";
        runUpdateQuery(query);
    }

    public void unlockUser(String email, String orgId) throws SQLException, ParseException {
        String query = "update authentication_credentials\n" +
                "set failed_attempts ='0'\n" +
                "where email='" + email + "' and org_id='" + orgId + "'";
        runUpdateQuery(query);
    }

    public void deleteTmpAdminUserFromDb(String email, String cred_id){

        String query = "delete from user_info\n" +
                "where user_id='" + getUserIdFromDb(email) + "'";
        runUpdateQuery(query);

        String query2 = "delete from user_list\n" +
                "where email='" + email + "'";
        runUpdateQuery(query2);

        String query3 = "delete from authentication_password_reset_token\n" +
                "where credentials_id ='" + cred_id + "'";
        runUpdateQuery(query3);

        String query4 = "delete from authentication_credentials_prev_password\n" +
                "where credentials_id='" + cred_id + "'";
        runUpdateQuery(query4);

        String query5 = "delete from authentication_credentials\n" +
                "where email='" + email + "'";
        runUpdateQuery(query5);
    }

    public void deletePatientByPolicyIdFromDb(String email, String policyId) {

        String userId = getUserIdByPolicyIdFromDb(email, policyId);
        LOGGER.info("policyId is " + policyId);

        String query = "delete from user_info\n" +
                "where user_id='" + userId + "'";
        runUpdateQuery(query);

        String query2 = "delete from user_list\n" +
                "where email='" + email + "' and user_id='" +userId + "'";
        runUpdateQuery(query2);

        String query3 = "delete from authentication_policy\n" +
                "where policy_id='" + policyId + "'";
        runUpdateQuery(query3);
    }

    public void deletePatientByPolicyIdFromDb(String policyId) {

        String query= "delete from authentication_policy\n" +
                "where policy_id='" + policyId + "'";
        runUpdateQuery(query);
    }

    private String getUserIdFromDb(String userEmail){
        String userId = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select user_id from user_list where email='" + userEmail + "'");
            while (rs.next()) {
                userId = rs.getString("user_id");
                LOGGER.info("User id is : " + userId);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return userId;
    }

    private String getUserIdByPolicyIdFromDb(String userEmail, String policyId){
        String userId = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select user_id from user_list where email='" + userEmail + "' and external_id='" + policyId + "'");
            while (rs.next()) {
                userId = rs.getString("user_id");
                LOGGER.info("User id is : " + userId);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return userId;
    }

    public String getTmpAdminPassFromDb(String userEmail){
        String pass = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select password_hash from authentication_credentials where email='" + userEmail + "'");
            while (rs.next()) {
                pass = rs.getString("password_hash");
                LOGGER.info("User password_hash is : " + pass);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return pass;
    }

    private String getTokenTmpAdminFromDb(String hashPass){
        String token = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select token from authentication_password_reset_token \n" +
                    "where credentials_id = \n" +
                    "(select credentials_id from authentication_credentials_prev_password \n" +
                    "where password_hash = '" + hashPass + "') and valid = 'true'");
            while (rs.next()) {
                token = rs.getString("token");
                LOGGER.info("User token is : " + token);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return token;
    }

    public String getCredentialsIdFromDb(String hash_pass){
        String credentials_id = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select credentials_id from authentication_credentials_prev_password where password_hash='" + hash_pass + "'");
            while (rs.next()) {
                credentials_id = rs.getString("credentials_id");
                LOGGER.info("User credentials_id is : " + credentials_id);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return credentials_id;
    }


    private String getStartTimeAppointmentFromDB(String customerId) throws SQLException {
        String startTime = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select start_time from appointment where owner_id='" + customerId + "' and status='CREATED'");
            while (rs.next()) {
                startTime = rs.getString("start_time");
                LOGGER.info("Start time for appointment : " + startTime);
                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) connection.close();
        }

        return startTime;
    }

    private String minus30MinFromStartTime(String time) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = dateFormat.parse(time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, -31);
        return dateFormat.format(cal.getTime());
    }

    public void terminateAllSessionsForUser(String email){
        String query = "update ses_session " +
                    "set status = 'TERMINATED' " +
                    "where ses_session.user_id in (select user_id from user_list  where email like '%" +
                    email +
                    "%') " +
                    "and status = 'CONSULTATION'";
        runUpdateQuery(query);
    }

    private void runUpdateQuery(String query){
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Connection closed successfully.");
            }
        }
    }


    private String getConsultationId() throws SQLException {
        String consultationId = null;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select consultation_id from consultation_participant where reference_id = 'P-13a76158-0ae1-48b9-afe4-4b8800e07425' limit 1");
            while (rs.next()) {
                consultationId = rs.getString("consultation_id");

                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) connection.close();
        }
        return consultationId;
    }

    private Integer getParticipantId(String consultId) throws SQLException {
        int participantId = 0;
        Connection connection = null;
        try {
            connection = connectToDB();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select participant_id from consultation_participant where consultation_id = '" + consultId + "' order by participant_id asc limit 1");
            while (rs.next()) {
                participantId = Integer.parseInt(rs.getString("participant_id"));

                break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(connection != null) connection.close();
        }
        return participantId;
    }

    public void deleteConsultation() throws SQLException {

        String consId = getConsultationId();
        System.out.println("Consultation id =  " + consId);

        int participantId = getParticipantId(consId);
        System.out.println("Participant id =  " + participantId);

        String query = "delete from consultation_participant_info where participant_id = '" + participantId +"'";
        runUpdateQuery(query);
        participantId++;
        String query2 = "delete from consultation_participant_info where participant_id = '" + participantId +"'";
        runUpdateQuery(query2);
        participantId++;
        String query3 = "delete from consultation_participant_info where participant_id = '" + participantId +"'";
        runUpdateQuery(query3);
        String query4 = "delete from consultation_info where consultation_id='" + consId +"'";
        runUpdateQuery(query4);
        String query5 = "delete from consultation_participant where consultation_id='" + consId +"'";
        runUpdateQuery(query5);
        String query6 = "delete from consultation_status_log where consultation_id ='" +consId +"'";
        runUpdateQuery(query6);
        String query7 = "delete from consultation_reminder where consultation_id='" + consId +"'";
        runUpdateQuery(query7);
        String query8 = "delete from consultation where consultation_id ='" + consId +"'";
        runUpdateQuery(query8);
    }
}
