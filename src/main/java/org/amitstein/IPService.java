package org.amitstein;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped 
public class IPService {

    @ConfigProperty(name = "ip.file") 
    private String fileName;
    private final IPRepository ipRepository;

    public IPService () {
        ipRepository = new IPRepository();
    }

    public boolean exists(String ip) {
        return ipRepository.exists(ip);
    }
    
    @Scheduled(every="30s", concurrentExecution=ConcurrentExecution.SKIP) 
    public void updateIPRepository() {
        long startTime = System.nanoTime();
        readFileFromResources();
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);
    }

    private void readFileFromResources() {
        try (InputStreamReader inputStreamReader = new FileReader(fileName);
             BufferedReader reader = new BufferedReader(inputStreamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    String[] parts = line.split("\t");
                    ipRepository.add(parts[0]);   
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
