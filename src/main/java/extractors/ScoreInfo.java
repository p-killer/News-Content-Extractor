package extractors;

import org.jsoup.nodes.Element;

/**
 * Created by shishir on 2/6/15.
 */
public class ScoreInfo {

    public static void updateContentScore(Element element,double scoreToAdd){
        double currentScore;
        try{
            currentScore=getContentScore(element);
        }
        catch (NumberFormatException e){
            currentScore=0;
        }
        double newScore=currentScore + scoreToAdd;
        setContentScore(element,newScore);
    }

    public static void setContentScore(Element element,double score){
        element.attr("algoScore",Double.toString(score));
    }

    public static double getContentScore(Element element){
        if(element==null)
            return 0;
        try{
            String strScore=element.attr("algoScore");
            if(strScore==null || strScore=="")
                return 0;
            return Double.parseDouble(strScore);
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            return 0;
        }
    }
}
