package hello;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName QuoteService
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-06 11:19
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@SuppressWarnings("unused")
@Service
public class QuoteService {

    protected static final String ID_BASED_QUOTE_SERVICE_URL = "http://gturnquist-quoters.cfapps.io/api/{id}";

    protected static final String RANDOM_QUOTE_SERVICE_URL = "http://hturnquist-quoters.cfapps.io/api/random";

    private volatile boolean cacheMiss = false;

    private final RestTemplate quoteServiceTemplate = new RestTemplate();

    public boolean isCacheMiss() {
        boolean cacheMiss = this.cacheMiss;
        this.cacheMiss = false;
        return cacheMiss;
    }

    protected void setCacheMiss() {
        this.cacheMiss = true;
    }


    /**
     * @Description: @Cacheable("Quotes") 表示可以缓存调用方法（或类中的所有方法）的结果的注解。
     * @Param: [id]
     * @return: hello.Quote
     * @Author: XiaoShuMu
     * @Date: 2021/11/6
     */
    @Cacheable("Quotes")
    public Quote requestQuote(Long id) {

        setCacheMiss();
        return requestQuote(ID_BASED_QUOTE_SERVICE_URL, Collections.singletonMap("id", id));
    }

    @CachePut(cacheNames = "Quotes", key = "#result.id")
    public Quote requestRandomQuote() {
        setCacheMiss();
        return requestQuote(RANDOM_QUOTE_SERVICE_URL);
    }

    protected Quote requestQuote(String URL) {
        return requestQuote(URL, Collections.emptyMap());
    }

    private Quote requestQuote(String URL, Map<String, Object> urlVariables) {

        return Optional.ofNullable(this.quoteServiceTemplate.getForObject(URL, QuoteResponse.class, urlVariables)).map(QuoteResponse::getQuote)
                .orElse(null);
    }
    //两个数比较

}
