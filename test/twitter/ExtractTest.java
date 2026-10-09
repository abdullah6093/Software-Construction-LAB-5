package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class ExtractTest {

    /*
     * Testing strategy for getTimespan(tweets):
     * - number of tweets: 1, > 1
     * - order of tweets in the list: in time order, not in time order
     * - timestamps: all the same, some different
     * 
     * Testing strategy for getMentionedUsers(tweets):
     * - number of tweets: 1, > 1
     * - mentions in a tweet: 0, 1, > 1
     * - same user mentioned: once, more than once with different case
     * - position of mention: start of text, middle, end
     * - "@" preceded by a username character (email address): yes, no
     * - mention followed by punctuation: yes, no
     * 
     * The returned set may use any case for a username, so the tests
     * compare usernames after converting them to lowercase.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:30:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "@bbitdiddle saving you a seat, ask @Evaluator too", d3);
    private static final Tweet tweet4 = new Tweet(4, "evaluator", "thanks @BBitDiddle! mail me at eval@mit.edu", d2);
    private static final Tweet tweet5 = new Tweet(5, "cy_d_fect", "see you there @cy-dfect_2", d1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }

    @Test
    public void testGetTimespanUnorderedTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet3, tweet1, tweet2));
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }

    @Test
    public void testGetMentionedUsersEmailIsNotMention() {
        Set<String> mentionedUsers = toLowerCase(Extract.getMentionedUsers(Arrays.asList(tweet4)));
        assertFalse("mit is not mentioned", mentionedUsers.contains("mit"));
        assertEquals("expected one user", new HashSet<>(Arrays.asList("bbitdiddle")), mentionedUsers);
    }

    @Test
    public void testGetMentionedUsersDifferentCase() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3, tweet4));
        assertEquals("expected each user once", 2, mentionedUsers.size());
        assertEquals("expected two users", new HashSet<>(Arrays.asList("bbitdiddle", "evaluator")), toLowerCase(mentionedUsers));
    }

    private static Set<String> toLowerCase(Set<String> usernames) {
        Set<String> result = new HashSet<>();
        for (String username : usernames) {
            result.add(username.toLowerCase());
        }
        return result;
    }
}