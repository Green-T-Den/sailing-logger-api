package com.greentstudio.sailingloggerapi.boat;

import com.greentstudio.sailingloggerapi.port.Port;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(BoatController.class)
@Import({BoatRepresentationModelAssembler.class})
public class BoatControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private BoatRepository boatRepository;
    Port testPort = new Port("testPort");

    @Test
    public void getShouldFetchAHalDocument() throws Exception {


        Instant instant = Instant.now();
        given(boatRepository.findAll()).willReturn(Arrays.asList(
                new Boat(1L, "Boat A", "Color A", instant, testPort),
                new Boat(2L, "Boat B", "Color B", instant, testPort)
        ));

        mockMvc.perform(get("/boats").accept(MediaTypes.HAL_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk()) //
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$._embedded.boats[0].id", is(1)))
                .andExpect(jsonPath("$._embedded.boats[0].strName", is("Boat A")))
                .andExpect(jsonPath("$._embedded.boats[0].strColor", is("Color A")))
                .andExpect(jsonPath("$._embedded.boats[0].instantBoatConstruction", is(instant.toString())))
                .andExpect(jsonPath("$._embedded.boats[0]._links.self.href", is("http://localhost/boats/1")))
                .andExpect(jsonPath("$._embedded.boats[0]._links.boats.href", is("http://localhost/boats")))
                .andExpect(jsonPath("$._embedded.boats[1].id", is(2)))
                .andExpect(jsonPath("$._embedded.boats[1].strName", is("Boat B")))
                .andExpect(jsonPath("$._embedded.boats[1].strColor", is("Color B")))
                .andExpect(jsonPath("$._embedded.boats[1].instantBoatConstruction", is(instant.toString())))
                .andExpect(jsonPath("$._embedded.boats[1]._links.self.href", is("http://localhost/boats/2")))
                .andExpect(jsonPath("$._embedded.boats[1]._links.boats.href", is("http://localhost/boats")))
                .andExpect((jsonPath("$._links.self.href", is("http://localhost/boats"))))
                .andReturn();

    }

}