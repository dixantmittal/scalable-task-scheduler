package com.ixigo.factory;

import com.ixigo.constants.jobhandlingservice.ServiceConstants;
import com.ixigo.requestserver.AddTaskRequestServer;
import com.ixigo.requestserver.DeleteTaskRequestServer;
import com.ixigo.requestserver.IRequestServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dixant on 28/04/17.
 */
@Component
public class RequestServerProvider {

    @Autowired
    private DeleteTaskRequestServer deleteTaskRequestServer;

    @Autowired
    private AddTaskRequestServer addTaskRequestServer;


    public IRequestServer getRequestServer(String requestType) {
        if (requestType.equals(ServiceConstants.ADD_TASK))
            return addTaskRequestServer;
        else if (requestType.equals(ServiceConstants.DELETE_TASK))
            return deleteTaskRequestServer;
        else
            return null;
    }
}
