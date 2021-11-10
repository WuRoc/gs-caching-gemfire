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

//    protected static final String ID_BASED_QUOTE_SERVICE_URL = "http://gturnquist-quoters.cfapps.io/api/{id}";
//
//    protected static final String RANDOM_QUOTE_SERVICE_URL = "http://gturnquist-quoters.cfapps.io/api/random";

    protected static final String ID_BASED_QUOTE_SERVICE_URL = "https://quoters.apps.pcfone.io/api/{id}";
    protected static final String RANDOM_QUOTE_SERVICE_URL = "https://quoters.apps.pcfone.io/api/random";

    private volatile boolean cacheMiss = false;

    private final RestTemplate quoteServiceTemplate = new RestTemplate();
    
    /**
     * @Description: Determines whether the previous service method invocation resulted in a cache miss.
     * @Param: []
     * @return: boolean 一个布尔值，指示先前的服务方法调用是否导致缓存未命中。
     * @Author: XiaoShuMu
     * @Date: 2021/11/8
     */
    public boolean isCacheMiss() {
        boolean cacheMiss = this.cacheMiss;
        this.cacheMiss = false;
        return cacheMiss;
    }

    protected void setCacheMiss() {
        this.cacheMiss = true;
    }


    /**
     * @Description: @Cacheable("Quotes") 表示可以缓存调用方法（或类中的所有方法）的结果的注解。请求带有给定标识符的Quotes
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

    /**
     * @Description: 要求随机quote。这里的key指定key是id
     * @Param: []
     * @return: hello.Quote 一个随机的 {@link Quote}
     * @Author: XiaoShuMu
     * @Date: 2021/11/8
     */
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
