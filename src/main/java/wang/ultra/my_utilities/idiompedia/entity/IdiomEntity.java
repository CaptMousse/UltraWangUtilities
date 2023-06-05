package wang.ultra.my_utilities.idiompedia.entity;

import lombok.Data;

@Data
public class IdiomEntity {
    private String uuid;
    private String word;
    private String word_1;
    private String word_2;
    private String word_3;
    private String word_4;
    private String pinyin;
    private String pinyin_r;
    private String pinyin_simple;
    private String pinyin_1;
    private String pinyin_2;
    private String pinyin_3;
    private String pinyin_4;
    private String explanation;
    private String derivation;
    private String example;
    private long amount;

    // uuid, word, pinyin, pinyin_r, pinyin_simple, pinyin_first, pinyin_last,
    // explanation, derivation, example
}
