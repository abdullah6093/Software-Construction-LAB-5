package twitter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {

    public static Timespan getTimespan(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            return new Timespan(Instant.EPOCH, Instant.EPOCH);
        }
        Instant start = tweets.get(0).getTimestamp();
        Instant end = start;
        for (Tweet tweet : tweets) {
            Instant time = tweet.getTimestamp();
            if (time.isBefore(start)) {
                start = time;
            }
            if (time.isAfter(end)) {
                end = time;
            }
        }
        return new Timespan(start, end);
    }

    private static final Pattern MENTION = Pattern.compile("(?<![\\w-])@([\\w-]+)");

    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> mentioned = new HashSet<>();
        for (Tweet tweet : tweets) {
            Matcher matcher = MENTION.matcher(tweet.getText());
            while (matcher.find()) {
                mentioned.add(matcher.group(1).toLowerCase(Locale.ROOT));
            }
        }
        return mentioned;
    }
}