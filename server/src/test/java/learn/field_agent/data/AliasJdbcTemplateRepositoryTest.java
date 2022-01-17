package learn.field_agent.data;


import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll(){
        List<Alias> aliases = repository.findAll();
        assertNotNull(aliases);
        assertFalse(aliases.isEmpty());
    }

    @Test
    void shouldFindByAgentId(){
        List<Alias> actual = repository.findByAgentId(2);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
    }

    @Test
    void shouldUpdate(){
        Alias alias = new Alias();
        alias.setAliasId(2);
        alias.setAliasName("Mr. Bean");
        alias.setAgentId(2);

        assertTrue(repository.update(alias));
    }

    @Test
    void shouldAdd(){
        Alias alias = new Alias();
        alias.setAliasName("Test Alias");
        alias.setAgentId(3);
        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertEquals(3, actual.getAliasId());
    }

    @Test
    void shouldDelete(){
        assertTrue(repository.deleteById(1));
        assertFalse(repository.deleteById(1));
    }
}
