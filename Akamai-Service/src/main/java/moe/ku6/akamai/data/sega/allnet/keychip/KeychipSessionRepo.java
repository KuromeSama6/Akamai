package moe.ku6.akamai.data.sega.allnet.keychip;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface KeychipSessionRepo extends MongoRepository<KeychipSession, String> {
    @Query("{ 'keychip' : ?0,  'gameId' : ?1 }")
    KeychipSession FindByKeychipAndGame(String keychip, String gameId);

    @Query(value = "{ 'keychip' : ?0, 'gameId' : ?1 }", delete = true)
    void DeleteByKeychipAndGameId(String keychip, String gameId);
}
