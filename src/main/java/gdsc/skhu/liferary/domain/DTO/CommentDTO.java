package gdsc.skhu.liferary.domain.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import gdsc.skhu.liferary.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentDTO.Request")
    public static class Request {
        @Schema(description = "Board post Id", defaultValue = "1")
        private Long boardPostId;
        @Schema(description = "Context", defaultValue = "Test comment context")
        private String context;
    }

    @Getter
    @Schema(name = "CommentDTO.Response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        @Schema(description = "Comment Id")
        private Long id;
        @Schema(description = "Email")
        private String author;
        @Schema(description = "Origin writer nickname")
        private String writer;
        @Schema(description = "Context")
        private String context;
        @Schema(description = "Child comments")
        private List<Response> childComments;
        @Schema(description = "Modified Date")
        private LocalDateTime modifiedDate;

        public Response(Comment comment) {
            this.id = comment.getId();
            this.author = comment.getWriter().getEmail();
            this.writer = comment.getWriter().getNickname();
            this.context = comment.getContext();
            this.childComments = comment.getChildComments().stream()
                    .map(CommentDTO.Response::new).collect(Collectors.toList());
            this.modifiedDate = comment.getModifiedDate();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentDTO.Reply")
    public static class Reply {
        @Schema(description = "Board post Id", defaultValue = "1")
        private Long boardPostId;
        @Schema(description = "Parent comment Id", defaultValue = "1")
        private Long parentCommentId;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String writer;
        @Schema(description = "Context", defaultValue = "Test comment context")
        private String context;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "CommentDTO.Update")
    public static class Update {
        @Schema(description = "Context", defaultValue = "Test comment context")
        private String context;
    }
}
