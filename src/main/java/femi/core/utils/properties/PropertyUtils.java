package femi.core.utils.properties;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class PropertyUtils {
   private InputStream inputStream;
   private Properties prop;
   private String propertyPath;

   public PropertyUtils(String propertyPath){
        this.propertyPath = propertyPath;
    }


   public Properties getPropValues(){
        try {
            prop = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(propertyPath);
            if (inputStream != null) {
                Reader reader = new InputStreamReader(inputStream,Charset.forName("UTF-8"));
                prop.load(reader);
            } else {
                throw new FileNotFoundException("property file '" + propertyPath + "' not found in the classpath");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return prop;
    }

}
