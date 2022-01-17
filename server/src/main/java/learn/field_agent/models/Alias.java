package learn.field_agent.models;

public class Alias {
    private int aliasId;
    private String aliasName;
    private String aliasPersona;
    private int agentId;

    public Alias(int aliasId, String aliasName, String aliasPersona, int agentId) {
    }
    public Alias(){};

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    private Agent agent;

    public int getAliasId() {
        return aliasId;
    }

    public void setAliasId(int aliasId) {
        this.aliasId = aliasId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAliasPersona() {
        return aliasPersona;
    }

    public void setAliasPersona(String aliasPersona) {
        this.aliasPersona = aliasPersona;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }


}
