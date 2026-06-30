package br.com.db.rapid_food_api.vendor.service;

import br.com.db.rapid_food_api.vendor.common.VendorTestFactory;
import br.com.db.rapid_food_api.vendors.domain.Vendor;
import br.com.db.rapid_food_api.vendors.exceptions.DuplicateVendorException;
import br.com.db.rapid_food_api.vendors.exceptions.VendorNotFoundException;
import br.com.db.rapid_food_api.vendors.repository.VendorRepository;
import br.com.db.rapid_food_api.vendors.service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VendorServiceTest {

    @Mock
    private VendorRepository vendorRepository;

    @InjectMocks
    private VendorService vendorService;

    private Vendor validVendor;

    @BeforeEach
    void setUp() {
        validVendor = VendorTestFactory.createValidVendor();
    }

    @Test
    @DisplayName("Deve criar um Vendor com sucesso quando CNPJ for novo")
    void shouldCreateVendorSuccessfully() {
        when(vendorRepository.existsByCnpj(anyString())).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenReturn(validVendor);

        Vendor savedVendor = vendorService.create(validVendor);

        assertNotNull(savedVendor);
        assertEquals(VendorTestFactory.DEFAULT_CNPJ, savedVendor.getCnpj());
        verify(vendorRepository, times(1)).save(validVendor);
    }

    @Test
    @DisplayName("Deve lançar Exception quando CNPJ já existir")
    void shouldThrowExceptionWhenCnpjAlreadyExists() {
        when(vendorRepository.existsByCnpj(anyString())).thenReturn(true);

        assertThrows(DuplicateVendorException.class, () -> vendorService.create(validVendor));
        verify(vendorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve retornar o Vendor quando ID for encontrado")
    void shouldReturnVendorWhenIdIsFound() {
        when(vendorRepository.findById(VendorTestFactory.DEFAULT_ID)).thenReturn(Optional.of(validVendor));

        Vendor found = vendorService.findById(VendorTestFactory.DEFAULT_ID);

        assertNotNull(found);
        assertEquals(VendorTestFactory.DEFAULT_ID, found.getId());
    }

    @Test
    @DisplayName("Deve lançar VendorNotFoundException quando ID não existir")
    void shouldThrowExceptionWhenVendorIsAlreadyInactive() {
        UUID randomId = UUID.randomUUID();
        when(vendorRepository.findById(randomId)).thenReturn(Optional.empty());

        assertThrows(VendorNotFoundException.class, () -> vendorService.findById(randomId));
    }

    @Test
    @DisplayName("Deve inativar o Vendor com sucesso")
    void shouldDeactivateVendorWithSuccess() {
        when(vendorRepository.findById(VendorTestFactory.DEFAULT_ID)).thenReturn(Optional.of(validVendor));

        vendorService.deactivate(VendorTestFactory.DEFAULT_ID);

        assertFalse(validVendor.getActive());
        verify(vendorRepository, times(1)).save(validVendor);
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException ao inativar Vendor já inativo")
    void deactivate_ThrowsIllegalStateExceptionWhenAlreadyInactive() {
        validVendor.setActive(false); 
        when(vendorRepository.findById(VendorTestFactory.DEFAULT_ID)).thenReturn(Optional.of(validVendor));

        assertThrows(IllegalStateException.class, () -> vendorService.deactivate(VendorTestFactory.DEFAULT_ID));
        verify(vendorRepository, never()).save(any());
    }
}