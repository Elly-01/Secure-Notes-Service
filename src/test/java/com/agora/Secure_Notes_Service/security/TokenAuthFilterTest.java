package com.agora.Secure_Notes_Service.security;

import com.agora.Secure_Notes_Service.service.NotesService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TokenAuthFilterTest.DummyController.class) // only load dummy controller
class TokenAuthFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotesService notesService; // mock so Spring context starts


    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("notes.api.token", () -> "test-token");
    }

    // Dummy controller so MockMvc has an endpoint
    @RestController
    static class DummyController {
        @GetMapping("/test")
        public String test() {
            return "OK";
        }
    }

    @Test
    void testMissingAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Missing Authorization header"));
    }

    @Test
    void testInvalidToken() throws Exception {
        mockMvc.perform(get("/test")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"));
    }
}
