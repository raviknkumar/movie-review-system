package enums;

import java.util.Arrays;
import java.util.Comparator;

public enum Role {
    VIEWER(0,1),
    CRITIC(3,2),
    EXPERT(5, 3),
    ADMIN(30, 4);

    private final int minReviews;

    public static Integer findRatingMultipler(Role role) {
        for (Role roleIter : Role.values()){
            if(role == roleIter)
                return role.ratingMultipler;
        }
        return -1;
    }

    public static Role getRoleByReviewCount(int numberOfReviews) {

        Role[] roles = Role.values();
        Arrays.sort(roles, Comparator.comparing(Role::getMinReviews).reversed());
        for (Role role : roles){
            if(numberOfReviews >= role.minReviews){
                return role;
            }
        }
        return VIEWER;
    }

    public int getMinReviews() {
        return minReviews;
    }

    public int getRatingMultipler() {
        return ratingMultipler;
    }

    private final int ratingMultipler;

    Role(int minReviews, int scoreMultipler) {
        this.minReviews=minReviews;
        this.ratingMultipler =scoreMultipler;
    }
}
