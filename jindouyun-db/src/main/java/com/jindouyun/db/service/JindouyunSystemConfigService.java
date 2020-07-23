package com.jindouyun.db.service;

import com.jindouyun.db.dao.JindouyunSystemMapper;
import com.jindouyun.db.domain.JindouyunSystem;
import com.jindouyun.db.domain.JindouyunSystemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JindouyunSystemConfigService
 * @Description
 * @Author Bruce
 * @Date 2020/7/23 8:47 下午
 */
@Service
@Transactional
public class JindouyunSystemConfigService {
    
    @Autowired
    private JindouyunSystemMapper systemMapper;

    public Map<String, String> queryAll() {
        JindouyunSystemExample example = new JindouyunSystemExample();
        example.or().andDeletedEqualTo(false);

        List<JindouyunSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> systemConfigs = new HashMap<>();
        for (JindouyunSystem item : systemList) {
            systemConfigs.put(item.getKeyName(), item.getKeyValue());
        }

        return systemConfigs;
    }

    public Map<String, String> listMail() {
        JindouyunSystemExample example = new JindouyunSystemExample();
        example.or().andKeyNameLike("Jindouyun_mall_%").andDeletedEqualTo(false);
        List<JindouyunSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(JindouyunSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listWx() {
        JindouyunSystemExample example = new JindouyunSystemExample();
        example.or().andKeyNameLike("Jindouyun_wx_%").andDeletedEqualTo(false);
        List<JindouyunSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(JindouyunSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listOrder() {
        JindouyunSystemExample example = new JindouyunSystemExample();
        example.or().andKeyNameLike("Jindouyun_order_%").andDeletedEqualTo(false);
        List<JindouyunSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(JindouyunSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listExpress() {
        JindouyunSystemExample example = new JindouyunSystemExample();
        example.or().andKeyNameLike("Jindouyun_express_%").andDeletedEqualTo(false);
        List<JindouyunSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(JindouyunSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            JindouyunSystemExample example = new JindouyunSystemExample();
            example.or().andKeyNameEqualTo(entry.getKey()).andDeletedEqualTo(false);

            JindouyunSystem system = new JindouyunSystem();
            system.setKeyName(entry.getKey());
            system.setKeyValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            systemMapper.updateByExampleSelective(system, example);
        }

    }

    public void addConfig(String key, String value) {
        JindouyunSystem system = new JindouyunSystem();
        system.setKeyName(key);
        system.setKeyValue(value);
        system.setAddTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        systemMapper.insertSelective(system);
    }
    
}
