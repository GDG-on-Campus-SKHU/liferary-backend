package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.service.BoardPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BoardPost", description = "API for community board post")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardPostController {
    private final BoardPostService boardPostService;

    @Operation(summary = "create board posts", description = "Create posts for community board")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/new")
    public ResponseEntity<BoardPostDTO.Response> save(@RequestBody BoardPostDTO.Request boardPostDTO) {
        return ResponseEntity.ok(boardPostService.save(boardPostDTO));
    }

    @Operation(summary = "get board posts by main post id", description = "Read board posts by main post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{mainPostId}")
    public Page<BoardPostDTO.Response> findByMainPost(Pageable pageable,
                                                      @PathVariable("mainPostId") Long mainPostId) {
        return boardPostService.findByMainPost(pageable, mainPostId);
    }
}
