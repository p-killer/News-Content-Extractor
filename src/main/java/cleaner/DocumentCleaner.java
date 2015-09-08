package cleaner;

import model.Article;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class DocumentCleaner {
    public static final Logger _logger= Logger.getLogger(DocumentCleaner.class);

    public void cleanDoc(Article article){
       _logger.info("going for pre-extraction cleanup....");
        Document doc=article.getDoc();
        doc=removeHeadElements(doc);
        doc=removeScriptsAndStyles(doc);
        doc=removeUnwantedTags(doc);
        article.setDoc(doc);
    }

    public void preCleanup(Element element){
        removeScriptsAndStyles(element);
        removeUnwantedTags(element);
    }

    public void removeNode(Element element){
      try {
          if (element == null)
              return;
          try {
              element.remove();
          } catch (Exception e) {
              e.printStackTrace();
          }
      }
      catch (Exception e){
          e.printStackTrace();
      }
    }
    
    public Document removeScriptsAndStyles(Document doc){
        doc=removeTag(doc, "script");
        doc=removeTag(doc,"style");
        return  doc;
    }

    public void removeScriptsAndStyles(Element el){
        removeTag(el, "script");
        removeTag(el,"style");
    }

    public Document removeTag(Document document,String tagName){
        Elements elements=document.getElementsByTag(tagName);
        for(Element element:elements){
            removeNode(element);
        }
        _logger.info("removed tag : "+tagName);
        return document;
    }

    public void removeTag(Element node,String tagName){
        Elements elements=node.getElementsByTag(tagName);
        for(Element element:elements){
            removeNode(element);
        }
        _logger.info("removed tag : "+tagName);
    }

    public Document removeUnwantedTags(Document document){



        Elements children=document.body().children();
        Elements elements1=children.select(RegExPattern.unwantedIDs);
        for(Element element:elements1){
            if(element.text().length() < 1000)
            removeNode(element);
        }
        Elements elements2=children.select(RegExPattern.unwantedClasses);
        for(Element element:elements2){
           // _logger.info("removed "+element.className()+" size "+element.text().length());
            if(element.text().length() < 1000)
            removeNode(element);
        }
        Elements elements3=children.select(RegExPattern.unwantedNames);
        for(Element element:elements3){
            if(element.text().length() < 1000)
            removeNode(element);
        }

       // _logger.info("doc : "+document);
        return document;
    }

    public void removeUnwantedTags(Element el){

        Elements children=el.children();
        Elements elements1=children.select(RegExPattern.unwantedIDs);
        for(Element element:elements1){
                removeNode(element);
        }
        Elements elements2=children.select(RegExPattern.unwantedClasses);
        for(Element element:elements2){
                removeNode(element);
        }
        Elements elements3=children.select(RegExPattern.unwantedNames);
        for(Element element:elements3){
                removeNode(element);
        }
    }

    public Document removeHeadElements(Document document){
        Elements elements=document.head().children();
        for(Element element:elements){
            removeNode(element);
        }
        return document;
    }

    public String postExtractionCleanUp(Element element,String link){
        _logger.info("going for post-extraction cleanup....");
        removeBadNode(element);
        removeBadTag(element);
        removeDivWithDisplayNone(element);
        formateImage(element);
        removeVideoNode(element);
        replacePreTagswithP(element);
        //replaceTagsWithText(element);
        replaceHTagsWithBold(element);
        replaceLinksWithText(element);
        removeAllAtrribute(element);
        removeParaWithFewWords(element);
        removeDivWithFewWords(element);
        String finalText=textToHTML(convertToText(element),link);
        return finalText;
    }


    public void replacePreTagswithP(Element element){
        Elements elementsHTAG  = element.select("pre");
        for (Element e : elementsHTAG ) {
            e.tagName("p");
        }

    }

    public void removeDivWithDisplayNone(Element element){

        Elements elements =element.getElementsByTag("div");

        // _logger.info("All DIV Found---->");

        for( Element el:elements) {

            if(el.attr("style").contains("display:none")){

                // _logger.info("DIV Found---->");

                el.remove();

            }

        }
    }


    public String textToHTML(String data,String link){

        if(data==null || data.length() < 200){
            return null;
        }

        data="<!doctype html><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"+"</head><body>"+data;
        data=removeBrs(data);
        data=data+"</body></html>";
        return data;
    }

    public String removeBrs(String data){
        data=data.replaceAll("<br /> \n" +
                "  <br />","<br />");
        return data;
    }

    public void formateImage(Element element){
        HTMLFormatter formatter=new HTMLFormatter();
        formatter.removeBlankImage(element);
        formatter.addBRafterImg(element);
    }

    public String convertToText(Element element){
        return StringEscapeUtils.unescapeHtml(element.html()).trim();
    }

    public void removeAllAtrribute(Element element){
        Elements el = element.getAllElements();
        for (Element e : el) {
            Attributes at = e.attributes();
            for (Attribute a : at) {
                if(a.getKey().equals("src")){
                    continue;
                }
                e.removeAttr(a.getKey());

            }
        }
    }

    public void replaceHTagsWithBold(Element element){
        for(int i=1;i<6;i++) {
            Elements elementsHTAG  = element.select("h" + i);
            for (Element e : elementsHTAG ) {
                e.tagName("b");
            }
        }
    }

    public void removeBadTag(Element element){
        removeTag(element,"hr");
        removeTag(element,"input");
        removeEmptyLi(element);
    }

    public void removeEmptyLi(Element element){
        Elements elements=element.getElementsByTag("li");
        for(Element el:elements){
            if(el.text().length()==0)
                removeNode(el);
        }
    }

    public void removeParaWithFewWords(Element element){

        Elements elements=element.getElementsByTag("p");
        for(Element el:elements){
            int wordCount=0;
            String text=el.text();
            if(text.contains(" ")){
                wordCount=text.split(" ").length;
            }
            Elements imgEls=el.getElementsByTag("img");
            //_logger.info("wc "+wordCount+" img "+imgEls.size());
            if(wordCount < 3 && imgEls.size()==0){
               removeNode(el);
            }
        }
    }


    public void removeDivWithFewWords(Element element){

        Elements elements=element.getElementsByTag("div");
        for(Element el:elements){
            int wordCount=0;
            String text=el.text();
            if(text.contains(" ")){
                wordCount=text.split(" ").length;
            }
            Elements imgEls=el.getElementsByTag("img");
            if(wordCount < 3 && imgEls.size()==0){
                removeNode(el);
            }
        }
    }
    public void replaceLinksWithText(Element element){
        Elements links=element.getElementsByTag("a");
        for(Element el:links){
            el.tagName("span");
        }
    }

    public void replaceTagsWithText(Element element){
        String baseURI=element.baseUri();
        Elements boldTags=element.getElementsByTag("b");
        for(Element el:boldTags){
            TextNode tn=new TextNode(el.text(),baseURI);
            el.replaceWith(tn);
        }

        Elements strongTags=element.getElementsByTag("strong");
        for(Element el:strongTags){
            TextNode tn=new TextNode(el.text(),baseURI);
            el.replaceWith(tn);
        }

        Elements iTags=element.getElementsByTag("i");
        for(Element el:iTags){
            TextNode tn=new TextNode(el.text(),baseURI);
            el.replaceWith(tn);
        }
    }

    public void removeImgNode(Element element){
        Elements elements=element.getElementsByTag("img");
        for(Element el:elements){
            removeNode(el);
        }

    }


    public void removeVideoNode(Element element){
        Elements elements=element.getElementsByTag("iframe");
        for(Element el:elements){
            removeNode(el);
        }
    }

    public void removeBadNode(Element element){
        Elements childNodes=element.children();
        for(Element child:childNodes){
           if(child.tagName().equals("a")){
                continue;
            }
            if(isHighLinkDensity(child)){
                removeNode(child);
            }
        }

        Elements strongs=element.getElementsByTag("strong");
        for(Element child:strongs){
            if(isHighLinkDensity(child))
                removeNode(child);
        }

        Elements divs=element.getElementsByTag("div");
        for(Element child:divs){
            if(isHighLinkDensity(child))
                removeNode(child);
        }

        Elements tds=element.getElementsByTag("td");
        for(Element el:tds){
            Elements ps=el.getElementsByTag("p");
            if(ps.size()==0)
                removeNode(el);
            if(ps.size()==1 && ps.get(0).ownText().length() < 5) //for div changes to p case
                removeNode(el);
        }

        Elements hiddens=element.getElementsByAttributeValue("x-incest","hidden");
        for(Element el:hiddens){
            removeNode(el);
        }

    }


    public boolean isTableTagAndNoParagraphsExist(Element element){
        Elements subPara=element.getElementsByTag("p");
        for(Element el:subPara){
            if(el.text().length()<25)
                removeNode(el);
        }
        Elements subPara1=element.getElementsByTag("p");
        if(subPara1.size()==0 && element.tagName()=="td"){
            return true;
        }
        else
            return false;
    }

    public boolean isHighLinkDensity(Element element){
        Elements links=element.getElementsByTag("a");
        if(links.size()==0)
            return false;
        String text=element.text().trim();
        float textWordCount=0;
        textWordCount=RegExPattern.spaceSplitter.split(text).length;
        if(textWordCount==0)
            return true;

        StringBuilder sb=new StringBuilder();
        for(Element link:links){
            sb.append(link.text());
            sb.append(" ");
        }

        float linkWordCount=0;
        String linkText=sb.toString();
        linkWordCount=RegExPattern.spaceSplitter.split(linkText).length;

        float linkSize=links.size();

        float linkDivisor=linkWordCount/textWordCount;

        float score=linkDivisor * linkSize;

//        _logger.info("textsize "+textWordCount+", linksize "+linkWordCount+", n_link "+linkSize+", score "+score);

        if((score > 1 && (textWordCount-linkWordCount)<100) || ((textWordCount-linkWordCount) < 5 && linkWordCount > 0)) {
            return true;
        }

        return false;

    }

}
