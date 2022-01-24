package learn.field_agent.domain;


import learn.field_agent.models.Alias;
import learn.field_agent.data.AliasRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class AliasServiceTest {

    @Autowired
    AliasService service;
    @MockBean
    AliasRepository repository;

    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias mockOut = makeAlias();
        mockOut.setAliasId(1);

        when(repository.add(alias)).thenReturn(mockOut);

        Result<Alias> actual = service.add(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddNoName() {
        Alias alias = makeAlias();
        alias.setAliasId(0);
        alias.setName(" ");

        Result<Alias> result = service.add(alias);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddSameNameNoPersona() {
        Alias alias = makeAlias();
        Alias other = new Alias(0, "006", " ", 1);

        Result<Alias> result = service.add(alias);
        assertEquals(ResultType.SUCCESS, result.getType());
        result = service.add(other);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasId(1);

        when(repository.update(alias)).thenReturn(true);

        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.SUCCESS, actual.getType());

    }

    @Test
    void shouldNotUpdate() {
        Alias alias = makeAlias();
        Result<Alias> actual = service.update(alias);
        assertEquals(ResultType.INVALID, actual.getType());

        alias = makeAlias();
        alias.setAliasId(0);
        alias.setName(" ");

        Result<Alias> result = service.update(alias);
        assertEquals(ResultType.INVALID, result.getType());

    }

    @Test
    void shouldDelete() {
        when(repository.deleteById(2)).thenReturn(true);
        assertTrue(service.deleteById(2));
    }

    Alias makeAlias() {
        Alias alias = new Alias();
        alias.setName("006");
        alias.setPersona("Almost 007 but not quite");
        alias.setAgentId(1);
        return alias;
    }
}
