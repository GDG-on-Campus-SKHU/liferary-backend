package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.BoardPost;
import gdsc.skhu.liferary.domain.Category;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.MemberDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.boardpost.BoardPostRepository;
import gdsc.skhu.liferary.repository.mainpost.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BoardPostServiceTest {
    @InjectMocks
    private BoardPostService boardPostService;
    @Mock
    private BoardPostRepository boardPostRepository;
    @Mock
    private MainPostRepository mainPostRepository;
    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    public void setup() {
        Member member = Member.ofUser(new MemberDTO.Join("testuser@gmail.com", "testuser", "@Test1234", "@Test1234", false));
        MainPost mainPost = MainPost.builder()
                .id(1L)
                .title("Hello Liferary Main")
                .author(member)
                .context("This is context of main post")
                .category(Category.PROGRAMMING)
                .images(new ArrayList<>())
                .video("https://www.youtube.com/")
                .build();

        Mockito.when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        Mockito.when(mainPostRepository.findById(any())).thenReturn(Optional.of(mainPost));
    }

    @Test
    @DisplayName("Board post save logic")
    void save() throws IOException {
        //given
        Member member = memberRepository.findByEmail("testuser@gmail.comn").orElseThrow();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        BoardPostDTO.Request request = BoardPostDTO.Request.builder()
                .mainPostId(mainPostRepository.findById(1L).get().getId())
                .title("Hello Liferary Board")
                .context("This is context")
                .images(new ArrayList<>())
                .build();

        //when
        BoardPostDTO.Response boardPost = boardPostService.save(principal, request);

        //then
        Assertions.assertThat(boardPost.getAuthor()).isEqualTo("testuser@gmail.com");
    }
}