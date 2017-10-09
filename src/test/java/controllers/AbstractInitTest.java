package controllers;

import com.dc.config.WebConfig;
import com.dc.pojo.Device;
import com.dc.pojo.Devices;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by deimos on 17/10/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public abstract class AbstractInitTest {

    private MockMvc mockMvc;

    @Autowired
    Devices devices;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        Device device = new Device(UUID.randomUUID(),"127.0.0.1");
        devices.addDevice(device);
        devices.setCurrentDevice(device);
    }

    public MockMvc getMockMvc() {
        return mockMvc;
    }

}