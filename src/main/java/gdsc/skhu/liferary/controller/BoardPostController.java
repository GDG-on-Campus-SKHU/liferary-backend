package gdsc.skhu.liferary.controller;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.service.BoardPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardPostController {
    private final BoardPostService boardPostService;

    @PostMapping("/new")
    public ResponseEntity<String> save(BoardPostDTO.Request boardPostDTO) {
        boardPostService.save(boardPostDTO);
        return ResponseEntity.ok("Post upload success");
    }

    @GetMapping("/{mainPostId}")
    public Page<BoardPostDTO.Response> findByMainPost(Pageable pageable,
                                                      @PathVariable("mainPostId") Long mainPostId) {
        return boardPostService.findByMainPost(pageable, mainPostId);
    }
}
