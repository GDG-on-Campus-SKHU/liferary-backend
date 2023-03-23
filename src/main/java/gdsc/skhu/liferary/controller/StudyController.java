package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.StudyDTO;
import gdsc.skhu.liferary.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "Study", description = "API for study")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/study")
public class StudyController {
    private final StudyService studyService;

    // Create
    @Operation(summary = "create study", description = "Create study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudyDTO.Response> save(@AuthenticationPrincipal UserDetails userDetails,
                                                  @ModelAttribute StudyDTO.Request request) throws IOException {
        return ResponseEntity.ok(studyService.save(userDetails.getUsername(), request));
    }

    // Read
    @Operation(summary = "get study by main post id", description = "Read study by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/all")
    public Page<StudyDTO.Response> findAll(@RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return studyService.findAll(pageable);
    }

    @Operation(summary = "get study by main post id", description = "Read study by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping
    public StudyDTO.Response findByMainPost(@RequestParam("mainPost") Long mainPostId) {
        return studyService.findByMainPost(mainPostId);
    }

    @Operation(summary = "get study by main post id", description = "Read study by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/member")
    public Page<StudyDTO.Response> findByMember(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return studyService.findByMember(pageable, userDetails.getUsername());
    }

    // Update
    @Operation(summary = "Update study", description = "Update study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudyDTO.Response> update(@AuthenticationPrincipal UserDetails userDetails,
                                                    @ModelAttribute StudyDTO.Update update,
                                                    @RequestParam("mainPost") Long mainPostId) throws IOException {
        return ResponseEntity.ok(studyService.update(userDetails.getUsername(), update, mainPostId));
    }

    // Delete
    @Operation(summary = "Delete study", description = "Delete study")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("mainPost") Long mainPostId) {
        return studyService.delete(mainPostId);
    }
}
