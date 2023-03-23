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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public ResponseEntity<BoardPostDTO.Response> save(@AuthenticationPrincipal UserDetails userDetails,
                                                      @ModelAttribute BoardPostDTO.Request boardPostDTO) throws IOException {
        return ResponseEntity.ok(boardPostService.save(userDetails.getUsername(), boardPostDTO));
    }

    // Read
    @Operation(summary = "get board posts by main post id", description = "Read board posts by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/page")
    public Page<BoardPostDTO.Response> findByMainPost(@PathVariable("mainPostId") Long mainPostId,
                                                      @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findByMainPost(pageable, mainPostId);
    }

    @Operation(summary = "get one board post by main post id", description = "Read one board post by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/post")
    public BoardPostDTO.Response findById(@PathVariable("mainPostId") Long mainPostId,
                                          @RequestParam("id") Long id) {
        return boardPostService.findById(mainPostId, id);
    }

    @Operation(summary = "get one board post by member email", description = "Read one board post by member email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/member/page")
    public Page<BoardPostDTO.Response> findByMember(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findByMember(pageable, userDetails.getUsername());
    }

    @Operation(summary = "get board posts by main post and keyword", description = "Read board posts by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}/{keyword}/page")
    public Page<BoardPostDTO.Response> findByMainPostAndKeyword(@PathVariable("mainPostId") Long mainPostId,
                                                                @PathVariable("keyword") String keyword,
                                                                @RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findByMainPostAndKeyword(pageable, mainPostId, keyword);
    }

    @Operation(summary = "find all board posts", description = "Read all board posts from database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/all")
    public Page<BoardPostDTO.Response> findAll(@RequestParam("page") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 9, Sort.by("id").descending());
        return boardPostService.findAll(pageable);
    }

    // Update
    @Operation(summary = "Update board post", description = "Update board post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping(value = "/{mainPostId}/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardPostDTO.Response> update(@AuthenticationPrincipal UserDetails userDetails,
                                                        @ModelAttribute BoardPostDTO.Update update,
                                                        @PathVariable(name = "mainPostId") Long mainPostId,
                                                        @RequestParam(name = "id") Long id) throws IOException {
        return ResponseEntity.ok(boardPostService.update(userDetails.getUsername(), update, mainPostId, id));
    }

    // Delete
    @Operation(summary = "Delete main post", description = "Delete main post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{mainPostId}/post")
    public ResponseEntity<String> delete(@PathVariable("mainPostId") Long mainPostId,
                                         @RequestParam("id") Long id) {
        return boardPostService.delete(mainPostId, id);
    }
}
