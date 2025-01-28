package moe.ku6.akamai.data.game;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ALLNetGameAccountRepository<T extends ALLNetGameAccount> extends MongoRepository<T, String> {
    @Query("{ 'aimeId' : ?0 }")
    T FindByAimeId(int aimeId);

    @Query("{ 'aimeId' : ?0, 'accessCode' : ?1 }")
    T FindByAimeIdAndAccessCode(int aimeId, String accessCode);
}
