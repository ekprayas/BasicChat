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
	ConcurrentHashMap<String, SentimentScore> cumulativeSentimentMap = 
			new ConcurrentHashMap<String, SentimentScore>();
	
	//This keeps record of the recent score of a chat between pair
	//Key is "fromUsername->toUsername"
	ConcurrentHashMap<String, SentimentScore> pairSentimentMap = 
			new ConcurrentHashMap<String, SentimentScore>();
	
	public static SentimentStore getInstance() {
		return store;
	}
	
	private SentimentStore() {
		
	}
	
	public void updateSentimentStoreMap(SentimentScore score) {
		SentimentScore origScore = cumulativeSentimentMap.get(score.getFrom());
		if(origScore == null)
			cumulativeSentimentMap.put(score.getFrom(),score);
		else {
			cumulativeSentimentMap.put(score.getFrom(), origScore.addScore(score));
		}
	}
	
	public SentimentScore getSentimentStoreMap(String username) {
		return cumulativeSentimentMap.get(username);
	}
	
	public void updatePairSentimentStoreMap(SentimentScore score) {
			pairSentimentMap.put(score.getFrom()+"->"+score.getTo(),score);
	}
	
	public SentimentScore getPairSentimentStoreMap(String fromUsername, String toUsername) {
		return pairSentimentMap.get(fromUsername+"->"+toUsername);
	}
	
}
