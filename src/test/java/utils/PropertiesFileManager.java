package utils;

import driverFactory.DriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class PropertiesFileManager {
    static Logger log = LogManager.getLogger(PropertiesFileManager.class);
    public PropertiesFileManager() {
    }

    public static Properties loadPropertiesFile(final String propFileName) {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(propFileName);
            prop.load(input);
            input.close();
        } catch (IOException ex) {
            log.error("Cannot open properties file: " + propFileName + " exception: \n" + ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.info("loadPropertiesFile()"+e);
                }
            }
        }

        return prop;
    }
    public static String getPropertyValue(String key) {
        String value = null;
        String environment= DriverManager.returnEnvironment();
        //environment= DriverManager.returnEnvironment()==null?"prod":DriverManager.returnEnvironment();
        List<String> results = new ArrayList<String>();
        String filePath=Constants.TEST_RESOURCE_PATH + environment + "/";
        File[] files = new File(Constants.TEST_RESOURCE_PATH + environment + "/").listFiles();
        //If this pathname does not denote a directory, then listFiles() returns null.
        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getPath());
            }
        }
        Properties prop = loadPropertiesFile(Constants.TEST_RESOURCE_PATH + environment + "/environment.properties");
        value = prop.getProperty(key);
        if (value == null || Constants.EMPTY.equals(value)) {
            if (value == null || Constants.EMPTY.equals(value)) {
                int count = 0;
                while (count < results.size()) {
                    Properties searchAllProp = loadPropertiesFile(results.get(count));
                    value = searchAllProp.getProperty(key);
                    if (value != null) {
                        break;
                    }
                    count++;
                }
            }
        }
        return value;
    }
    public static void writeToProperties(String key, String value, String completeFileName) {
        Properties prop = loadPropertiesFile(completeFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(completeFileName);
            prop.setProperty(key, value);
            prop.store(out, null);
            out.close();
        } catch (FileNotFoundException e) {
            log.debug("FileNotFoundException: Error setting the value from properties file for key" + key);
        } catch (IOException io) {
            log.error("IOException: Error setting the value from properties file for key" + key + " exception: \n" + io);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("writeToProperties(): \n" + e);
                }
            }
        }

        log.info("setting key <" + key + "> and value <" + value + ">");
    }

}
