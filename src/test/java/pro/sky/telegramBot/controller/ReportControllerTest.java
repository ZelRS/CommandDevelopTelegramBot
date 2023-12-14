package pro.sky.telegramBot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import pro.sky.telegramBot.model.adoption.Report;
import pro.sky.telegramBot.service.ReportService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReportService reportService;
    @Value("${TST_IMAGE}")
    String pathToTestImage;

    @Test
    void getReportPhotoTest() throws Exception {
        //Подготовка данных для теста
        Report report = new Report();
        report.setId(1L);
        InputStream inputStream = getClass().getResourceAsStream(pathToTestImage);
        assert inputStream != null;
        report.setData(inputStream.readAllBytes());
        inputStream.close();
        ByteArrayResource byteArrayResource = new ByteArrayResource(report.getData());
        Report reportWithEmptyData = new Report();
        reportWithEmptyData.setId(2L);

        // Подготовка моков
        when(reportService.getReportById(1L)).thenReturn(report);
        when(reportService.getReportById(2L)).thenReturn(reportWithEmptyData);
        when(reportService.getReportById(3L)).thenReturn(null);

        // Проверка метода
        mockMvc.perform(get("/adoption/record/report/{id}/photo", 1))
                .andExpect(status().isOk())
                .andExpect(content().bytes(report.getData()))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"pet.jpg\""));
        mockMvc.perform(get("/adoption/record/report/{id}/photo", 2))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/adoption/record/report/{id}/photo", 3))
                .andExpect(status().isNotFound());
    }
}
