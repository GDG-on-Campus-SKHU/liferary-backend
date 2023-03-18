package gdsc.skhu.liferary.repository.boardpost;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.DTO.QBoardPostDTO_Response;
import gdsc.skhu.liferary.domain.DTO.QMainPostDTO_Response;
import gdsc.skhu.liferary.domain.MainPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gdsc.skhu.liferary.domain.QBoardPost.boardPost;

@Repository
@RequiredArgsConstructor
public class BoardPostCustomRepositoryImpl implements BoardPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardPostDTO.Response> findByKeyword(Pageable pageable, MainPost mainPost, String keyword) {
        List<BoardPostDTO.Response> searchResult = jpaQueryFactory
                .select(new QBoardPostDTO_Response(boardPost))
                .from(boardPost)
                .where(boardPost.mainPost.eq(mainPost)
                        .and(boardPost.title.containsIgnoreCase(keyword)
                                .or(boardPost.context.containsIgnoreCase(keyword))
                        )
                )
                .orderBy(
                        boardPost.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(searchResult, pageable, searchResult.size());
    }
}
