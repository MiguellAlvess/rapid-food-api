package br.com.db.rapid_food_api.product.controller;

import br.com.db.rapid_food_api.product.dto.ProductRequestDto;
import br.com.db.rapid_food_api.product.dto.ProductResponseDto;
import br.com.db.rapid_food_api.product.dto.ProductUpdateDto;
import br.com.db.rapid_food_api.product.factory.ProductFactoryHelper;
import br.com.db.rapid_food_api.product.mapper.ProductMapper;
import br.com.db.rapid_food_api.product.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Test
    @DisplayName("Should create an product with valid data")
    void createProductSuccess() throws Exception {
        ProductRequestDto request = ProductFactoryHelper.PRODUCT_REQUEST;
        ProductResponseDto responseMock = ProductFactoryHelper.createValidProductResponse();

        when(productService.createProduct(any())).thenReturn(responseMock);

        mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.vendorName").value(ProductFactoryHelper.VENDOR_NAME))
               .andExpect(jsonPath("$.productName").value(ProductFactoryHelper.PRODUCT_NAME))
               .andExpect(jsonPath("$.price").value(ProductFactoryHelper.PRICE))
               .andDo(print());
    }

    @Test
    @DisplayName("Should not create an product with invalid vendor ID")
    void notCreateProduct() throws Exception {
        ProductRequestDto request = ProductFactoryHelper.INVALID_PRODUCT_REQUEST;

        mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    @DisplayName("Should get all products from a vendor with success")
    void getAllProductsSuccess() throws Exception {
        UUID vendorId = ProductFactoryHelper.VENDOR_ID;
        List<ProductResponseDto> responseMock = List.of(ProductFactoryHelper.createValidProductResponse());

        when(productService.getAllProducts(vendorId)).thenReturn(responseMock);

        mockMvc.perform(get("/api/products/"+ vendorId)
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].vendorName").value(ProductFactoryHelper.VENDOR_NAME))
               .andExpect(jsonPath("$[0].productName").value(ProductFactoryHelper.PRODUCT_NAME))
               .andExpect(jsonPath("$[0].price").value(ProductFactoryHelper.PRICE))
               .andDo(print());
    }

    @Test
    @DisplayName("Should update an product with valid data")
    void updateProductSuccess() throws Exception {
        UUID vendorId = ProductFactoryHelper.VENDOR_ID;
        ProductUpdateDto updateDto = ProductFactoryHelper.PRODUCT_UPDATE_REQUEST;
        ProductResponseDto responseMock = ProductFactoryHelper.createValidProductResponse();

        when(productService.updateProduct(any(ProductUpdateDto.class),
                                          eq(vendorId))).thenReturn(responseMock);

        mockMvc.perform(patch("/api/products/{vendorId}", vendorId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.vendorName").value(ProductFactoryHelper.VENDOR_NAME))
               .andExpect(jsonPath("$.productName").value(ProductFactoryHelper.PRODUCT_NAME))
               .andExpect(jsonPath("$.price").value(ProductFactoryHelper.PRICE))
               .andDo(print());
    }

    @Test
    @DisplayName("Should not update an product with invalid data")
    void notUpdateProduct() throws Exception {
        UUID vendorId = ProductFactoryHelper.VENDOR_ID;
        ProductUpdateDto invalidUpdateDto = ProductFactoryHelper.INVALID_PRODUCT_UPDATE_REQUEST;

        mockMvc.perform(patch("/api/products/}" + vendorId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidUpdateDto)))
               .andExpect(status().isBadRequest())
               .andDo(print());
    }

    @Test
    @DisplayName("Should delete an product with success")
    void deleteProductSuccess() throws Exception {
        UUID vendorId = ProductFactoryHelper.VENDOR_ID;
        UUID productId = ProductFactoryHelper.PRODUCT_ID;

        doNothing().when(productService).deleteProduct(vendorId, productId);

        mockMvc.perform(delete("/api/products/"+vendorId+"/"+productId)
                            .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent())
               .andDo(print());
    }

}
