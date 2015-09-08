package cleaner;

import java.util.regex.Pattern;

public class RegExPattern {
    public static Pattern captionPattern;
    public static String unwantedIDs;
    public static String unwantedClasses;
    public static String unwantedNames;
    public static Pattern positive;
    public static Pattern badImageNamesPattern;
    public static Pattern spaceSplitter;

    static {
        StringBuilder sb=new StringBuilder();
        sb.append("^side$|combx|retweet|mediaarticlerelated|menucontainer|navbar|comment|PopularQuestions|contact|foot|footer|Footer|footnote|cnn_strycaptiontxt|links|meta$|shoutbox|sponsor");
        sb.append("|tags|socialnetworking|socialbarline|socialNetworking|cnnStryHghLght|cnn_stryspcvbx|^inset$|pagetools|post-attributes|welcome_form|contentTools2|the_answers|remember-tool-tip");
        sb.append("|communitypromo|runaroundLeft|subscribe|vcard|articleheadings|date|^print$|popup|author-dropdown|tools|socialtools|byline|konafilter|KonaFilter|breadcrumbs|^fn$|wp-caption-text");
        sb.append("|article-caption|amzn|visually-hidden|flipboard-keep|social-icons|outbrain-widget|btm_lnk");
        String regExRemoveNodes=sb.toString();
        unwantedIDs="[id~=(" + regExRemoveNodes + ")]";
        unwantedClasses="[class~=(" + regExRemoveNodes + ")]";
        unwantedNames="[name~=(" + regExRemoveNodes + ")]";
        captionPattern=Pattern.compile("");

        positive=Pattern.compile("(^(body|content|h?entry|main|page|post|text|blog|story|haupt))|arti(cle|kel)|instapaper_body|art(ext|text)|sty|stry");

        StringBuilder badImage=new StringBuilder();
        sb.append(".html|.ico|button|twitter.jpg|facebook.jpg|digg.jpg|digg.png|delicious.png|facebook.png|reddit.jpg|doubleclick|diggthis|diggThis|adserver|/ads/|ec.atdmt.com");
        sb.append("|mediaplex.com|adsatt|view.atdmt|reuters_fb_share.jpg");
        badImageNamesPattern = Pattern.compile(sb.toString());

        spaceSplitter=Pattern.compile(" ");
    }
}
