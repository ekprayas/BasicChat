/**
 * 
 */
package org.chatapp.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author prabhat
 *
 */
public class SentimentStore {

	private static SentimentStore store = new SentimentStore();
	ConcurrentHashMap<String, SentimentScore> sentimentMap = 
			new ConcurrentHashMap<String, SentimentScore>();
	
	public static SentimentStore getInstance() {
		return store;
	}
	
	private SentimentStore() {
		
	}
	public void updateSentimentStoreMap(SentimentScore score) {
		SentimentScore origScore = sentimentMap.get(score.getUser());
		if(origScore == null)
			sentimentMap.put(score.getUser(),score);
		else {
			sentimentMap.put(score.getUser(), origScore.addScore(score));
		}
	}
	
	public SentimentScore getSentimentStoreMap(String username) {
		return sentimentMap.get(username);
	}
}
