package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentDTO.Request")
    public static class Request {
        @Schema(description = "Board post Id", defaultValue = "1")
        private Long boardPostId;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String writer;
        @Schema(description = "Context", defaultValue = "Test comment context")
        private String context;
    }

    @Getter
    @Schema(name = "CommentDTO.Response")
    public static class Response {
        @Schema(description = "Username(email)")
        private String writer;
        @Schema(description = "Context")
        private String context;

        public Response(Comment comment) {
            this.writer = comment.getWriter().getNickname();
            this.context = comment.getContext();
        }
    }
}
