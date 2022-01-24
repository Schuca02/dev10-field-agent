package learn.field_agent.data;

import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }


    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(3);
        assertEquals(null, actual);
    }


    @Test
    void shouldFindAll(){
        List<SecurityClearance> clearances = repository.findAll();
        assertNotNull(clearances);
    }

    @Test
    void shouldFindByName(){
        SecurityClearance actual = repository.findByName("Top Secret");
        assertEquals(2, actual.getSecurityClearanceId());

        actual = repository.findByName("Jeff");
        assertNull(actual);
    }

    @Test
    void shouldAdd(){
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Test Clearance");
        SecurityClearance actual = repository.add(securityClearance);
        assertNotNull(actual);
        assertEquals(3, actual.getSecurityClearanceId());

    }

    @Test
    void shouldUpdate(){
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setSecurityClearanceId(2);
        securityClearance.setName("Super Top Secret");

        assertTrue(repository.update(securityClearance));
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.deleteById(3));
        assertFalse(repository.deleteById(3));
    }



}