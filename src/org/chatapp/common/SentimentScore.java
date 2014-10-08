package org.chatapp.common;

import java.io.Serializable;

public class SentimentScore implements Serializable{

   
	private static final long serialVersionUID = 1L;

	private String user;

    private double happiness;

    private double sadness;

    private double anger;

    private double disgust;

    private double surprise;

    private double fear;

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the happiness
     */
    public double getHappiness() {
        return happiness;
    }

    /**
     * @param happiness
     *            the happiness to set
     */
    public void setHappiness(double happiness) {
        this.happiness = happiness;
    }

    /**
     * @return the sadness
     */
    public double getSadness() {
        return sadness;
    }

    /**
     * @param sadness
     *            the sadness to set
     */
    public void setSadness(double sadness) {
        this.sadness = sadness;
    }

    /**
     * @return the anger
     */
    public double getAnger() {
        return anger;
    }

    /**
     * @param anger
     *            the anger to set
     */
    public void setAnger(double anger) {
        this.anger = anger;
    }

    /**
     * @return the disgust
     */
    public double getDisgust() {
        return disgust;
    }

    /**
     * @param disgust
     *            the disgust to set
     */
    public void setDisgust(double disgust) {
        this.disgust = disgust;
    }

    /**
     * @return the surprise
     */
    public double getSurprise() {
        return surprise;
    }

    /**
     * @param surprise
     *            the surprise to set
     */
    public void setSurprise(double surprise) {
        this.surprise = surprise;
    }

    /**
     * @return the fear
     */
    public double getFear() {
        return fear;
    }

    /**
     * @param fear
     *            the fear to set
     */
    public void setFear(double fear) {
        this.fear = fear;
    }

}