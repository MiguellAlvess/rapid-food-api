package br.com.db.rapid_food_api.vendor.controller;

import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import br.com.db.rapid_food_api.vendors.controller.VendorController;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.dto.CreateVendorRequest;
import br.com.db.rapid_food_api.vendors.dto.VendorResponse;
import br.com.db.rapid_food_api.vendors.exceptions.DuplicateVendorException;
import br.com.db.rapid_food_api.vendors.exceptions.VendorNotFoundException;
import br.com.db.rapid_food_api.vendors.mapper.VendorMapper;
import br.com.db.rapid_food_api.vendors.service.VendorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VendorController.class)
@AutoConfigureMockMvc(addFilters = false)
class VendorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VendorService vendorService;

    @MockitoBean
    private VendorMapper vendorMapper;

    @Test
    @DisplayName("Deve retornar 201 no cadastro com sucesso")
    void shouldReturn201WhenCreateIsSuccessful() throws Exception {
        CreateVendorRequest request = VendorTestFactory.createValidRequest();
        Vendor vendorMock = VendorTestFactory.createValidVendor();
        VendorResponse responseMock = VendorTestFactory.createValidResponse();

        when(vendorMapper.toEntity(any())).thenReturn(vendorMock);
        when(vendorService.create(any())).thenReturn(vendorMock);
        when(vendorMapper.toResponse(any())).thenReturn(responseMock);

        mockMvc.perform(post("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(VendorTestFactory.DEFAULT_ID.toString()))
                .andExpect(jsonPath("$.name").value(VendorTestFactory.DEFAULT_NAME));
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se CNPJ for invalido")
    void shouldReturn400WhenCnpjIsInvalid() throws Exception {
        CreateVendorRequest request = new CreateVendorRequest("Restaurante Teste", "cnpj-invalido-123");

        mockMvc.perform(post("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        
        verify(vendorService, never()).create(any());
    }

    @Test
    @DisplayName("Deve retornar 409 Conflict")
    void shouldReturn409WhenCnpjExists() throws Exception {
        CreateVendorRequest request = VendorTestFactory.createValidRequest();
        when(vendorMapper.toEntity(any())).thenReturn(VendorTestFactory.createValidVendor());
        when(vendorService.create(any())).thenThrow(new DuplicateVendorException("CNPJ já cadastrado"));

        mockMvc.perform(post("/api/v1/vendors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Deve retornar 200 OK ao buscar Vendor existente")
    void shouldReturn200WhenFindByIdIsSuccessful() throws Exception {
        Vendor vendorMock = VendorTestFactory.createValidVendor();
        VendorResponse responseMock = VendorTestFactory.createValidResponse();

        when(vendorService.findById(VendorTestFactory.DEFAULT_ID)).thenReturn(vendorMock);
        when(vendorMapper.toResponse(vendorMock)).thenReturn(responseMock);

        mockMvc.perform(get("/api/v1/vendors/{id}", VendorTestFactory.DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cnpj").value(VendorTestFactory.DEFAULT_CNPJ));
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found se Vendor não existir")
    void shouldReturn404WhenVendorDoesNotExist() throws Exception {
        UUID randomId = UUID.randomUUID();
        when(vendorService.findById(randomId)).thenThrow(new VendorNotFoundException("Não encontrado"));

        mockMvc.perform(get("/api/v1/vendors/{id}", randomId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar 204 No Content ao inativar Vendor com sucesso")
    void shouldReturn204WhenDeactivateIsSuccessful() throws Exception {
        doNothing().when(vendorService).deactivate(VendorTestFactory.DEFAULT_ID);

        mockMvc.perform(patch("/api/v1/vendors/{id}/deactivate", VendorTestFactory.DEFAULT_ID))
                .andExpect(status().isNoContent());
        
        verify(vendorService, times(1)).deactivate(VendorTestFactory.DEFAULT_ID);
    }
}