package controllers;

import com.dc.pojo.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        Card card = new Card();
        card.setCardSign("1");
        card.setCardValue("2");

        ObjectMapper mapper = new ObjectMapper();

        getMockMvc().perform(post("/card/playCard/").content(mapper.writeValueAsString(card))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
    }

}
