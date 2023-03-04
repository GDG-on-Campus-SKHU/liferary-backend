package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.Authority;
import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.BoardPostRepository;
import gdsc.skhu.liferary.repository.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class BoardPostServiceTest {
    @Autowired
    private BoardPostService boardPostService;

    @Autowired
    private BoardPostRepository boardPostRepository;

    @Autowired
    private MainPostRepository mainPostRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("Board post save logic")
    void save() throws IOException {
        //given
        Member member = Member.ofUser(new MemberDTO.Join("testuser@gmail.com", "testuser", "@Test1234", "@Test1234", false));
        memberRepository.save(member);

        MainPost mainPost = new MainPost(1L, "Hello Liferary Main", member, "programming", "This is context of main post", new ArrayList<>(), "Video URL");
        mainPostRepository.save(mainPost);

        BoardPostDTO.Request request = BoardPostDTO.Request.builder()
                .mainPostId(1L)
                .title("Hello Liferary Board")
                .context("This is context")
                .build();

        //when
        boardPostService.save(request);
        BoardPost boardPost = boardPostRepository.findById(1L).get();

        //then
        Assertions.assertThat(boardPost).isNotNull();
    }

    @Test
    void findByMainPost() {
        //given

        //when

        //then
    }
}