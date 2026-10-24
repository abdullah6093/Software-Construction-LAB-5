package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class SocialNetwork {

    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase(Locale.ROOT);
            Set<String> mentioned = Extract.getMentionedUsers(Arrays.asList(tweet));
            mentioned.remove(author); 
            if (!mentioned.isEmpty()) {
                followsGraph.computeIfAbsent(author, k -> new HashSet<>()).addAll(mentioned);
            }
        }
        return followsGraph;
    }

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followers = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : followsGraph.entrySet()) {
            followers.putIfAbsent(entry.getKey().toLowerCase(Locale.ROOT), 0);
            for (String followed : entry.getValue()) {
                followers.merge(followed.toLowerCase(Locale.ROOT), 1, Integer::sum);
            }
        }
        List<String> result = new ArrayList<>(followers.keySet());
        result.sort((a, b) -> followers.get(b) - followers.get(a));
        return result;
    }
}