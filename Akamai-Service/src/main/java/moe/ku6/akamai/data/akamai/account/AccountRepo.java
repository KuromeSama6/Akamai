package moe.ku6.akamai.data.akamai.account;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AccountRepo extends MongoRepository<Account, String> {
    @Query("{ 'username' : ?0 }")
    Account FindByUsername(String username);

    @Query("{ 'email' : ?0 }")
    Account FindByEmail(String email);

    @Query("{ $or: [ { 'username' : ?0 }, { 'email' : ?1 } ] }")
    Account FindByUsernameOrEmail(String username, String email);

    @Query("{ 'keychip' : ?0 }")
    Account FindByKeychip(String keychip);
}
