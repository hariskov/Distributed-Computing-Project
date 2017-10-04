package controllers;

import com.dc.pojo.Devices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xumepa on 9/24/17.
 */
public class VotingControllerTest extends AbstractInitTest{

    @Autowired
    Devices devices;

    @Test
    public void echoText() throws Exception {
        getMockMvc().perform(get("/voting/startVote")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

//    @Before
//    @Test
//    public void discoverTest() throws Exception {
//        getMockMvc().perform(get("/echo/discovery")
//                .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andDo(print());
//    }


    @Before
    @Test
    public void votingConfig() throws Exception {
//        devices.addDevice(UUID.randomUUID(),"192.168.43.175");
        devices.addDevice(UUID.randomUUID(),"192.168.43.230");
    }
}
