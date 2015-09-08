package Network;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.HttpURLConnection;

/**
 * Created by shishir on 8/9/15.
 */
public class DocGenerator {

    public static final Logger _logger= Logger.getLogger(DocGenerator.class);

    public Document getDocument(String link){

        Document document=null;
        HttpURLConnection connection=null;
        try {

            _logger.info("going for jsoup connect");
            document = Jsoup.connect(link).ignoreContentType(true).get();
            _logger.info("document parsed..");

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(connection!=null)try{
                connection.disconnect();}
            catch (Exception e){
                _logger.error("ERROR::==" + e.getMessage());
                e.printStackTrace();
            }
        }

        return document;
    }
}
