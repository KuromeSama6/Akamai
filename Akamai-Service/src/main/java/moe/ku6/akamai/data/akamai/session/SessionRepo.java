package moe.ku6.akamai.data.akamai.session;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SessionRepo extends MongoRepository<Session, String> {
    @Query(value = "{'account': ?0}", delete = true)
    void DeleteByAccount(String accountId);
}
