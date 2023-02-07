package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.BoardPost;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class BoardPostDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long mainPostId;
        private String title;
        private String author;
        private String context;
    }

    @Getter
    public static class Response {
        private String title;
        private String nickname;
        private String context;
        private List<CommentDTO.Response> comments;

        public Response(BoardPost boardPost) {
            this.title = boardPost.getTitle();
            this.nickname = boardPost.getAuthor().getNickname();
            this.context = boardPost.getContext();
            this.comments = boardPost.getComments().stream().map(CommentDTO.Response::new).collect(Collectors.toList());
        }
    }
}
