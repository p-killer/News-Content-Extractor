package model;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by shishir on 8/9/15.
 */
public class Article {

    private String rawHtml;
    private Document doc;
    private String imgURL;
    private String articleText;
    private Element topNode;
    private String link;

    public String getRawHtml() {
        return rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public Document getDoc() {
        return doc;
    }

    public void setDoc(Document doc) {
        this.doc = doc;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getArticleText() { return articleText; }

    public void setArticleText(String articleText) {
        this.articleText = articleText;
    }

    public Element getTopNode() {
        return topNode;
    }

    public void setTopNode(Element topNode) {
        this.topNode = topNode;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
