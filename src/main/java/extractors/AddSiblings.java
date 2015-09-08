package extractors;

import cleaner.DocumentCleaner;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AddSiblings {

    public static final Logger _logger= Logger.getLogger(AddSiblings.class);


    public void addSiblings(Element node){

        ContentExtractor extractor=new ContentExtractor();
        int baseScore=getBaseLineScore(node);

        Element prevSibling=node.previousElementSibling();
        while (prevSibling!=null) {

            if (prevSibling.tagName().equals("p")) {
                node.child(0).before(prevSibling.outerHtml());
                prevSibling = prevSibling.previousElementSibling();
                continue;
            }

            _logger.info("prevSibling " + prevSibling.className());

            int insertedSiblings = 0;

            Elements potentialSiblings = prevSibling.getElementsByTag("p");

            _logger.info("p count " + potentialSiblings.size());
            if (potentialSiblings.first() == null) {
                prevSibling = prevSibling.previousElementSibling();
                continue;
            }

            Elements imgs = prevSibling.getElementsByTag("img");
           if(imgs.size()>0){
            Elements child = prevSibling.children();
            for (Element el : child) {
                if (el.tagName().equals("img")) {
                    node.child(insertedSiblings).before(el);
                    insertedSiblings++;
                } else if (el.tagName().equals("p")) {
                    double paraScore = extractor.getElementScore(el);
                    if (paraScore > (float) (baseScore * 0.3)) {
                        node.child(insertedSiblings).before("<p>" + el.html() + "<p>");
                        insertedSiblings++;
                    }
                }

                else{
                    Elements ps=el.getElementsByTag("p");
                    for(Element sibling:ps){
                        double paraScore=extractor.getElementScore(sibling);
                        if(paraScore > (float)(baseScore*0.3)){
                            node.child(insertedSiblings).before("<p>" + sibling.html() + "<p>");
                            insertedSiblings++;
                        }

                    }
                }

            }
        }

           /* else{

            for(Element sibling:potentialSiblings){
                double paraScore=extractor.getElementScore(sibling);
                if(paraScore > (float)(baseScore*0.3)){
                    node.child(insertedSiblings).before("<p>" + sibling.html() + "<p>");
                    insertedSiblings++;
                }

            }
            }*/

            prevSibling=prevSibling.previousElementSibling();
        }



        Element nextSibling=node.nextElementSibling();
        while (nextSibling!=null){

            if(nextSibling.tagName().equals("p")){
                node.appendElement("p").html(nextSibling.html());
                nextSibling=nextSibling.nextElementSibling();
                continue;
            }


            Elements potentialSiblings=nextSibling.getElementsByTag("p");

            if(potentialSiblings.first()==null){
                nextSibling=nextSibling.nextElementSibling();
                continue;
            }

            for(Element sibling:potentialSiblings){
                double paraScore=extractor.getElementScore(sibling);
                if(paraScore > (float)(baseScore*0.3)){
                    node.appendElement("p").html(sibling.html());
                }

            }

            nextSibling=nextSibling.nextElementSibling();
        }
    }

    public int getBaseLineScore(Element node){
        ContentExtractor extractor=new ContentExtractor();
        DocumentCleaner cleaner=new DocumentCleaner();
        int baseScore=100000;
        int totalScore=0;
        int no_para=0;

        Elements nodesToCheck=node.getElementsByTag("p");

        for(Element el:nodesToCheck){
            double paraScore=extractor.getElementScore(el);

            if(!cleaner.isHighLinkDensity(el) && el.text().length() > 25){
                totalScore+=paraScore;
                no_para++;
            }

        }

        if(no_para > 0){
            baseScore=totalScore/no_para;
        }

        return baseScore;
    }
}
