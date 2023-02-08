package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.MainPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainPostDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MainPostDTO.Request")
    public static class Request {
        @Schema(description = "Main post title", defaultValue = "Test Title")
        private String title;
        @Schema(description = "Username(email)", defaultValue = "testuser@gmail.com")
        private String author;
        @Schema(description = "Category", defaultValue = "programming")
        private String category;
        @Schema(description = "Main post context", defaultValue = "Test Context")
        private String context;
        @Schema(description = "Video url", defaultValue = "https://www.youtube.com")
        private String video;
    }

    @Getter
    @Schema(name = "MainPostDTO.Response")
    public static class Response {
        private String title;
        private String nickname;
        private String category;
        private String context;

        public Response(MainPost mainPost) {
            this.title = mainPost.getTitle();
            this.nickname = mainPost.getAuthor().getNickname();
            this.category = mainPost.getCategory();
            this.context = mainPost.getContext();
        }
    }
}
