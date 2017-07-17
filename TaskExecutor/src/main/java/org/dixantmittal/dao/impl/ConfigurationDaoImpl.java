package org.dixantmittal.dao.impl;

import org.dixantmittal.dao.IConfigurationDao;
import org.dixantmittal.dbmapper.IConfigDetailsMapper;
import org.dixantmittal.dbmapper.entity.ConfigDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dixant on 31/03/17.
 */
@Service
public class ConfigurationDaoImpl implements IConfigurationDao {

    @Autowired
    IConfigDetailsMapper configDetailsMapper;

    @Override
    public List<ConfigDetails> getAllConfigs() {
        return configDetailsMapper.getAllConfigs();
    }
}
