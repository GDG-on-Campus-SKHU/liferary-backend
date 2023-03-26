package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.ImageDTO;
import gdsc.skhu.liferary.service.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "Image", description = "API for images")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;

    // Create
    @Operation(summary = "create images", description = "Create images with Firebase Storage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageDTO.Result> save(@RequestParam("path") String path,
                                                @RequestPart("images") List<MultipartFile> images) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(path + "/", images));
    }

    @Operation(summary = "Delete image", description = "Delete image with internal path and Firebase Storage path")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("path") String path,
                                         @RequestBody ImageDTO.Delete request) {
        return imageService.deleteImage(path + "/", request.getImagePath());
    }
}
