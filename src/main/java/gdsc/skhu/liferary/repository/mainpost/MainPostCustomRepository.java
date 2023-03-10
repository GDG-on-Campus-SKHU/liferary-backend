package gdsc.skhu.liferary.repository.mainpost;

import gdsc.skhu.liferary.domain.DTO.MainPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MainPostCustomRepository {
    Page<MainPostDTO.Response> findByKeyword(Pageable pageable, String keyword);
}
