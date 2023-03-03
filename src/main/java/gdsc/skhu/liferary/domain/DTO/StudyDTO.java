package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Study;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class StudyDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "StudyDTO.Request")
    public static class Request {
        @Schema(description = "Main post Id", defaultValue = "1")
        private Long mainPostId;
        @Schema(description = "Title", defaultValue = "Test study Title")
        private String title;
        @Schema(description = "Context", defaultValue = "Test study Context")
        private String context;
    }

    @Getter
    @Schema(name = "StudyDTO.Response")
    public static class Response {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "Title")
        private String title;
        @Schema(description = "Email")
        private String author;
        @Schema(description = "Nickname")
        private String nickname;
        @Schema(description = "Context")
        private String context;
        @Schema(description = "Modified Date")
        private LocalDateTime modifiedDate;
        public Response(Study study) {
            this.id = study.getId();
            this.title = study.getTitle();
            this.author = study.getAuthor().getEmail();
            this.nickname = study.getAuthor().getNickname();
            this.context = study.getContext();
            this.modifiedDate = study.getModifiedDate();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "StudyDTO.Update")
    public static class Update {
        @Schema(description = "Title", defaultValue = "Modified Title")
        private String title;
        @Schema(description = "Context", defaultValue = "Modified Context")
        private String context;
    }
}
