package hku.hk.EmoLM.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserBloomFilter {
    private BloomFilter<Integer> bloomFilter;

    @PostConstruct
    public void init() {
        bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 10000, 0.01);
    }

    public void add(int userId) {
        bloomFilter.put(userId);
    }

    public boolean mightContain(int userId) {
        return bloomFilter.mightContain(userId);
    }
}