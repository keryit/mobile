package femi.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrSubstitutor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;


public class FileUtils {


    public static String readFileByAbsolutePath(String absolutePath){
        String content = "";
        try {
            content  = new String(Files.readAllBytes(Paths.get(absolutePath)));
        }catch(IOException e){
            e.printStackTrace();
        }
        return content;
    }

    public static String readFile(String pathToFile){
        String content = "";
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(pathToFile);
            content  = IOUtils.toString(is);
        }catch(IOException e){
            e.printStackTrace();
        }
        return content;
    }

    public static String replaceParametrizedData(Map<String, String> expectedData, String fileContent){
        return new StrSubstitutor(expectedData).replace(fileContent);
    }

    public static void writeFile(String fileName, String text){
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter( new FileWriter( fileName));
            writer.write( text);

        }
        catch ( IOException e)
        {
        }
        finally
        {
            try
            {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
    }
}
