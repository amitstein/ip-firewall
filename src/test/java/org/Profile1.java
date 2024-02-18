package org;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Map;

import io.quarkus.test.junit.QuarkusTestProfile;

public class Profile1 implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        try{
            String str = "45.155.91.99";
            BufferedWriter writer = new BufferedWriter(new FileWriter("test-file.txt"));
            writer.write(str);    
            writer.close();
        } catch(Exception e){
            e.printStackTrace();
        }
        return Collections.singletonMap("ip.file","test-file.txt");
    }
}
