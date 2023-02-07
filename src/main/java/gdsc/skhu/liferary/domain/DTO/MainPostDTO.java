package gdsc.skhu.liferary.domain.DTO;

import gdsc.skhu.liferary.domain.MainPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MainPostDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String title;
        private String author;
        private String context;
        private String video;
    }

    @Getter
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
