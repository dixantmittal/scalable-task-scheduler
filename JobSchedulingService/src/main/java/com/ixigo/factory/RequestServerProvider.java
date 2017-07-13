package com.ixigo.factory;

import com.ixigo.constants.jobhandlingservice.ServiceConstants;
import com.ixigo.requestserver.impl.AddTaskRequestServer;
import com.ixigo.requestserver.impl.DeleteTaskRequestServer;
import com.ixigo.requestserver.IRequestServer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by dixant on 28/04/17.
 */
public class RequestServerProvider {

    private static RequestServerProvider _INSTANCE = new RequestServerProvider();

    @Autowired
    private DeleteTaskRequestServer deleteTaskRequestServer;
    @Autowired
    private AddTaskRequestServer addTaskRequestServer;

    public static RequestServerProvider getInstance() {
        return _INSTANCE;
    }

    public IRequestServer getRequestServer(String requestType) {
        if (requestType.equals(ServiceConstants.ADD_TASK))
            return addTaskRequestServer;
        else if (requestType.equals(ServiceConstants.DELETE_TASK))
            return deleteTaskRequestServer;
        else
            return null;
    }
}
