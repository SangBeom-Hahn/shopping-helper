package com.sketch2fashion.backend.acceptance;

import com.sketch2fashion.backend.controller.dto.ClothesSaveRequest;
import com.sketch2fashion.backend.controller.dto.ClothesSharedRequest;
import com.sketch2fashion.backend.controller.dto.ClothesUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class AcceptanceFixture {

    private static ExtractableResponse<Response> get(String url) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all().extract();
    }

    private static ExtractableResponse<Response> put(String url, Object body) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .put(url)
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 업로드(ClothesSaveRequest clothesSaveRequest) throws IOException {
        MultipartFile image = clothesSaveRequest.getImage();

        return RestAssured
                .given()
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                .multiPart("image",
                        image.getOriginalFilename(),
                        image.getInputStream(),
                        image.getContentType()
                )
                .param("objectType", clothesSaveRequest.getObjectType())
                .param("refine", clothesSaveRequest.getRefine())
                .when().post("/api/clothes/upload")
                .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 추론_결과_조회(Long messageId) {
        return get("/api/clothes/" + messageId);
    }

    public static ExtractableResponse<Response> 평점_부여(Long messageId, ClothesUpdateRequest clothesUpdateRequest) {
        return put("/api/clothes/" + messageId, clothesUpdateRequest);
    }

    public static ExtractableResponse<Response> 추론_결과_공유(Long messageId, ClothesSharedRequest clothesSharedRequest) {
        return put("/api/clothes/" + messageId + "/share", clothesSharedRequest);
    }

    public static ExtractableResponse<Response> 갤러리_조회() {
        return get("/api/gallery");
    }

    public static ExtractableResponse<Response> 검색_결과_조회(Long resultId) {
        return get("/api/gallery/" + resultId);
    }

    public static ExtractableResponse<Response> 검색_결과_저장(Long messageId) {
        return get("/api/home/" + messageId);
    }

    public static ClothesSaveRequest createClothesSaveRequest() throws IOException {
        String originalFileName = "test.jpeg";
        InputStream fileInputStream = new FileInputStream("src/test/resources/" + originalFileName);
        MockMultipartFile image = new MockMultipartFile(
                "image",
                originalFileName,
                "image/jpeg",
                fileInputStream
        );

        return new ClothesSaveRequest("HAT", false, image);
    }
}
