package hello;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;

import java.util.Optional;

/**
 * @ClassName Application
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-08 16:00
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
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

        return args -> {
            Quote quote = requestQuote(quoteService, 12L);
            requestQuote(quoteService, quote.getId());
            requestQuote(quoteService, 10L);
            requestQuote(quoteService, 1L);
            //如果是null，直接对应下面的随机值
            requestQuote(quoteService, null);
        };
    }

    private Quote requestQuote(QuoteService quoteService, Long id) {
        long startTime = System.currentTimeMillis();

        Quote quote = Optional.ofNullable(id)
                .map(quoteService::requestQuote)
                .orElseGet(quoteService::requestRandomQuote);

        long elapsedTime = System.currentTimeMillis();

        System.out.printf("\"%1$s\"%nCache Miss [%2$s] - Elapsed Time [%3$s ms]%n", quote, quoteService.isCacheMiss(), (elapsedTime - startTime));
        return quote;
    }
}
