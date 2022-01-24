import { useState, useEffect } from "react";

function FieldAgents(){
    const [ errors, setErrors ] = useState([]);
    const [ agents, setAgents ] = useState([]);
    const [ firstName, setFirstName ] = useState("");
    const [ lastName, setLastName ] = useState("");
    const [ dob, setDob ] = useState("");
    const [ heightInInches, setHeightInInches ] = useState("");
    const [ editingAgentId, setEditingAgentId ] = useState(NaN); 
    const [ view, setView ] = useState("List");  //"Add" and "Edit"


    useEffect(() => {
        fetch("http://localhost:8080/api/agent")
        .then(response => response.json())
        .then(data => setAgents(data))
        .catch(error => console.log(error));
    }, []);



    const addAgent = () => {
        const newAgent = {
            firstName,
            lastName,
            dob,
            heightInInches
        };

        const initAdd = {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newAgent)
        }

        fetch("http://localhost:8080/api/agent", initAdd)
            .then(response => {
                if (response.status === 201 || response.status === 400) {
                    return response.json();
                }
                return Promise.reject("Unexpected response from the server");
            })
            .then(data => {
                if (data.agentId) {
                    setAgents([...agents, data]);
                    setErrors([]);
                    setView("List");
                    setFirstName("");
                    setLastName("");
                    setDob("");
                    setHeightInInches(0)
                } else {
                    setErrors(data);
                }
            })
            .catch(error => console.log(error));
    }

    const onSubmit = (event) => {
        event.preventDefault();
        if(isNaN(editingAgentId)){
            addAgent();
        } else {
            editAgent();
        }
    }

    const editAgent = () => {
        const updateAgentObj = {
            agentId: editingAgentId,
            firstName,
            lastName,
            dob,
            heightInInches
        };

        const initUpdate = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(updateAgentObj)
        }

        fetch(`http://localhost:8080/api/agent/${editingAgentId}`, initUpdate)
            .then(response => {
                if (response.status === 204){
                    return null;
                } else if (response.status === 400) {
                    return response.json();
                }
                return Promise.reject("Unexpected response from the server.");
            })
            .then(data => {
                if (!data) {
                    const editingAgents = [...agents];
                    const indexOfEdit = editingAgents.findIndex(agent => agent.agentId === editingAgentId);
                    editingAgents[indexOfEdit] = updateAgentObj;
                    setAgents(editingAgents);
                    setEditingAgentId(NaN);
                    setErrors([]);
                    setView("List");
                    setFirstName("");
                    setLastName("");
                    setDob("");
                    setHeightInInches("");
                } else {
                    setErrors(data);
                }
            })
            .catch(error => console.log(error))
    }


    const deleteAgent = (agentId) => {
        fetch(`http://localhost:8080/api/agent/${agentId}`, {method: "DELETE" })
        .then(response => {
            if (response.status === 204) {
                const filteredAgents = agents.filter(agent => agent.agentId !== agentId);
                setAgents(filteredAgents);
            } else if (response.status === 404) {
                return Promise.reject("Field Agent Not Found");
            } else {
                return Promise.reject(`Delete failed with status: ${response.status}`);
            }
        })
        .catch(console.log);
    }



    const editView = (agentId) => {
        setView("Edit");
        setEditingAgentId(agentId);
        const agentEdit = agents.find(agent => agent.agentId === agentId);
        setFirstName(agentEdit.firstName);
        setLastName(agentEdit.lastName);
        setDob(agentEdit.dob);
        setHeightInInches(agentEdit.heightInInches);
    }

    const renderAgents = () => {
        return agents.map(agent =>
            <li key={agent.agentId} className="list-group-item">
                <div className="row">
                    <div className="col-8">
                        {agent.firstName}
                    </div>
                    <div className="col-8">
                        {agent.lastName}
                    </div>
                    <div className="col-2">
                        <span className="clickable" onClick={() => editView(agent.agentId)}>
                        ✏️
                        </span>
                    </div>
                    <div className="col-2">
                        <span className="clickable" onClick={() => deleteAgent(agent.agentId)}>
                        👻  
                        </span>
                    </div>
                </div>
            </li>
        )

    }

    const renderErrors = () => {
        return errors.map(error => <li key={error}>{error}</li>);
    }

    return (
        <>
            {(errors.length > 0) && (
                <div className ="ALERT WILL ROBINSON -DANGER-">
                    <ul>
                        {renderErrors()}
                    </ul>
                </div>
            )}

            {(view === "Add" || view === "Edit") && (
                <div className ="row">
                    <div className="col">
                        <form onSubmit={onSubmit}>
                            <label htmlFor="first name">First Name: </label>
                            <input 
                                id="first name"
                                name="first name"
                                type="text"
                                value={firstName}
                                onChange={event => setFirstName(event.target.value)}
                            /> <label htmlFor="last name">Last Name: </label>
                            <input 
                                id="last name"
                                name="last name"
                                type="text"
                                value={lastName}
                                onChange={event => setLastName(event.target.value)}
                            /> <label htmlFor="dob">Date of Birth: </label>
                            <input 
                                id="dob"
                                name="dob"
                                type= {Date}
                                value={dob}
                                onChange={event => setDob(event.target.value)}
                            /> <label htmlFor="height">Height: </label>
                            <input 
                                id="height"
                                name="height"
                                type= {Number}
                                value={heightInInches}
                                onChange={event => setHeightInInches(event.target.value)}
                            /><br />
                            <button type="submit">
                                {editingAgentId > 0 ? "Edit Field Agent" : "Add Field Agent"}
                            </button>
                        </form>
                    </div>
                </div>
            )}

            {view === "List" && (
                <div className = "row">
                    <div className="col-10">
                        <ul className="list-group">
                            {renderAgents()}
                        </ul>
                    </div>
                    <div className="col-2">
                        <button className="btn btn-outline-info btn-lg" onClick={() => setView("Add")}>➕</button>
                    </div>
                </div>
            )}
         </>
    )
}

export default FieldAgents;