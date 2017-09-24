package controllers;

import com.dc.config.WebConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xumepa on 9/17/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
@WebAppConfiguration
public class EchoTest extends AbstractInitTest{

    @Test
    public void echoText() throws Exception {
        getMockMvc().perform(post("/echo/")
                .content("abcd")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

    @Test
    public void discoverTest() throws Exception {
        getMockMvc().perform(get("/echo/discovery")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

}
