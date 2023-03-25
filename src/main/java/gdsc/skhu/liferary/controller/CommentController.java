package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.CommentDTO;
import gdsc.skhu.liferary.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "API for comment")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    // Create
    @Operation(summary = "create comment", description = "Create comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/new")
    public ResponseEntity<CommentDTO.Response> save(@AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody CommentDTO.Request request) {
        return ResponseEntity.ok(commentService.save(userDetails.getUsername(), request));
    }

    @Operation(summary = "reply comment", description = "Create reply comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PostMapping("/reply")
    public ResponseEntity<CommentDTO.Response> reply(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody CommentDTO.Reply reply) {
        return ResponseEntity.ok(commentService.reply(userDetails.getUsername(), reply));
    }

    // Read
    @Operation(summary = "get comments by board post id", description = "Read comments by board post id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @GetMapping("/{boardPostId}/page/{pageNumber}")
    public List<CommentDTO.Response> findByBoardPost(@PathVariable("boardPostId") Long boardPostId,
                                                     @PathVariable("pageNumber") Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber == 0 ? 0 : pageNumber-1, 20, Sort.by("id").descending());
        return commentService.findByBoardPost(pageable, boardPostId);
    }

    // Update
    @Operation(summary = "Update comment", description = "Update comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @PatchMapping("/{boardPostId}/comment/{id}")
    public ResponseEntity<CommentDTO.Response> update(@AuthenticationPrincipal UserDetails userDetails,
                                                      @RequestBody CommentDTO.Update update,
                                                      @PathVariable("boardPostId") Long mainPostId,
                                                      @PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.update(userDetails.getUsername(), update, mainPostId, id));
    }

    // Delete
    @Operation(summary = "Delete comment", description = "Delete comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        return commentService.delete(id);
    }
}