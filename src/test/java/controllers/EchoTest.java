package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xumepa on 9/17/17.
 */

public class EchoTest extends AbstractInitTest{

    @Test
    public void echoTest() throws Exception {
        getMockMvc().perform(post("/echo/")
//                .content("abcd")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

//    @Before
    public void discoverTest() throws Exception {
        getMockMvc().perform(get("/echo/discovery")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

//    @Ignore
    @Test
    public void devicesTest() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        String a = mapper.writeValueAsString(devices.getDevices());
        getMockMvc().perform(post("/echo/syncDevices")
                .content(a)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

}
