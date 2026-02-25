package com.rail.asset.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rail.asset.domain.Asset;
import com.rail.asset.service.AssetService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(controllers = AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
class AssetControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private org.springframework.test.web.servlet.MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @Test
    void createAssetReturns201() throws Exception {
        Asset asset = assetWithId(10L);
        given(assetService.create(any(Asset.class))).willReturn(asset);

        mockMvc.perform(post("/api/assets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetWithoutId())))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", containsString("/api/assets/10")))
            .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void updateAssetNotFoundReturns404() throws Exception {
        given(assetService.update(eq(50L), any(Asset.class))).willThrow(new RuntimeException("not found"));

        mockMvc.perform(put("/api/assets/50")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(assetWithoutId())))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteAssetReturns204() throws Exception {
        mockMvc.perform(delete("/api/assets/5"))
            .andExpect(status().isNoContent());
    }

    @Test
    void listAssetsWithCategoryAndStatusDelegates() throws Exception {
        mockMvc.perform(get("/api/assets")
                .param("category", "TRACK")
                .param("status", "ACTIVE"))
            .andExpect(status().isOk());
    }

    @Test
    void getAssetByCodeHandlesMissing() throws Exception {
        doThrow(new RuntimeException("missing")).when(assetService).findByAssetCode("RAM-999");

        mockMvc.perform(get("/api/assets/code/RAM-999"))
            .andExpect(status().isNotFound());
    }

    private Asset assetWithoutId() {
        Asset asset = assetWithId(null);
        asset.setId(null);
        return asset;
    }

    private Asset assetWithId(Long id) {
        return new Asset(
            id,
            "RAM-200",
            "Signal",
            "SIGNALING",
            null,
            "Acme",
            "SIG-1",
            null,
            LocalDate.of(2021, 1, 1),
            "ACTIVE",
            null,
            60,
            LocalDate.of(2024, 2, 1),
            new BigDecimal("1.50"),
            new BigDecimal("50000.00"),
            "notes"
        );
    }
}
