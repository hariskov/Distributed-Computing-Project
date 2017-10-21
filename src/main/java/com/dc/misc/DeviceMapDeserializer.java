package com.dc.misc;

import com.dc.pojo.Device;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

/**
 * Created by xumepa on 10/21/17.
 */
public class DeviceMapDeserializer extends KeyDeserializer {
    @Override
    public Device deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        //Use the string key here to return a real map key object
        return null;
    }
}
