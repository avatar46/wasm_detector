import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * This class shows how you can crawl images on the web and store them in a
 * folder. This is just for demonstration purposes and doesn't scale for large
 * number of images. For crawling millions of images you would need to store
 * downloaded images in a hierarchy of folders
 */
public class JSCrawler extends WebCrawler {

    private static final Pattern jsPatterns = Pattern.compile(".*(\\.(js?))$");

    private final File storageFolder;

    private boolean containsWasm = false;

    public JSCrawler(File storageFolder) {
        this.storageFolder = storageFolder;
    }

    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        if (jsPatterns.matcher(href).matches()) {
            return true;
        }
        return false;
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();

        // Find if it loads a ".wasm" file
        String pageContent = new String(page.getContentData(), StandardCharsets.UTF_8);
        if (pageContent.contains(".wasm\"")) {
            if (!containsWasm) {
                containsWasm = true;
                writeResult(url);
            }
            WebCrawler.logger.info("The JS file: {} matches!", url);
        }
        else
            WebCrawler.logger.info("The JS file: {} doesn't match!", url);

        // Store .js files
        String filename = storageFolder.getAbsolutePath() + '/' + url.substring(url.lastIndexOf('/') + 1);
        try {
            Files.write(page.getContentData(), new File(filename));
            WebCrawler.logger.info("Stored: {}, {}", url, filename);
        } catch (IOException e) {
            WebCrawler.logger.error("Failed to write file: {}", filename, e);
        }
    }

    private void writeResult(String url) {
        String filename = System.getProperty("user.dir") + "/result.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true));
            writer.append(url.substring(0, url.lastIndexOf('/')) + "\n");
            writer.close();
        } catch (IOException e) {
            WebCrawler.logger.error("Failed to write file: {}", filename, e);
        }
    }

}