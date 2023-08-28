package wang.ultra.my_utilities.common.cache.blogContextVisits;

import lombok.Data;

@Data
public class BlogContextVisitsEntity {
    private String visitId;
    private long expireMillis;

    public BlogContextVisitsEntity(String visitId, long expireMillis) {
        this.visitId = visitId;
        this.expireMillis = expireMillis;
    }
}
