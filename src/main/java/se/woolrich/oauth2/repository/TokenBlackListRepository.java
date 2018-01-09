package se.woolrich.oauth2.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import se.woolrich.mongo.TokenBlackList;
import se.woolrich.mongo.User;

import java.util.List;
import java.util.Optional;

public interface TokenBlackListRepository extends MongoRepository<TokenBlackList, String> {
     Optional<TokenBlackList> findByJti(String jti);
     List<TokenBlackList> findByUserIdAndIsBlackListed(String userId, boolean isBlackListed);
     List<TokenBlackList> deleteAllByUserIdAndExpiresBefore(String userId, Long date);

}
