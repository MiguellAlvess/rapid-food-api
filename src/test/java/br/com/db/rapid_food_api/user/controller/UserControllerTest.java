package br.com.db.rapid_food_api.user.controller;

import br.com.db.rapid_food_api.user.dto.UserResponse;
import br.com.db.rapid_food_api.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static br.com.db.rapid_food_api.user.common.UserConstants.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldCreateUserWithValidData() throws Exception {
        UserResponse response = createUserResponse();

        when(userService.create(CREATE_USER_REQUEST)).thenReturn(response);

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(CREATE_USER_REQUEST)))
               .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(response.id().toString()))
               .andExpect(jsonPath("$.name").value(CREATE_USER_REQUEST.name()))
               .andExpect(jsonPath("$.email").value(CREATE_USER_REQUEST.email()))
               .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingUserWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("""
                                                                                           {
                                                                                             "name": "",
                                                                                             "email": "email-invalido",
                                                                                             "password": ""
                                                                                           }
                                                                                           """))
               .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetUserByIdWhenUserExists() throws Exception {
        UserResponse response = createUserResponse();

        when(userService.getById(USER_ID)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", USER_ID)).andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(response.id().toString()))
               .andExpect(jsonPath("$.name").value(response.name()))
               .andExpect(jsonPath("$.email").value(response.email()))
               .andExpect(jsonPath("$.active").value(response.active()));
    }

    @Test
    void shouldUpdateUserWithValidData() throws Exception {
        UserResponse response = new UserResponse(USER_ID, UPDATE_USER_REQUEST.name(), UPDATE_USER_REQUEST.email(),
                                                 UPDATE_USER_REQUEST.active(), null);

        when(userService.update(USER_ID, UPDATE_USER_REQUEST)).thenReturn(response);

        mockMvc.perform(patch("/api/users/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON)
                                                         .content(objectMapper.writeValueAsString(UPDATE_USER_REQUEST)))
               .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(USER_ID.toString()))
               .andExpect(jsonPath("$.name").value(UPDATE_USER_REQUEST.name()))
               .andExpect(jsonPath("$.email").value(UPDATE_USER_REQUEST.email()))
               .andExpect(jsonPath("$.active").value(UPDATE_USER_REQUEST.active()));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingUserWithInvalidData() throws Exception {
        mockMvc.perform(patch("/api/users/{id}", USER_ID).contentType(MediaType.APPLICATION_JSON).content("""
                                                                                                          {
                                                                                                            "name": "Miguel",
                                                                                                            "email": "email-invalido",
                                                                                                            "password": "123"
                                                                                                          }
                                                                                                          """))
               .andExpect(status().isBadRequest());
    }
}
