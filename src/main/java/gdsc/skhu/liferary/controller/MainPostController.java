package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/new")
    public ResponseEntity<MainPostDTO.Response> save(@RequestBody MainPostDTO.Request request) {
        return ResponseEntity.ok(mainPostService.save(request));
    }

    // Read
    @Operation(summary = "find main post by id", description = "Read main post from database by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MainPostDTO.Response> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(mainPostService.findById(id));
    }

    @Operation(summary = "find main post by category", description = "Read main posts from database by category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{category}/{pageNumber}")
    public Page<MainPostDTO.Response> findByCategory(@PathVariable("category") String category,
                                                      @PathVariable("pageNumber") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return mainPostService.findByCategory(pageable, category);
    }

    @Operation(summary = "find main posts", description = "Read main posts from database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{pageNumber}")
    public Page<MainPostDTO.Response> findAll(@PathVariable("pageNumber") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return mainPostService.findAll(pageable);
    }

    // Update
    @Operation(summary = "Update main post", description = "Update main post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MainPostDTO.Response> update(@RequestBody MainPostDTO.Request request,
                                                       @PathVariable("id") Long id) {
        return mainPostService.update(id);
    }
}
