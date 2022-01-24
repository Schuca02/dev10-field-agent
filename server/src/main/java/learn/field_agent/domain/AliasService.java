package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AliasService {

    private final AliasRepository repository;

    public AliasService(AliasRepository repository) {
        this.repository = repository;
    }

    public List<Alias> findAll() {
        return repository.findAll();
    }


    public Result<Alias> add(Alias alias) {
        Result<Alias> result = validate(alias);

        if(!result.isSuccess()){
            return result;
        }

        if(alias.getAliasId() != 0){
            result.addMessage("aliasId cannot be set for 'add' operation", ResultType.INVALID);
            return result;
        }

        alias = repository.add(alias);
        result.setPayload(alias);
        return result;
    }

    public Result<Alias> update(Alias alias) {
        Result<Alias> result = validate(alias);
        if (!result.isSuccess()) {
            return result;
        }

        if (alias.getAliasId() <= 0) {
            result.addMessage("agencyId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!repository.update(alias)) {
            String msg = String.format("aliasId: %s, not found", alias.getAliasId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int aliasId) {
        return repository.deleteById(aliasId);
    }


    private Result<Alias> validate(Alias alias) {
        Result<Alias> result = new Result<>();

        if (alias == null) {
            result.addMessage("alias cannot be null", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getName())) {
            result.addMessage("alias needs a name", ResultType.INVALID);
            return result;
        }

        if (Validations.isNullOrBlank(alias.getPersona()) &&
                repository.findAll().stream().anyMatch(i -> Objects.equals(i.getName(), alias.getName()))) {
            result.addMessage("alias by that name needs a persona", ResultType.INVALID);
            return result;
        }
        return result;
    }
}
