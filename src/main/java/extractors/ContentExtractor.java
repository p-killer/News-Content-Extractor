package extractors;

import cleaner.DocumentCleaner;
import cleaner.RegExPattern;
import model.Article;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentExtractor {

    public static final Logger _logger= Logger.getLogger(ContentExtractor.class);

    public Article getArticleText(Article article){

        Document document=article.getDoc();
        Set<Element> parentNode = new HashSet<Element>();
        ArrayList<Element> nodesToCheck = getNodesToCheck(document);
        _logger.info("no of nodes "+nodesToCheck.size());

        for(Element element:nodesToCheck){
            if(element.text().length() < 25){
                continue;
            }

            double contentScore=getElementScore(element);
          //  _logger.info(element.className()+" --> "+contentScore);
          // _logger.info("contentScore "+contentScore+" Element "+element.tag()+" name "+element.className()+" parent "+element.parent().className());
            if(element.parent().siblingElements().size()==0){

                ScoreInfo.updateContentScore(element.parent().parent(),contentScore);
                ScoreInfo.updateContentScore(element.parent().parent().parent(),contentScore/2);
                if(!parentNode.contains(element.parent().parent().parent())){
                    parentNode.add(element.parent().parent().parent());
                }
            }
            else{
                ScoreInfo.updateContentScore(element.parent(),contentScore);
                ScoreInfo.updateContentScore(element.parent().parent(),contentScore/2);
            }


            if(!parentNode.contains(element.parent())){
                parentNode.add(element.parent());
            }
            if(!parentNode.contains(element.parent().parent())){
                parentNode.add(element.parent().parent());
            }
        }

        Element bestNode=getBestNode(parentNode);
        AddSiblings addSiblings=new AddSiblings();
        if(bestNode!=null){
            addSiblings.addSiblings(bestNode);
        }
     // _logger.info("best node:: "+bestNode);

        article.setTopNode(bestNode);
        return article;
    }


    //select elements with tag <p>,<pre>,<td> and special div
    public ArrayList<Element> getNodesToCheck(Document document){

      //  _logger.info("doc : "+document);
        ArrayList<Element> nodesToCheck=new ArrayList<Element>();
        nodesToCheck.addAll(document.getElementsByTag("p"));
        nodesToCheck.addAll(document.getElementsByTag("pre"));
        nodesToCheck.addAll(document.getElementsByTag("td"));
        nodesToCheck=getDivTag(nodesToCheck,document); //to handle TOI case
        return nodesToCheck;
    }

    public double getElementScore(Element element){
        double contentScore=0;
        DocumentCleaner documentCleaner=new DocumentCleaner();
        if(documentCleaner.isHighLinkDensity(element)) {
            return 0;
        }

        //text length criteria
        contentScore+=getTextLenScore(element);

        //id and class name criteria
        contentScore+=getIdAndClassNameScore(element);

        //short desc match criteria
        contentScore+=getShortDescMatchScore(element);

        contentScore-=getULScore(element);

        return contentScore;
    }

    public double getTextLenScore(Element element){
        double score=0;
        score=(int) Math.round(element.text().length() / 100.0 * 10);
        return  score;
    }

    public double getULScore(Element element){
        double score=0;
        Elements elements=element.getElementsByTag("ul");
        for(Element el:elements){
            score+=(int) Math.round(el.text().length() / 100.0 * 10);
        }
        return score;
    }

    public double getIdAndClassNameScore(Element element){
        double score=0;
        if(RegExPattern.positive.matcher(element.className()).find())
            score+=40;
        if (RegExPattern.positive.matcher(element.id()).find())
            score+=40;
        return score;
    }

    public double getShortDescMatchScore(Element element){
        double score=0;

        return score;
    }

    private Element changeElementTag(Element element, String newTag,Document document) {
        Element newElement = document.createElement(newTag);
        List<Node> copyOfChildNodeList = new ArrayList<Node>();
        copyOfChildNodeList.addAll(element.childNodes());
        for (Node n : copyOfChildNodeList) {
            n.remove();
            newElement.appendChild(n);
        }
        element.replaceWith(newElement);
      //  _logger.info("new child "+newElement);
        return newElement;
    }

    public ArrayList<Element> getDivTag(ArrayList<Element> nodesToCheck,Document document){
       Elements divs=document.getElementsByTag("div");
        for(Element el:divs){
            if(el.text().length() < 25)
                continue;
            int length=el.text().length();
            boolean hasBlock=false;
            Elements divChild=el.getAllElements();
            for(Element child:divChild){
                if(child!=el) {
                    if ("p;pre;td;".contains(child.tagName())) {
                        hasBlock = true;
                        break;
                    }
                    if("div".contains(child.tagName()) && child.text().length() > 25){

                            hasBlock = true;
                            break;

                    }
                }
            }
            if(hasBlock==false){
                Element newElement=changeElementTag(el,"p",document);
                newElement.addClass(el.className());
                nodesToCheck.remove(el);
                nodesToCheck.add(newElement);
            }
        }
        return nodesToCheck;
    }

    public Element getBestNode(Set<Element> candidate){
           Element bestNode=null;
           double topScore=0;
           for(Element node:candidate){
               double score=ScoreInfo.getContentScore(node);
              // _logger.info("candidate : "+node.className()+" score "+score);
               if(score > topScore){
                   topScore=score;
                   bestNode=node;
               }
           }
        return bestNode;
    }
}
