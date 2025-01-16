package com.sketch2fashion.backend.document;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sketch2fashion.backend.controller.ClothesController;
import com.sketch2fashion.backend.controller.GalleryController;
import com.sketch2fashion.backend.service.ClothesService;
import com.sketch2fashion.backend.service.GalleryService;
import com.sketch2fashion.backend.service.MessageService;
import com.sketch2fashion.backend.service.ResultService;
import com.sketch2fashion.backend.support.FileUploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        GalleryController.class,
        ClothesController.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

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
}
