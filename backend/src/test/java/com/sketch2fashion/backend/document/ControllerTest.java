package com.sketch2fashion.backend.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.config.RestDocsConfig;
import com.sketch2fashion.backend.controller.ClothesController;
import com.sketch2fashion.backend.controller.GalleryController;
import com.sketch2fashion.backend.controller.history.HistoryInterceptor;
import com.sketch2fashion.backend.repository.history.CommonHistoryRepository;
import com.sketch2fashion.backend.repository.history.TransactionHistoryRepository;
import com.sketch2fashion.backend.service.ClothesService;
import com.sketch2fashion.backend.service.GalleryService;
import com.sketch2fashion.backend.service.MessageService;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.support.SlackAlarmGenerator;
import com.sketch2fashion.backend.support.upload.FileUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = {
        GalleryController.class,
        ClothesController.class
})
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class ControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected TransactionHistoryRepository transactionHistoryRepository;

    @MockBean
    protected CommonHistoryRepository commonHistoryRepository;

    @MockBean
    protected GalleryService galleryService;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected ClothesService clothesService;

    @MockBean
    protected MessageService messageService;

    @MockBean
    protected ResultService resultService;

    @MockBean
    protected FileUploader fileUploader;

    @MockBean
    protected SlackAlarmGenerator slackAlarmGenerator;

    @MockBean
    protected HistoryInterceptor historyInterceptor;

    @BeforeEach
    void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(restDocs)
                .build();
    }
}
