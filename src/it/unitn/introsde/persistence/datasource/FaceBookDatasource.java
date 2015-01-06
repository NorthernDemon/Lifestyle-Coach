package it.unitn.introsde.persistence.datasource;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Page;
import com.restfb.types.User;

/**
* Created by davie on 1/6/2015.
*/
public class FaceBookDatasource {
    public static void main(String args[]) {
        FacebookClient facebookClient = new DefaultFacebookClient("AQBDsrdjP2oXCkg8omz3E1PGekIGUmKO1V3rqFokKPPXMWndqEo6QbFWUQeMzUrpEihSLCTUZbBqyyDKC-Bcme2uxaY1TnpZklumg81ROZ8BPHfWnhFRNMkTCnPUkw1gR3nupR23qsgjtGD2-fXK-U3bjOZCwymxLTcD7MDw2VaKaL6KyNDmyEVsMqws10wwIUc43TeH3PtSw-FoHiQrAtlsmxTsQibaa_hqTgZfKeS44hlMeiw1yQ1uWwqgve9Kda9ABgF4blLRyXKQmSVckoTIKAEv2sJYHNSo5Vbj0Oyo27gfsPg4Y-RBnfxDruKymjFgAyIt32aWpap5BvQvl9ju");

        User user = facebookClient.fetchObject("me", User.class);
        Page page = facebookClient.fetchObject("cocacola", Page.class);

        System.out.println("User name: " + user.getName());
        System.out.println(user.getEducation());
        System.out.println("Page likes: " + page.getLikes());
    }
}
