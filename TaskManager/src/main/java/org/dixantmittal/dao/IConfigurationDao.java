package org.dixantmittal.dao;

import org.dixantmittal.dbmapper.entity.ConfigDetails;

import java.util.List;

/**
 * Created by dixant on 03/04/17.
 */
public interface IConfigurationDao {
    List<ConfigDetails> getAllConfigs();
}
