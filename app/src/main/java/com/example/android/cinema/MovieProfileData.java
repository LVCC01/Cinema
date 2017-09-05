package com.example.android.cinema;

/**
 * Created by hjadhav on 4/21/2017.
 */

public class MovieProfileData {
    String posterUrl;
    String titleText;
    String plotText;
    String ratingText;
    String releaseDateText;
    String idNumber;

    String idText;
    String keyText;
    String nameText;

    String idReviewText;
    String authorReviewText;
    String contentReviewText;

    //Public constructor
    public MovieProfileData (String posterUrl , String titleText, String plotText, String ratingText, String releaseDateText , String idNumber ){
        this.posterUrl = posterUrl;
        this.titleText = titleText;
        this.plotText = plotText;
        this.ratingText = ratingText;
        this.releaseDateText = releaseDateText;
        this.idNumber = idNumber;
    }

    //Public constructor for trailers
    public MovieProfileData (String idText,String keyText,String nameText){
        this.idText = idText;
        this.keyText = keyText;
        this.nameText = nameText;
    }

    /*//Public constructor for reviews
    public MovieProfileData (String idReviewText,String authorReviewText,String contentReviewText){
        this.idReviewText = idReviewText;
        this.authorReviewText = authorReviewText;
        this.contentReviewText = contentReviewText;
    }*/

    //Public constructor
    public MovieProfileData(){

    }
    // Getter for posterUrl
    public String getPosterUrl(){
        return posterUrl;
    }
    // Setter for posterUrl
    public void setPosterUrl(String posterUrl){
        this.posterUrl = posterUrl;
    }
    // Getter for titleText
    public String getTitleText(){
        return titleText;
    }
    // Setter for titleText
    public void setTitleText(String titleText){
        this.titleText = titleText;
    }
    // Getter for plotText
    public String getPlotText(){
        return plotText;
    }
    // Setter for plotText
    public void setPlotText(String plotText){
        this.plotText = plotText;
    }
    // Getter for ratingText
    public String getRatingText(){
        return ratingText;
    }
    // Setter for ratingText
    public void setRatingText(String ratingText){
        this.ratingText = ratingText;
    }
    // Getter for releaseDateText
    public String getReleaseDateText(){
        return releaseDateText;
    }
    // Setter for releaseDateText
    public void setReleaseDateText(String releaseDateText){
        this.releaseDateText = releaseDateText;
    }
    // Getter for idNumber
    public String getIdNumber() {return idNumber; }
    // Setter for idNumber
    public void setIdNumber (String idNumber){this.idNumber = idNumber;}

    // Getter and Setter for trailers

   // Getter for idText
    public String getIdText() {return idText; }
    // Setter for idText
    public void setIdText (String idText){this.idText = idText;}
    // Getter for keyText
    public String getKeyText() {return keyText; }
    // Setter for idText
    public void setKeyText (String keyText){this.keyText = keyText;}
    // Getter for nameText
    public String getNameText() {return nameText; }
    // Setter for nameText
    public void setNameText (String nameText){this.nameText = nameText;}


    // Getter and Setter for reviews

    // Getter for idReviewText
    public String getIdReviewText() {return idReviewText; }
    // Setter for idReviewText
    public void setIdReviewText (String idReviewText){this.idReviewText = idReviewText;}

    // Getter for authorReviewText
    public String getAuthorReviewText() {return authorReviewText; }
    // Setter for idText
    public void setAuthorReviewText (String authorReviewText){this.authorReviewText = authorReviewText;}

    // Getter for contentReviewText
    public String getContentReviewText() {return contentReviewText; }
    // Setter for contentReviewText
    public void setContentReviewText (String contentReviewText){this.contentReviewText = contentReviewText;}
}
