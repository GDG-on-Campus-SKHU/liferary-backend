package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.service.MainPostService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Tag(name = "MainPost", description = "API for main board post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainPostController {
    private final MainPostService mainPostService;

    // Create
    @Operation(summary = "create main posts", description = "Create posts for main board")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MainPostDTO.Response> save(Principal principal, @ModelAttribute MainPostDTO.Request request) throws IOException {
        return ResponseEntity.ok(mainPostService.save(principal, request));
    }

    // Read
    @Operation(summary = "find main post by id", description = "Read main post from database by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/post")
    public ResponseEntity<MainPostDTO.Response> findById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(mainPostService.findById(id));
    }

    @Operation(summary = "find main post by category", description = "Read main posts from database by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/category/{category}/page")
    public Page<MainPostDTO.Response> findByCategory(@PathVariable("category") String category,
                                                      @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return mainPostService.findByCategory(pageable, category);
    }

    @Operation(summary = "find main post by keyword", description = "Read main posts from database by keyword in title or context")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/keyword/{keyword}/page")
    public Page<MainPostDTO.Response> findByKeyword(@PathVariable("keyword") String keyword,
                                                    @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return mainPostService.findByKeyword(pageable, keyword);
    }

    @Operation(summary = "find main posts", description = "Read main posts from database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/all")
    public Page<MainPostDTO.Response> findAll(@RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return mainPostService.findAll(pageable);
    }

    // Update
    @Operation(summary = "Update main post", description = "Update main post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MainPostDTO.Response> update(Principal principal,
                                                       @ModelAttribute MainPostDTO.Update update,
                                                       @RequestParam("id") Long id) throws IOException {
        return ResponseEntity.ok(mainPostService.update(principal, update, id));
    }

    // Delete
    @Operation(summary = "Delete main post", description = "Delete main post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/post")
    public ResponseEntity<String> delete(@RequestParam("id") Long id) {
        return mainPostService.delete(id);
    }
}
