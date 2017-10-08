package controllers;

import com.dc.pojo.Devices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xumepa on 9/17/17.
 */

public class EchoTest extends AbstractInitTest{

    @Test
    public void echoTest() throws Exception {
        getMockMvc().perform(post("/echo/")
                .content("abcd")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

//    @Before
    public void discoverTest() throws Exception {
        getMockMvc().perform(get("/echo/discovery")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

    @Test
    public void devicesTest() throws Exception {

        Devices devices = new Devices();
        devices.addDevice(UUID.randomUUID(),"abcd");
        ObjectMapper mapper = new ObjectMapper();
        String a = mapper.writeValueAsString(devices.getDevices());
        getMockMvc().perform(post("/echo/syncDevices")
                .content(a)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

}
