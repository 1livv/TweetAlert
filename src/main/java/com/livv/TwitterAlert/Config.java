package com.livv.TwitterAlert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gheorghe on 18/09/2017.
 */
@Service
@PropertySource("classpath:default.properties")
public class Config {

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env= env;
    }

    public String getProperty(String key) {
        return env.getProperty(key);
    }

    public <T>  List<T> getPropertyAsList(String key, Converter<T> converter) {

        List<T> result = new ArrayList<T>();

        String resultString =  env.getProperty(key);
        String [] tokens  = resultString.split(",");

        for (String elem : tokens) {
            result.add(converter.convert(elem));
        }
        return result;
    }
}
