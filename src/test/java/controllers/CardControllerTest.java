package controllers;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/**
 * Created by xumepa on 10/9/17.
 */
public class CardControllerTest extends AbstractInitTest {

    @Test
    public void playCardTest() throws Exception {
        String card = "0";
        getMockMvc().perform(post("/card/playCard/").content(card)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

}
