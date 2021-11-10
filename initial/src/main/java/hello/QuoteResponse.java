package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @ClassName QuoteResponse
 * @Description 这两个类都标有 @JsonIgnoreProperties(ignoreUnknown=true)。这意味着即使可以检索其他 JSON 属性，它们也会被忽略。
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-06 10:53
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuoteResponse {

    @JsonProperty("value")
    private Quote quote;

    @JsonProperty("type")
    private String status;


    @Override
    public String toString() {
        return String.format("{ @type = %1$s,  quote = '%2$s', status = %3$s }",
        getClass().getName(), getQuote(), getStatus());
    }
}
