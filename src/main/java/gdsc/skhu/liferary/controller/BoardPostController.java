package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.service.BoardPostService;
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

@Tag(name = "BoardPost", description = "API for community board post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardPostController {
    private final BoardPostService boardPostService;

    // Create
    @Operation(summary = "create board posts", description = "Create posts for community board")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardPostDTO.Response> save(Principal principal, @ModelAttribute BoardPostDTO.Request boardPostDTO) throws IOException {
        return ResponseEntity.ok(boardPostService.save(principal, boardPostDTO));
    }

    // Read
    @Operation(summary = "get board posts by main post id", description = "Read board posts by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/page/{pageNumber}")
    public Page<BoardPostDTO.Response> findByMainPost(@PathVariable("mainPostId") Long mainPostId,
                                                      @PathVariable("pageNumber") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findByMainPost(pageable, mainPostId);
    }

    @Operation(summary = "get one board post by main post id", description = "Read one board post by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/post/{id}")
    public BoardPostDTO.Response findById(@PathVariable("mainPostId") Long mainPostId,
                                          @PathVariable("id") Long id) {
        return boardPostService.findById(mainPostId, id);
    }

    @Operation(summary = "get board posts by main post and keyword", description = "Read board posts by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/{keyword}/page/{pageNumber}")
    public Page<BoardPostDTO.Response> findByMainPostAndKeyword(@PathVariable("mainPostId") Long mainPostId,
                                                      @PathVariable("keyword") String keyword,
                                                      @PathVariable("pageNumber") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findByMainPostAndKeyword(pageable, mainPostId, keyword);
    }

    // Update
    @Operation(summary = "Update board post", description = "Update board post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/{mainPostId}/post/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardPostDTO.Response> update(Principal principal,
                                                        @ModelAttribute BoardPostDTO.Update update,
                                                        @PathVariable(name = "mainPostId") Long mainPostId,
                                                        @PathVariable(name = "id") Long id) throws IOException {
        return ResponseEntity.ok(boardPostService.update(principal, update, mainPostId, id));
    }

    // Delete
    @Operation(summary = "Delete main post", description = "Delete main post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{mainPostId}/post/{id}")
    public ResponseEntity<String> delete(@PathVariable("mainPostId") Long mainPostId,
                                         @PathVariable("id") Long id) {
        return boardPostService.delete(mainPostId, id);
    }
}
