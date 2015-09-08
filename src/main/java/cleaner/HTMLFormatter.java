package cleaner;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLFormatter {

    public void removeNode(Element element){
        if(element==null)
            return;
        element.remove();
    }

    public void removeBlankImage(Element element){
        Elements elements=element.getElementsByTag("img");
        for(Element e:elements){
            String width=e.attr("width");
            String height=e.attr("height");
            if(width.equals("1") || height.equals("1"))
                e.remove();
        }

    }


    public void addBRafterImg(Element element){
        Elements elements=element.getElementsByTag("img");
        for(Element e:elements){
            e.after("</br>");
        }
    }


}
