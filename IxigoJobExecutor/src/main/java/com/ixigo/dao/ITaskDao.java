package com.ixigo.dao;

import com.ixigo.dbmapper.entity.TaskHistoryEntity;

/**
 * Created by dixant on 03/04/17.
 */
public interface ITaskDao {

    Boolean addTaskHistory(TaskHistoryEntity params);
}
