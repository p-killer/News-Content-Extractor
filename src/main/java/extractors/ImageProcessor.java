package extractors;

import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ImageProcessor {

    public static final Logger _logger= Logger.getLogger(ImageProcessor.class);

    public void changeImageSize(String src,String dest,String fileName){

        try{
            String imgQuery="convert "+src+" -resize 550x350 "+dest+fileName;

            Process process = Runtime.getRuntime().exec(imgQuery);
            int processId = process.waitFor();

            process.destroy();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getImageHeightFromURL(String link){
        int height=0;
        try{
            URL url=new URL(link);
            ImageIcon imageIcon=new ImageIcon(url);
            height=imageIcon.getIconHeight();

        }
        catch (Exception e){
           // e.printStackTrace();
        }
        return height;
    }

    public String getFullImageURL(String imgURL,String link){
        if(imgURL != null && imgURL!=""){
            if(!imgURL.contains("http") && link.contains("http")){
                String prefix=link.split(".com/")[0]+".com/";
                imgURL=prefix+imgURL;
            }
        }
        return imgURL;
    }

   
}
