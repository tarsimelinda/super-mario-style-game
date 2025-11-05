package com.codecool.backend.controller;

import com.codecool.backend.model.User;
import com.codecool.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        User user = new User("1", "Mario", 1, "plumber");
        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Mario"))
                .andExpect(jsonPath("$[0].checkpoint").value(1))
                .andExpect(jsonPath("$[0].character").value("plumber"));
    }

    @Test
    void createUser_shouldReturn201() throws Exception {
        User saved = new User("2", "Luigi", 1, "green-bro");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Luigi",
                                  "checkpoint": 1,
                                  "character": "green-bro"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Luigi"))
                .andExpect(jsonPath("$.checkpoint").value(1))
                .andExpect(jsonPath("$.character").value("green-bro"));
    }

    @Test
    void createUser_shouldReturn400IfNameMissing() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "character": "toad"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name: Name is required")));
    }
}
