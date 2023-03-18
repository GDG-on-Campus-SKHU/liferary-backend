package gdsc.skhu.liferary.repository.mainpost;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import gdsc.skhu.liferary.domain.DTO.QMainPostDTO_Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static gdsc.skhu.liferary.domain.QMainPost.mainPost;

@Repository
@RequiredArgsConstructor
public class MainPostCustomRepositoryImpl implements MainPostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MainPostDTO.Response> findByKeyword(Pageable pageable, String keyword) {
        List<MainPostDTO.Response> searchResult = jpaQueryFactory
                .select(new QMainPostDTO_Response(mainPost))
                .from(mainPost)
                .where(mainPost.title.containsIgnoreCase(keyword)
                        .or(mainPost.context.containsIgnoreCase(keyword))
                )
                .orderBy(
                        mainPost.id.desc()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(searchResult, pageable, searchResult.size());
    }
}
