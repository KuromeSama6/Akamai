package moe.ku6.akamai.data.sega.aimedb.card;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AimeCardRepo extends MongoRepository<AimeCard, String> {
    @Query("{ 'accessCode' : ?0 }")
    AimeCard FindByAccessCode(String accessCode);

    @Query("{ 'aimeId' : ?0 }")
    AimeCard FindByAimeId(int aimeId);
}
