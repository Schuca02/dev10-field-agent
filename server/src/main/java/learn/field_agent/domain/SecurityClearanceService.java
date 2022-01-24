package learn.field_agent.domain;

import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.SecurityClearance;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityClearanceService {

    private final SecurityClearanceRepository repository;

    public SecurityClearanceService(SecurityClearanceRepository repository) {
        this.repository = repository;
    }

    public List<SecurityClearance> findAll() {
        return repository.findAll();
    }

    public SecurityClearance findById(int securityClearanceId) {
        return repository.findById(securityClearanceId);
    }

    public SecurityClearance findByName(String securityClearanceName) {
        return repository.findByName(securityClearanceName);
    }

    public Result<SecurityClearance> add(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }
        if (securityClearance.getSecurityClearanceId() != 0) {
            result.addMessage("securityClearanceId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        if(!(findByName(securityClearance.getName()) == null)){
            result.addMessage("Name must be unique", ResultType.INVALID);
           return result;
        }

        securityClearance = repository.add(securityClearance);
        result.setPayload(securityClearance);
        return result;
    }

    public Result<SecurityClearance> update(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = validate(securityClearance);
        if (!result.isSuccess()) {
            return result;
        }
        if (securityClearance.getSecurityClearanceId() <= 0) {
            result.addMessage("securityClearanceId must be set for `update` operation", ResultType.INVALID);
            return result;
        }
        if (!repository.update(securityClearance)) {
            String msg = String.format("securityClearanceId: %s, not found", securityClearance.getSecurityClearanceId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        SecurityClearance duplicate = findByName(securityClearance.getName());

        if(duplicate != null && duplicate.getSecurityClearanceId() != securityClearance.getSecurityClearanceId()){
            result.addMessage("Name must be unique", ResultType.INVALID);
            return result;
        }

        return result;
    }


    public boolean deleteById(int securityClearanceId) {
        return repository.deleteById(securityClearanceId);
    }

    private Result<SecurityClearance> validate(SecurityClearance securityClearance) {
        Result<SecurityClearance> result = new Result<>();

        if (securityClearance == null) {
            result.addMessage("securityClearance cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(securityClearance.getName())) {
            result.addMessage("name is required", ResultType.INVALID);
            return result;
        }

        return result;
    }
}
