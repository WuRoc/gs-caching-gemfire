package hello;

import java.util.Optional;

import org.apache.geode.cache.client.ClientRegionShortcut;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;

@SpringBootApplication
@ClientCacheApplication(name = "CachingGemFireApplication")
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.LOCAL)
@EnableGemfireCaching
@SuppressWarnings("unused")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	ApplicationRunner runner(QuoteService quoteService) {
		//这个 return 后面的值与上面的args无关
		return arg -> {
			Quote quote = requestQuote(quoteService, 12L);
			requestQuote(quoteService, quote.getId());
			Quote quotes = requestQuote(quoteService, 10L);
			requestQuote(quoteService, quotes.getId());
			requestQuote(quoteService, 11L);
		};
	}

	private Quote requestQuote(QuoteService quoteService, Long id) {

		long startTime = System.currentTimeMillis();

		Quote quote = Optional.ofNullable(id)
			.map(quoteService::requestQuote)
			.orElseGet(quoteService::requestRandomQuote);

		long elapsedTime = System.currentTimeMillis();
		//%1对应后面的第一个值，%2对应后面的第二个值
		System.out.printf("\"%1$s\"%nCache Miss [%2$s] - Elapsed Time [%3$s ms]%n", quote,
			quoteService.isCacheMiss(), (elapsedTime - startTime));

		return quote;
	}
}
