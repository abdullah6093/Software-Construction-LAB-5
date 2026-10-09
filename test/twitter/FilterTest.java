package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FilterTest {

    /*
     * Testing strategy for writtenBy(tweets, username):
     * - number of tweets: 0, 1, > 1
     * - number of tweets by username: 0, 1, > 1
     * - case of username: same as author, different from author
     * 
     * Testing strategy for inTimespan(tweets, timespan):
     * - number of tweets: 0, > 1
     * - tweets inside timespan: none, some, all
     * - tweet exactly on start or end of timespan: yes, no
     * 
     * Testing strategy for containing(tweets, words):
     * - number of words: 1, > 1
     * - matching tweets: none, some, all
     * - case of word: same as in tweet, different
     * - word appears only as part of a bigger word: yes, no
     * 
     * Every result is also checked for keeping the input order.
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "Alyssa", "Talking about the pset tonight", d3);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; 
    }
    
    @Test
    public void testWrittenByDifferentCaseMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "ALYSSA");
        assertEquals("expected two tweets in order", Arrays.asList(tweet1, tweet3), writtenBy);
    }
    
    @Test
    public void testInTimespanEndpointsIncluded() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), new Timespan(d2, d3));
        assertEquals("expected tweets on the endpoints", Arrays.asList(tweet2, tweet3), inTimespan);
    }
    
    @Test
    public void testContainingCaseAndWholeWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3), Arrays.asList("TALK"));
        assertEquals("expected tweets 1 and 2 only", Arrays.asList(tweet1, tweet2), containing);
    }
}