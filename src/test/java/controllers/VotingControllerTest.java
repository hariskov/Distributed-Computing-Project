package controllers;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by xumepa on 9/24/17.
 */
public class VotingControllerTest extends AbstractInitTest{

    @Test
    @Ignore
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

}