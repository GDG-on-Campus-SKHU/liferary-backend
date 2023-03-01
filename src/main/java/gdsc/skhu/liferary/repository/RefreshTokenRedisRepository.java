package gdsc.skhu.liferary.repository;

import gdsc.skhu.liferary.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {
}
