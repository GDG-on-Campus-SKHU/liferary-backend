package gdsc.skhu.liferary.domain.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import gdsc.skhu.liferary.domain.MainPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainPostDTO {
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MainPostDTO.Request")
    public static class Request {
        @Schema(description = "Title", defaultValue = "Test Title")
        private String title;
        @Schema(description = "Category", defaultValue = "programming")
        private String category;
        @Schema(description = "Context", defaultValue = "Test Context")
        private String context;
        @Schema(description = "Image files", defaultValue = "")
        private List<MultipartFile> images;
        @Schema(description = "Video URL", defaultValue = "https://www.youtube.com")
        private String video;
    }

    @Getter
    @Schema(name = "MainPostDTO.Response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        @Schema(description = "ID")
        private Long id;
        @Schema(description = "Title")
        private String title;
        @Schema(description = "Email")
        private String author;
        @Schema(description = "Nickname")
        private String nickname;
        @Schema(description = "Category")
        private String category;
        @Schema(description = "Context")
        private String context;
        @Schema(description = "Image files", defaultValue = "")
        private List<String> images;
        @Schema(description = "Video URL")
        private String video;
        @Schema(description = "Modified Date")
        private LocalDateTime modifiedDate;

        @QueryProjection
        public Response(MainPost mainPost) {
            this.id = mainPost.getId();
            this.title = mainPost.getTitle();
            this.author = mainPost.getAuthor().getEmail();
            this.nickname = mainPost.getAuthor().getNickname();
            this.category = mainPost.getCategory().toString();
            this.context = mainPost.getContext();
            if(mainPost.getImages() == null) {
                this.images = new ArrayList<>();
            } else {
                this.images = mainPost.getImages();
            }
            this.video = mainPost.getVideo();
            this.modifiedDate = mainPost.getModifiedDate();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(name = "MainPostDTO.Update")
    public static class Update {
        @Schema(description = "Title", defaultValue = "Modified Title")
        private String title;
        @Schema(description = "Category", defaultValue = "fitness")
        private String category;
        @Schema(description = "Context", defaultValue = "Modified Context")
        private String context;
        @Schema(description = "Image files", defaultValue = "")
        private List<MultipartFile> images;
        @Schema(description = "Video URL", defaultValue = "https://www.youtube.com")
        private String video;
    }
}
