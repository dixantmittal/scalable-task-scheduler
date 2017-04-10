package com.ixigo.dao;

import com.ixigo.dbmapper.entity.ConfigDetails;

import java.util.List;

/**
 * Created by dixant on 03/04/17.
 */
public interface IConfigurationDao {
    List<ConfigDetails> getAllConfigs();
}
