package moe.ku6.akamai.controller.game.mai2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import moe.ku6.akamai.annotation.game.RequireGameSession;
import moe.ku6.akamai.annotation.sega.RawJson;
import moe.ku6.akamai.data.game.mai2.Mai2AccountRepo;
import moe.ku6.akamai.data.game.mai2.Mai2PlayLogRepo;
import moe.ku6.akamai.data.sega.aimedb.card.AimeCardRepo;
import moe.ku6.akamai.exception.api.APIException;
import moe.ku6.akamai.exception.api.NotAuthorizedException;
import moe.ku6.akamai.exception.api.NotFoundException;
import moe.ku6.akamai.service.game.mai2.Mai2AccountService;
import moe.ku6.akamai.service.sega.allnet.KeychipService;
import moe.ku6.akamai.util.JsonWrapper;
import moe.ku6.akamai.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping(value = "/g/mai2/Maimai2Servlet", consumes = "application/json", produces = "application/json")
@RawJson
public class Mai2Controller {
    @Autowired
    private Mai2AccountRepo accountRepo;
    @Autowired
    private Mai2AccountService accountService;
    @Autowired
    private AimeCardRepo cardRepo;
    @Autowired
    private Mai2PlayLogRepo playLogRepo;

    @PostMapping("/GetGameSettingApi")
    private JsonWrapper GetGameSettingsApi() {
        return new JsonWrapper()
                .Set("isAouAccession", true)
                .EnterObject()
                .Set("rebootStartTime", "2020-01-01 23:59:00.0")
                .Set("rebootEndTime", "2020-01-01 23:59:00.0")
                .Set("rebootInterval", 0)
                .Set("isMaintenance", false)
                .Set("requestInterval", 10)
                .Set("movieUploadLimit", 0)
                .Set("movieStatus", 0)
                .Set("movieServerUri", "")
                .Set("deliverServerUri", "")
                .Set("oldServerUri", "")
                .Set("usbDlServerUri", "")
                .Set("pingDisable", true)
                .Set("packetTimeout", 20000)
                .Set("packetTimeoutLong", 60000)
                .Set("packetRetryCount", 5)
                .Set("userDataDlErrTimeout", 300000)
                .Set("userDataDlErrRetryCount", 5)
                .Set("userDataDlErrSamePacketRetryCount", 5)
                .Set("userDataUpSkipTimeout", 0)
                .Set("userDataUpSkipRetryCount", 0)
                .Set("iconPhotoDisable", true)
                .Set("uploadPhotoDisable", false)
                .Set("maxCountMusic", 0)
                .Set("maxCountItem", 0)
                .ExitObject("gameSetting");
    }

    @PostMapping("/GetGameRankingApi")
    private JsonWrapper GetGameRankingApi() {
        return new JsonWrapper()
                .Set("type", 1)
                .Set("gameRankingList", new ArrayList<>());
    }

    @PostMapping("/GetGameEventApi")
    private JsonWrapper GetGameEventApi() {
        return new JsonWrapper()
                .Set("type", 1)
                .Set("gameEventList", new ArrayList<>());
    }

    /*
    Tournaments not supported at the moment
     */
    @PostMapping("/GetGameTournamentInfoApi")
    private JsonWrapper GetGameTournamentInfoApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("length", ret.size())
                .Set("gameTournamentInfoList", ret);
    }

    @PostMapping("/GetGameChargeApi")
    private JsonWrapper GetGameChargeApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("type", ret.size())
                .Set("gameChargeList", ret);
    }

    @PostMapping("/GetGameNgMusicIdApi")
    private JsonWrapper GetGameNgMusicIdApi() {
        var ret = new ArrayList<>();
        return new JsonWrapper()
                .Set("length", ret.size())
                .Set("gameNgMusicIdList", ret);
    }

    @PostMapping("/GetGameWeeklyDataApi")
    private JsonWrapper GetGameWeeklyDataApi() {
        return new JsonWrapper()
                .EnterObject()
                .Set("missionCategory", 0)
                .Set("updateDate", "2024-01-01 00:00:00.0")
                .Set("beforeDate", "2077-01-01 00:00:00.0")
                .ExitObject("gameWeeklyData");
    }

    @PostMapping("/GetUserPreviewApi")
    private JsonWrapper GetUserPreviewApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var user = accountRepo.FindByAimeId(userId);
        if (user == null)
            throw new NotFoundException();

        return user.GetPreviewData();
    }

    @PostMapping("/GetUserDataApi")
    private JsonWrapper GetUserDataApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userData", accountService.GetUserDataListOrThrow(userId, "userData").getFirst());
    }

    @PostMapping("/GetUserCardApi")
    private JsonWrapper GetUserCardApi(JsonWrapper body) {
        return new JsonWrapper()
                .Set("userId", body.GetInt("userId"))
                .Set("nextIndex", 0)
                .Set("length", 0)
                .Set("userCardList", new ArrayList<>());
    }

    @PostMapping("GetUserCharacterApi")
    private JsonWrapper GetUserCharacterApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userCharacterList", accountService.GetUserDataListOrThrow(userId, "userCharacterList"));
    }

    @PostMapping("/GetUserItemApi")
    private JsonWrapper GetUserItemApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var list = accountService.GetUserDataListOrThrow(userId, "userItemList");
        var itemType = body.GetLong("nextIndex") / 10000000000L;
//        log.info("Get item, type {}", itemType);

        var ret = new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("itemKind", itemType)
                .Set("userItemList", list.stream().filter(c -> c.GetInt("itemKind") == itemType).toList());
//        log.info(ret.toString());
        return ret;
    }

    @PostMapping("/GetUserCourseApi")
    private JsonWrapper GetUserCourseApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userCourseList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userCourseList", ret);
    }

    @PostMapping("/GetUserChargeApi")
    private JsonWrapper GetUserChargeApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userChargeList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userChargeList", ret);
    }

    @PostMapping("/GetUserFavoriteApi")
    private JsonWrapper GetUserFavoriteApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var itemKind = body.GetInt("itemKind");
        var ret = accountService.GetUserDataListOrThrow(userId, "userFavoriteList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userFavorite",
                        ret.stream()
                                .filter(c -> c.GetInt("itemKind") == itemKind)
                                .toList()
                );
    }

    @PostMapping("/GetUserGhostApi")
    private JsonWrapper GetUserGhostApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userGhost");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userGhostList", ret);
    }

    @PostMapping("/GetUserMapApi")
    private JsonWrapper GetUserMapApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userMapList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userMapList", ret);
    }

    @PostMapping("/GetUserLoginBonusApi")
    private JsonWrapper GetUserLoginBonusApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userLoginBonusList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userLoginBonusList", ret);
    }

    @PostMapping("/GetUserRegionApi")
    private JsonWrapper GetUserRegionApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", 0)
                .Set("userRegionList", new ArrayList<>());
    }

    @PostMapping("/GetUserRecommendRateMusicApi")
    private JsonWrapper GetUserRecommendRateMusicApi(JsonWrapper body) {
        return new JsonWrapper()
                .Set("userId", body.GetInt("userId"))
                .Set("userRecommendRateMusicIdList", new ArrayList<>());
    }

    @PostMapping("/GetUserRecommendSelectMusicApi")
    private JsonWrapper GetUserRecommendSelectMusicApi(JsonWrapper body) {
        return new JsonWrapper()
                .Set("userId", body.GetInt("userId"))
                .Set("userRecommendSelectionMusicIdList", new ArrayList<>());
    }

    @PostMapping("/GetUserOptionApi")
    private JsonWrapper GetUserOptionApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userOption");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userOption", ret.getFirst());
    }

    @PostMapping("/GetUserExtendApi")
    private JsonWrapper GetUserExtendApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userExtend");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userExtend", ret.getFirst());
    }

    @PostMapping("/GetUserRatingApi")
    private JsonWrapper GetUserRatingApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userRatingList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userRating", ret.getFirst());
    }

    //GetUserMusicApi
    @PostMapping("/GetUserMusicApi")
    private JsonWrapper GetUserMusicApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userMusicDetailList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userMusicDetailList", ret);
    }

    // GetUserFriendSeasonRankingApi
    @PostMapping("/GetUserFriendSeasonRankingApi")
    private JsonWrapper GetUserFriendSeasonRankingApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userFriendSeasonRankingList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userFriendSeasonRankingList", ret);
    }

    @PostMapping("/GetUserFavoriteItemApi")
    private JsonWrapper GetUserFavoriteItemApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("kind", body.GetInt("kind"))
                .Set("nextIndex", 0)
                .Set("length", 0)
                .Set("userFavoriteItemList", new ArrayList<>());
    }

    @PostMapping("/GetUserMissionDataApi")
    private JsonWrapper GetUserMissionDataApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var user = accountRepo.FindByAimeId(userId);
        if (user == null)
            throw new NotFoundException();

        var weeklyData = user.getFullData().GetObject("userWeeklyData");
        var missionList = user.getFullData().GetObjectList("userMissionDataList");
        return new JsonWrapper()
                .Set("userWeeklyData", weeklyData)
                .Set("userMissionDataList", missionList);
    }

    @PostMapping("/GetUserFriendBonusApi")
    private JsonWrapper GetUserFriendBonusApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("returnCode", 0)
                .Set("getMiles", 0);
    }

    @PostMapping("/GetUserActivityApi")
    private JsonWrapper GetUserActivityApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userActivityList");
        return new JsonWrapper()
                .Set("userActivity", ret);
    }

    @PostMapping("/GetUserPortraitApi")
    private JsonWrapper GetUserPortraitApi(JsonWrapper body) {
        return new JsonWrapper()
                .Set("userId", body.GetInt("userId"))
                .Set("length", 0)
                .Set("userPortraitList", new ArrayList<>());
    }

    @PostMapping("/GetUserIntimateApi")
    private JsonWrapper GetUserIntimateApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        var ret = accountService.GetUserDataListOrThrow(userId, "userIntimateList");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("nextIndex", 0)
                .Set("length", ret.size())
                .Set("userIntimateList", ret);
    }

    @PostMapping("/GetUserShopStockApi")
    private JsonWrapper GetUserShopStockApi(JsonWrapper body) {
        var userId = body.GetInt("userId");
        return new JsonWrapper()
                .Set("userId", userId)
                .Set("userShopStockList", new ArrayList<>());
    }

    @PostMapping("/UserLoginApi")
    private JsonWrapper Login(JsonWrapper body, @RequestHeader("Mai-Encoding") String version, HttpServletRequest req) {
        var aimeId = body.GetInt("userId");
        var card = cardRepo.FindByAimeId(aimeId);
        if (card == null) {
            log.warn("[UserLoginApi] Card not found for {}", aimeId);
            throw new NotAuthorizedException();
        }

        var keychipSession = KeychipService.getInstance().getSessionRepo().FindByKeychip(body.GetString("clientId"));
        if (keychipSession == null || keychipSession.getPlaceId() != body.GetInt("placeId")) {
            log.warn("[UserLoginApi] Keychip session not found for {}", body.GetString("clientId"));
            throw new NotAuthorizedException();
        }

        var account = accountRepo.FindByAimeId(aimeId);

        if (account == null) {
            // create card
            account = accountService.CreateAccountAndCommit(card);
        }

        // generate play session data
        var versionString = version.split("\\.").length >= 3 ? version : version + ".00";
        accountService.RecordAccountLogin(account, keychipSession, versionString, versionString, req.getRemoteAddr());

        /*
        {"returnCode":1,"loginCount":1,"lastLoginDate":"2020-01-01 00:00:00.0","consecutiveLoginCount":0,"loginId":1,"Bearer":"meow","bearer":"meow"}
         */
        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("loginCount", 1)
                .Set("lastLoginDate", Util.FormatDatetimeALLNetUTC(account.getLastPlay().getDate()))
                .Set("consecutiveLoginCount", 0)
                .Set("loginId", 1)
                .Set("Bearer", account.getId())
                .Set("bearer", account.getId());
    }

    @PostMapping("/UploadUserPlaylogApi")
    private JsonWrapper UploadUserPlaylogApi(JsonWrapper body) {
        var aimeId = body.GetInt("userId");

        var account = accountRepo.FindByAimeId(aimeId);
        if (account == null)
            throw new NotFoundException();

        var playLog = accountService.CreatePlayLog(account, body.GetObject("userPlaylog"));
        playLogRepo.save(playLog);

        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("api", "com.sega.maimai2servlet.api.UploadUserPlaylogApi");
    }

    @PostMapping("/UpsertUserAllApi")
    private JsonWrapper UpsertUserAllApi(JsonWrapper body) {
        var aimeId = body.GetInt("userId");
        var account = accountRepo.FindByAimeId(aimeId);
        if (account == null)
            throw new NotFoundException();
        var data = body.GetObject("upsertUserAll");

        // user data
        account.UpdateFullData(data);
        accountRepo.save(account);

        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("api", "com.sega.maimai2servlet.api.UpsertUserAllApi");
    }

    @PostMapping("/{apiName}")
    private JsonWrapper NOPEndpoints(@PathVariable("apiName") String apiName) {
        log.info("API NOP: {}", apiName);
        return new JsonWrapper()
                .Set("returnCode", 1)
                .Set("api", "com.sega.maimai2servlet.api." + apiName);
    }

}
