package com.greentstudio.sailingloggerapi.port;

import com.greentstudio.sailingloggerapi.boat.Boat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PortController.class)
@Import({PortRepresentationModelAssembler.class})
public class PortControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private PortRepository portRepository;


    @Test
    public void getShouldFetchAHalDocument() throws Exception {
        Instant instant = Instant.now();
        Port testPort = new Port("testPort");
        List<Boat> boats1 = Arrays.asList(
                new Boat(1L, "Boat A", "Color A", instant, testPort),
                new Boat(2L, "Boat B", "Color B", instant, testPort)
        );
        given(portRepository.findAll()).willReturn(Arrays.asList(
                new Port(1L,"testPort", boats1),
                new Port(2L,"testPort2", boats1)
        ));

        mockMvc.perform(get("/ports").accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk()) //
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.ports[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.ports[0].strName", is("testPort")))
                .andExpect(jsonPath("$._embedded.ports[0]._links.self.href", is("http://localhost/ports/1")))
                .andExpect(jsonPath("$._embedded.ports[0]._links.ports.href", is("http://localhost/ports")))
                .andExpect(jsonPath("$._embedded.ports[0]._links.boats.href", is("http://localhost/ports/1/boats")))
                .andExpect(jsonPath("$._embedded.ports[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.ports[1].strName", is("testPort2")))
                .andExpect(jsonPath("$._embedded.ports[1]._links.self.href", is("http://localhost/ports/2")))
                .andExpect(jsonPath("$._embedded.ports[1]._links.ports.href", is("http://localhost/ports")))
                .andExpect(jsonPath("$._embedded.ports[1]._links.boats.href", is("http://localhost/ports/2/boats")))
                .andReturn();

    }

}