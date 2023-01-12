
import org.springframework.beans.factory.annotation.Value;

public class ValueSample {
    
    @Value("${metroApiKey}") public String key;
    public String ckey;

    public ValueSample(@Value("${metroApiKey}") String ckey) {
        this.ckey = ckey;
    }
    

    public static void main(String[] args) {
        ValueSample sample = new ValueSample("");
        System.out.println(sample.ckey);
        System.out.println(sample.key);
    }

}
