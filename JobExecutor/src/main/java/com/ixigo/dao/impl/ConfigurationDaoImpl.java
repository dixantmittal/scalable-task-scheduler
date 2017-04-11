package com.ixigo.dao.impl;

import com.ixigo.dao.IConfigurationDao;
import com.ixigo.dbmapper.IConfigDetailsMapper;
import com.ixigo.dbmapper.entity.ConfigDetails;
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
