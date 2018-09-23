package g.server.account;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chengjin.lyf on 2018/9/23 下午3:18
 * @since 1.0.25
 */
public class AccountManager {

    private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);

    private Map<String, String> accountMap = new HashMap<>();

    private String userJsonFile = "user.json";

    private static AccountManager instance = new AccountManager();

    private Vertx vertx;

    private int intervalSec;

    private AccountManager(){
        //do sth
    }

    public static AccountManager getInstance(){
        return instance;
    }

    public void loadConfig(Vertx vertx){
        this.vertx = vertx;
        loadConfig0();
    }

    public String getPassword(String account){
        Map<String ,String > accountMap = this.accountMap;
        return accountMap.get(account);
    }


    private void loadConfig0(){
        logger.info("************** begin to load user file ***************");
        vertx.fileSystem().readFile(userJsonFile, result->{
            if (!result.succeeded()){
                logger.error("load user config failed!");
                System.exit(1);
            }
            Buffer data = result.result();
            JsonObject jsonObject = new JsonObject(data);
            int intervalSec = jsonObject.getInteger("intervalSec");
            JsonArray userJsonArray = jsonObject.getJsonArray("userList");
            Map<String, String> accountMap = new HashMap<>();
            for (int i=0 ; i<userJsonArray.size(); i ++){
                JsonObject userJson = userJsonArray.getJsonObject(i);
                accountMap.put(userJson.getString("username"), userJson.getString("password"));
            }
            AccountManager.this.accountMap = accountMap;
            logger.info("************** load user file success ***************");
            vertx.setTimer(TimeUnit.SECONDS.toMillis(intervalSec), timerId->{
                loadConfig0();
            });
        });
    }
}
