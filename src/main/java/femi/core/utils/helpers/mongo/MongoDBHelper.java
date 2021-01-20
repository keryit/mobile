package femi.core.utils.helpers.mongo;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import femi.core.utils.EnvironmentUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoDBHelper {
    private static String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    static Logger LOGGER = LoggerFactory.getLogger(MongoDBHelper.class);

    private static DB connectToDb(){
        MongoClient mongo = null;
        String dbName ="";
        String login;
        String pass;
        String mongoHost;
        String mongoPort;
        try {
            login = EnvironmentUtils.getEnvironmentDependentValue("envKey_DB_USER");
            pass = EnvironmentUtils.getEnvironmentDependentValue("envKey_DB_PASSWORD");
            dbName = EnvironmentUtils.getEnvironmentDependentValue("envKey_DB_NAME");
            mongoHost = EnvironmentUtils.getEnvironmentDependentValue("envKey_DB_HOST");
            mongoPort = EnvironmentUtils.getEnvironmentDependentValue("envKey_DB_PORT");

            if(login.isEmpty() && pass.isEmpty()){
                mongo = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
            }else {
                List<ServerAddress> seeds = new ArrayList<>();
                seeds.add(new ServerAddress(mongoHost, Integer.parseInt(mongoPort)));
                List<MongoCredential> credentials = new ArrayList<>();
                credentials.add(
                        MongoCredential.createScramSha1Credential(
                                login,
                                dbName,
                                pass.toCharArray()
                        )
                );
                mongo = new MongoClient(seeds, credentials);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongo.getDB(dbName);
    }

    public static String getLastCreatedToken(String userIdentifier){
        String token = "";
        DBCollection table = connectToDb().getCollection("verifToken");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("userIdentifier", userIdentifier);
        searchQuery.put("valid", true);

        DBCursor cursor = table.find(searchQuery).sort(new BasicDBObject("creationDateTime", -1));
        while (cursor.hasNext()) {
            token = cursor.next().get("_id").toString();
            break;
        }
     return token;
    }

    public static String getLastCreatedOtpCode(String policyIdOrEmail){
        String otpCode = "";
        DBCollection table = connectToDb().getCollection("authentication_otp");
        BasicDBObject searchQuery = new BasicDBObject();
        searchQuery.put("userIdentifier", policyIdOrEmail);
        searchQuery.put("valid", true);

        DBCursor cursor = table.find(searchQuery).sort(new BasicDBObject("creationDateTime", 1));
        cursor = cursor.sort(new BasicDBObject("creationDateTime", -1));
        while (cursor.hasNext()) {
            otpCode = cursor.next().get("otp").toString();
            break;
        }
        return otpCode;
    }

    public static void resetFailedAttemptsRowByEmail(String email){
        try {
            DBObject objForUpdate = null;
            DBCollection table = connectToDb().getCollection("credentials");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("_id", email);

            DBCursor cursor = table.find(searchQuery);
            while (cursor.hasNext()) {
                objForUpdate = cursor.next();
                objForUpdate.put("failedAttemptsRow", 0);
            }

            if(email.matches(EMAIL_PATTERN) && cursor.size() > 0) {
                table.update(searchQuery, objForUpdate);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void deleteVerifTokenAndOtpByEmail(String email){
        String [] collections = {"otp","verifToken"};
        for(String collect: collections) {
            DBCollection table = connectToDb().getCollection(collect);
            BasicDBObject searchQuery = new BasicDBObject() {{
                put("userIdentifier", email);
            }};
            if(table.find(searchQuery).size() > 0) {
                table.remove(searchQuery);
                LOGGER.info("email address: " + email + " has been deleted from collection '" + collect + "'");
            }
        }
    }

    public static void doUnblockCurrentIP(){
        try {
            DBCollection table = connectToDb().getCollection("ipAddress");
            String ip = InetAddress.getLocalHost().toString().replaceAll(".*/", "");
            BasicDBObject searchQuery = new BasicDBObject() {{
                put("_id", ip);
            }};
            if (table.find(searchQuery).size() > 0) {
                table.remove(searchQuery);
                LOGGER.info("ip address: " + ip + " has been deleted from collection 'ipAddress'");
            }
        }catch(UnknownHostException ex){
            ex.printStackTrace();
        }
    }

    public static void clearPreviousPasswordsInCredentialsTable(String email){
        try {
            DBCollection table = connectToDb().getCollection("credentials");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("_id", email);
            DBCursor cursor = table.find(searchQuery);
            DBObject objForUpdate = null;
            while (cursor.hasNext()) {
                objForUpdate = cursor.next();
                objForUpdate.put("prevPasswordHashes", "");
            }
            table.update(searchQuery, objForUpdate);
            LOGGER.info("prevPasswordHashes for email: " + email + " has been deleted from collection 'credentials'");

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void clearCurrentPasswordInCredentialsTable(String email){
        setCurrentPasswordInCredentialsTable(email, "");
    }

    public static void setCurrentPasswordInCredentialsTable(String email, String pass){
        try {
            DBCollection table = connectToDb().getCollection("credentials");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("_id", email);
            DBCursor cursor = table.find(searchQuery);
            DBObject objForUpdate = null;
            while (cursor.hasNext()) {
                objForUpdate = cursor.next();
                objForUpdate.put("passwordHash", pass);
            }
            table.update(searchQuery, objForUpdate);
            LOGGER.info("passwordHash for email: " + email + " has been updated in collection 'credentials'");

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static List<Map<String,String>> getDataFromCollectionByCriteria(String collectionName,
                                                                   Map<String, Object> searchCriteria,
                                                                   List<String> ignoreColumns){
        List<Map<String,String>> data = new ArrayList();
        DBCollection table = connectToDb().getCollection(collectionName);
        BasicDBObject searchQuery = new BasicDBObject();
        searchCriteria.forEach((k,v) -> searchQuery.put(k,v));

        DBCursor cursor = table.find(searchQuery);
        while (cursor.hasNext()) {
            data.add(getData((BasicDBObject) cursor.next(), ignoreColumns));
        }
        return data;
    }

    private static HashMap<String,String> getData(BasicDBObject cursor, List<String> ignoreColumns){
        HashMap<String,String> map = new HashMap<>();
        cursor.forEach((k,v) -> {
            if (!ignoreColumns.contains(k)){map.put(k,v.toString());}
        });
        return map;
    }

}
