package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String writer;
        private Long boardPostId;
        private String context;
    }

    @Getter
    public static class Response {
        private String writer;
        private String context;

        public Response(Comment comment) {
            this.writer = comment.getWriter().getNickname();
            this.context = comment.getContext();
        }
    }
}
