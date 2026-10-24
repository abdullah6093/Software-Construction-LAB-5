package twitter;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            assert false;
            throw new Error("Always run main and tests with assertions enabled");
        } catch (AssertionError ae) { }
        
        // Using local sample tweets because the MIT server is offline
        Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
        Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
        Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
        Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
        Tweet tweet3 = new Tweet(3, "alyssa", "@bbitdiddle saving you a seat!", d2);
        Tweet tweet4 = new Tweet(4, "evaluator", "thanks @BBitDiddle! mail me at eval@mit.edu", d1);
        
        final List<Tweet> tweets = Arrays.asList(tweet1, tweet2, tweet3, tweet4);
        
        System.out.println("Fetched " + tweets.size() + " sample tweets locally.");
        
        final Timespan span = Extract.getTimespan(tweets);
        System.out.println("Timespan ranges from " + span.getStart() + " to " + span.getEnd());
        
        final Set<String> mentionedUsers = Extract.getMentionedUsers(tweets);
        System.out.println("Mentioned users: " + mentionedUsers);
        
        final List<Tweet> alyssaTweets = Filter.writtenBy(tweets, "alyssa");
        System.out.println("Tweets written by alyssa: " + alyssaTweets.size());
        
        final List<Tweet> talkTweets = Filter.containing(tweets, Arrays.asList("talk"));
        System.out.println("Tweets containing 'talk': " + talkTweets.size());
        
        // Lab 07: Infer the social network and print influencers
        final java.util.Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        System.out.println("Follows graph has " + followsGraph.size() + " nodes");
        
        final List<String> influencers = SocialNetwork.influencers(followsGraph);
        System.out.println("Top influencers:");
        for (String username : influencers) {
            System.out.println(username);
        }
    }
}