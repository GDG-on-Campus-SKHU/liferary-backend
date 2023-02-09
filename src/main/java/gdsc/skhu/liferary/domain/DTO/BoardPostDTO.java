package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.BoardPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardPostDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "BoardPostDTO.Request")
    public static class Request {
        @Schema(description = "Main post Id", defaultValue = "1")
        private Long mainPostId;
        @Schema(description = "Title", defaultValue = "Test Board Title")
        private String title;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String author;
        @Schema(description = "Context", defaultValue = "Test Board Context")
        private String context;
    }

    @Getter
    @Setter
    @Schema(name = "BoardPostDTO.Response")
    public static class Response {
        @Schema(description = "Title")
        private String title;
        @Schema(description = "Nickname")
        private String nickname;
        @Schema(description = "Context")
        private String context;
        @Schema(description = "Comments")
        private List<CommentDTO.Response> comments;

        public Response(BoardPost boardPost) {
            this.title = boardPost.getTitle();
            this.nickname = boardPost.getAuthor().getNickname();
            this.context = boardPost.getContext();
            if(boardPost.getComments() == null) {
                this.comments = new ArrayList<>();
            } else {
                this.comments = boardPost.getComments().stream().map(CommentDTO.Response::new).collect(Collectors.toList());
            }
        }
    }
}
