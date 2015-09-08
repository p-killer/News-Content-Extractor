import Network.DocGenerator;
import cleaner.DocumentCleaner;
import extractors.ContentExtractor;
import extractors.ImageExtractor;
import model.Article;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

/**
 * Created by shishir on 6/9/15.
 */
public class ExtractionManager {

    public static final Logger _logger= Logger.getLogger(ExtractionManager.class);

    private Article article;

    public ExtractionManager(){
        article=new Article();
    }


   public Article extract(String link){
       return processExtraction(link);
   }

    private Article processExtraction(String link){
        DocumentCleaner cleaner=new DocumentCleaner();
        ContentExtractor extractor=new ContentExtractor();
        ImageExtractor imageExtractor=new ImageExtractor();
        DocGenerator docGenerator=new DocGenerator();
        Document document=docGenerator.getDocument(link);
        if(document==null){
            _logger.info("Failed to get document from given link.");
            return null;
        }

        article.setLink(link);
        article.setDoc(document);

        cleaner.cleanDoc(article);

        extractor.getArticleText(article);

        if(article.getTopNode()==null){
            _logger.info("Best node not found : we failed");
            return null;
        }

        imageExtractor.getBestImage(article);
        setFullImageURL(article,link);
        String articleText=cleaner.postExtractionCleanUp(article.getTopNode(),link);
        article.setArticleText(articleText);
        _logger.info("best node "+article.getTopNode().className());
        _logger.info("best img "+article.getImgURL());
        return article;
    }

    public void setFullImageURL(Article article,String url){
        String imgURL=article.getImgURL();
        if(imgURL != null && imgURL!=""){
            if(!imgURL.contains("http") && url.contains("http")){
                String prefix=url.split(".com/")[0]+".com/";
                imgURL=prefix+imgURL;
                article.setImgURL(imgURL);
            }
        }
    }


    public static void main(String[] args){
        String link="";
        ExtractionManager manager=new ExtractionManager();
        Article article1=manager.extract(link);
    }
}
