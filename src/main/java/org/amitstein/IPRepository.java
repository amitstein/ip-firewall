package org.amitstein;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IPRepository {
    Map<Byte, Map<Byte, Map<Byte,List<Byte>>>> ips = new HashMap<Byte, Map<Byte, Map<Byte,List<Byte>>>>();
    
    void add(String ip){
        var octets = convertIPAddressToBytes(ip);
        if (!ips.containsKey(octets[0])) {
            ips.put(octets[0] , new HashMap<Byte, Map<Byte,List<Byte>>>());
        }
        
        var octet1 = ips.get(octets[0]);
        if (!octet1.containsKey(octets[1])) {
            octet1.put(octets[1] , new HashMap<Byte,List<Byte>>());
        }

        var octet2 = octet1.get(octets[1]);
        if (!octet2.containsKey(octets[2])) {
            octet2.put(octets[2] , new ArrayList<Byte>());
        }
        
        var octet3 = octet2.get(octets[2]);
        if (!octet3.contains(octets[3])) {
            octet3.add(octets[3]);
        }

    }

    public boolean exists (String ip) {
        var octets = convertIPAddressToBytes(ip);

        if (!ips.containsKey(octets[0])) {
            return false;
        }
        
        var octet1 = ips.get(octets[0]);
        if (!octet1.containsKey(octets[1])) {
            return false;
        }

        var octet2 = octet1.get(octets[1]);
        if (!octet2.containsKey(octets[2])) {
            return false;
        }
        
        var octet3 = octet2.get(octets[2]);
        return octet3.contains(octets[3]);
    }

    private static Byte[] convertIPAddressToBytes(String ipAddress) {
        String[] octets = ipAddress.split("\\.");
        return Arrays.stream(octets)
            .map(Integer::parseInt) // Convert octet string to integer
            .map(Integer::byteValue) // Convert integer to byte
            .toArray(Byte[]::new); 
    }
}
