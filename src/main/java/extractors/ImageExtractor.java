package extractors;

import cleaner.RegExPattern;
import model.Article;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;

public class ImageExtractor {

    public static final Logger _logger= Logger.getLogger(ImageExtractor.class);

    public void getBestImage(Article article){
        String imgSrc="";
        String link=article.getLink();
        imgSrc=getImageFromCandidateNode(article.getTopNode(),link);
        if(imgSrc==null){
           int index=article.getTopNode().elementSiblingIndex();
            for(int i=0;i<index;i++){
                imgSrc=getImageFromCandidateNode(article.getTopNode().parent().child(i),link);
                if(imgSrc!=null)
                    break;
            }
        }
        if(imgSrc==null){
            int index=article.getTopNode().parent().elementSiblingIndex();
            for(int i=0;i<index;i++){
                imgSrc=getImageFromCandidateNode(article.getTopNode().parent().parent().child(i),link);
                if(imgSrc!=null)
                    break;
            }
        }

        article.setImgURL(imgSrc);
    }

    public String getImageFromCandidateNode(Element element,String link){
        filterBadImages(element);
        ImageProcessor imageProcessor=new ImageProcessor();
        Elements elements=element.select("img");
        for(Element imgElement:elements){
            String src=imgElement.attr("src");
            if(src==null || src=="")
                continue;
            if(src.contains("jpeg") || src.contains("jpg")){
                src=imageProcessor.getFullImageURL(src,link);
                if(imageProcessor.getImageHeightFromURL(src) > 100)
                return src;
            }
        }
        return  null;
    }

    public void filterBadImages(Element element){
        Elements imgElements=element.getElementsByTag("img");
        for(Element el:imgElements){
            String src=el.attr("src");
            if(src==null || src=="")
                continue;
            Matcher matcher= RegExPattern.badImageNamesPattern.matcher(src);
            if(matcher.find()){
                el.remove();
            }
        }
    }
}
