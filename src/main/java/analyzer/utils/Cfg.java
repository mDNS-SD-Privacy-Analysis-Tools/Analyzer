package analyzer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by codewing on 25.01.2017.
 */
public class Cfg {

    private Properties properties;

    private static Cfg cfg = null;

    public static Cfg getCfg(){
        if(cfg == null){
            cfg = new Cfg();
            cfg.getPropValues();
        }
        return cfg;
    }

    public void getPropValues() {
        properties = new Properties();
        InputStream input = null;

        try {
            String filename = "config.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
            }

            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Properties getProp() {
        return properties;
    }

    public String getServerURL(){
        return properties.getProperty("server");
    }
}
