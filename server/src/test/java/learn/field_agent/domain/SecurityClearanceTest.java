package learn.field_agent.domain;


import learn.field_agent.models.SecurityClearance;
import learn.field_agent.data.SecurityClearanceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SecurityClearanceTest {

    @Autowired
    SecurityClearanceService service;
    @MockBean
    SecurityClearanceRepository repository;

    @Test
    void shouldAdd() {
        SecurityClearance securityClearance = makeClearance();
        SecurityClearance mockOut = makeClearance();
        mockOut.setSecurityClearanceId(1);

        when(repository.add(securityClearance)).thenReturn(mockOut);

        Result<SecurityClearance> actual = service.add(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotAddSameName(){
        SecurityClearance one = makeClearance();
        one.setSecurityClearanceId(4);
//        SecurityClearance two = new SecurityClearance(4,"A Few Secrets");

        Result<SecurityClearance> result = service.add(one);
        assertEquals(ResultType.INVALID, result.getType());
//        Result<SecurityClearance> result1 = service.add(two);
//        assertEquals(ResultType.SUCCESS, result1.getType());
    }


    @Test
    void shouldNotAddNoName(){
        SecurityClearance securityClearance = makeClearance();
        securityClearance.setSecurityClearanceId(0);
        securityClearance.setName(null);

        Result<SecurityClearance> result = service.add(securityClearance);
        assertEquals(ResultType.INVALID, result.getType());
    }

    @Test
    void shouldNotAddDuplicate(){
        SecurityClearance expected = makeClearance();
        SecurityClearance arg = makeClearance();
        arg.setSecurityClearanceId(0);

        when(repository.add(arg)).thenReturn(expected);
        Result<SecurityClearance> result = service.add(arg);
        assertEquals(ResultType.SUCCESS, result.getType());

        assertEquals(expected, result.getPayload());
    }

    @Test
    void shouldUpdate(){
        SecurityClearance securityClearance = makeClearance();
        securityClearance.setSecurityClearanceId(1);

        when(repository.update(securityClearance)).thenReturn(true);

        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdateNoName(){
        SecurityClearance securityClearance = makeClearance();
        Result<SecurityClearance> actual = service.update(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());

        securityClearance = makeClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName("  ");
        actual = service.update(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());

        securityClearance = makeClearance();
        securityClearance.setSecurityClearanceId(1);
        securityClearance.setName(null);
        actual = service.update(securityClearance);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldDelete(){
        when(repository.deleteById(2)).thenReturn(true);
        assertTrue(service.deleteById(2));
    }


    SecurityClearance makeClearance() {
        SecurityClearance securityClearance = new SecurityClearance();
        securityClearance.setName("Some Secrets");
        return securityClearance;
    }
}
