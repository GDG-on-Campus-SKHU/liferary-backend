package gdsc.skhu.liferary.repository.boardpost;

import gdsc.skhu.liferary.domain.DTO.BoardPostDTO;
import gdsc.skhu.liferary.domain.MainPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardPostCustomRepository {
    Page<BoardPostDTO.Response> findByKeyword(Pageable pageable, MainPost mainPost, String keyword);
}
