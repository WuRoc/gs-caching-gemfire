package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName Quote
 * @Description TODO
 * @Author XiaoShuMu
 * @Version 1.0
 * @Create 2021-11-06 10:34
 * @Blog https://www.cnblogs.com/WLCYSYS/
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("unused")
public class Quote {

    private Long id;

    private String quote;

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Quote)) {
            return false;
        }

        Quote that = (Quote) obj;

        return ObjectUtils.nullSafeEquals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        int hashValue = 17;

        hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(getId());

        return hashValue;
    }

    @Override
    public String toString() {
        return getQuote();
    }
}
