package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.Study;
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
        private Long mainPostId;
        private String author;
        private String title;
        private String context;
    }

    @Getter
    public static class Response {
        private String nickname;
        private String title;
        private String context;
        public Response(Study study) {
            this.nickname = study.getAuthor().getNickname();
            this.title = study.getTitle();
            this.context = study.getContext();
        }
    }
}
