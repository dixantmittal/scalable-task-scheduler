package org.dixantmittal.dao;

import org.dixantmittal.dbmapper.entity.TaskHistoryEntity;

/**
 * Created by dixant on 03/04/17.
 */
public interface ITaskDao {

    Boolean addTaskHistory(TaskHistoryEntity params);
}
