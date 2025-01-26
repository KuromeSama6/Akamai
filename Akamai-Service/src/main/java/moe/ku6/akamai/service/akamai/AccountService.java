package moe.ku6.akamai.service.akamai;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.data.akamai.account.AccountRepo;
import moe.ku6.akamai.exception.api.APIException;
import moe.ku6.akamai.exception.sega.allnet.ALLNetException;
import moe.ku6.akamai.util.sega.ALLNetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
    private static final int MAX_KEYCHIP_GEN_RETRIES = 5;
    @Getter
    private static AccountService instance;
    @Autowired
    private AccountRepo accountRepo;

    @PostConstruct
    private void Init() {
        instance = this;
    }

    public String AllocateKeychipId() {
        for (int i = 0; i < MAX_KEYCHIP_GEN_RETRIES; i++) {
            var ret = ALLNetUtil.GenerateKeychipId();
            if (accountRepo.FindByKeychip(ret) == null) return ret;
        }

        throw new APIException(409, 17, "failed to allocate keychip id");
    }

}
