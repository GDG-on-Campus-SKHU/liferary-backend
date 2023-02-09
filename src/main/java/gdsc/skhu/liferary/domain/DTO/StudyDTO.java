package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StudyDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @Schema(description = "Main post Id", defaultValue = "1")
        private Long mainPostId;
        @Schema(description = "Title", defaultValue = "Test study Title")
        private String title;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String author;
        @Schema(description = "Context", defaultValue = "Test study Context")
        private String context;
    }

    @Getter
    public static class Response {
        @Schema(description = "Title")
        private String title;
        @Schema(description = "Nickname")
        private String nickname;
        @Schema(description = "Context")
        private String context;
        public Response(Study study) {
            this.title = study.getTitle();
            this.nickname = study.getAuthor().getNickname();
            this.context = study.getContext();
        }
    }
}
