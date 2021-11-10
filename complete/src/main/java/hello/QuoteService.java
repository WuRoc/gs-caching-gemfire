package hello;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("unused")
@Service
public class QuoteService {

//	protected static final String ID_BASED_QUOTE_SERVICE_URL = "http://gturnquist-quoters.cfapps.io/api/{id}";
//	protected static final String RANDOM_QUOTE_SERVICE_URL = "https://gturnquist-quoters.cfapps.io/api/random";
    protected static final String ID_BASED_QUOTE_SERVICE_URL = "https://quoters.apps.pcfone.io/api/{id}";
	protected static final String RANDOM_QUOTE_SERVICE_URL = "https://quoters.apps.pcfone.io/api/random";

	/**
	 * @Description: 这里没有加锁，volatile只能确保可见性
	 * @Param:
	 * @return:
	 * @Author: XiaoShuMu
	 * @Date: 2021/11/9
	 */
	private volatile boolean cacheMiss = false;

	private final RestTemplate quoteServiceTemplate = new RestTemplate();

	/**
	 * Determines whether the previous service method invocation resulted in a cache miss.
	 *
	 * @return a boolean value indicating whether the previous service method invocation resulted in a cache miss.
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
	 * Requests a quote with the given identifier.
	 *
	 * @param id the identifier of the {@link Quote} to request.
	 * @return a {@link Quote} with the given ID.
	 */
	@Cacheable("Quotes")
	public Quote requestQuote(Long id) {
		setCacheMiss();
		//这里的是带有id的
		return requestQuote(ID_BASED_QUOTE_SERVICE_URL, Collections.singletonMap("id", id));
	}

	/**
	 * Requests a random quote.
	 *
	 * @return a random {@link Quote}.
	 */
	@CachePut(cacheNames = "Quotes", key = "#result.id")
	public Quote requestRandomQuote() {
		setCacheMiss();
		return requestQuote(RANDOM_QUOTE_SERVICE_URL);
	}

	protected Quote requestQuote(String URL) {
		//这里是空的map，根据参数进行重载
		return requestQuote(URL, Collections.emptyMap());
	}

	protected Quote requestQuote(String URL, Map<String, Object> urlVariables) {

		return Optional.ofNullable(this.quoteServiceTemplate.getForObject(URL, QuoteResponse.class, urlVariables))
			.map(QuoteResponse::getQuote)
			.orElse(null);
	}
}
