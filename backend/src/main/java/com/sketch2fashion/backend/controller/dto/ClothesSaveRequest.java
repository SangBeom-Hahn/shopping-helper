package com.sketch2fashion.backend.controller.dto;

import com.sketch2fashion.backend.utils.validator.ValidImageExists;
import com.sketch2fashion.backend.utils.validator.ValidImageExtension;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClothesSaveRequest {

    @ValidImageExtension
    @ValidImageExists
    private MultipartFile image;
}
