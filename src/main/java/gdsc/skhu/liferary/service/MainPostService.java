package gdsc.skhu.liferary.service;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import gdsc.skhu.liferary.domain.Member;
import gdsc.skhu.liferary.repository.MainPostRepository;
import gdsc.skhu.liferary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPostService {
    private final MemberRepository memberRepository;
    private final MainPostRepository mainPostRepository;
    public MainPostDTO.Response save(MainPostDTO.Request request) {
        MainPost mainPost = MainPost.builder()
                .title(request.getTitle())
                .author(memberRepository.findByUsername(request.getAuthor()))
                .category(request.getCategory())
                .context(request.getContext())
                .video(request.getVideo())
                .build();
        return new MainPostDTO.Response(mainPostRepository.save(mainPost));
    }
}
