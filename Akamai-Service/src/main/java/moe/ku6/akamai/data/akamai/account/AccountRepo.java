package moe.ku6.akamai.data.akamai.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AccountRepo extends MongoRepository<AkamaiAccount, String> {
    @Query("{ 'username' : ?0 }")
    AkamaiAccount FindByUsername(String username);

    @Query("{ 'email' : ?0 }")
    AkamaiAccount FindByEmail(String email);

    @Query("{ $or: [ { 'username' : ?0 }, { 'email' : ?1 } ] }")
    AkamaiAccount FindByUsernameOrEmail(String username, String email);

    @Query("{ 'keychip' : ?0 }")
    AkamaiAccount FindByKeychip(String keychip);
}
