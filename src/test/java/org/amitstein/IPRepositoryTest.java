package org.amitstein;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest

public class IPRepositoryTest {

    @Test
    void test_ip_exists() {

        var iprepository = new IPRepository();
        iprepository.add("192.168.1.0");
        
        assertTrue(iprepository.exists("192.168.1.0"));
    }

    @Test
    void test_ip_does_not_exist() {
        var iprepository = new IPRepository();
        iprepository.add("192.168.1.0");
        
        assertFalse(iprepository.exists("192.168.1.1"));
    }

    @Test
    void test_ip_does_not_exist_empty_repository() {
        var iprepository = new IPRepository();
        
        assertFalse(iprepository.exists("192.168.1.1"));
    }    
}
