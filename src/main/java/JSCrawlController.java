import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class JSCrawlController {

    public static void main(String[] args) throws Exception {
        CrawlConfig config = new CrawlConfig();

        // Set the folder where intermediate crawl data is stored (e.g. list of urls that are extracted from previously
        // fetched pages and need to be crawled later).
        String workDir = System.getProperty("user.dir");
        config.setCrawlStorageFolder(workDir + "/tmp/crawler4j/");

        // Number of threads to use during crawling. Increasing this typically makes crawling faster. But crawling
        // speed depends on many other factors as well. You can experiment with this to figure out what number of
        // threads works best for you.
        int numberOfCrawlers = 10;

        // Store the downloaded JS files.
        File storageFolder = new File(workDir + "/tmp/crawled-javascript/");

        // Set this parameter to true to make sure all files are included in the crawl.
        config.setIncludeBinaryContentInCrawling(true);

        // Instantiate the controller for this crawl.
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        // For each crawl, you need to add some seed urls. These are the first
        // URLs that are fetched and then the crawler starts following links
        // which are found in these pages.
        List<String> crawlPages = Arrays.asList("https://magnum.graphics/showcase/picking/",
                                                "https://www.qt.io/web-assembly-example-pizza-shop",
                                                "https://ruffle.rs",
                                                "https://www.funkykarts.rocks/demo.html",
                                                "https://playgameoflife.com",
                                                "https://www.pdftron.com/webviewer/demo/",
                                                "https://colineberhardt.github.io/wasm-sudoku-solver/",
                                                "https://wasm4.org/play/break-it/",
                                                "https://sandspiel.club",
                                                "https://earth.google.com/web/@40.43884444,-88.03229828,63170000a,0d,35y,356.37537929h,0t,0r?beta=1");
        for (String webPage : crawlPages) {
            controller.addSeed(webPage);
        }

        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }

        // Create a file to write in the results.
        String filename = System.getProperty("user.dir") + "/result.txt";
        File result = new File(filename);
        Files.deleteIfExists(result.toPath());
        result.createNewFile();

        CrawlController.WebCrawlerFactory<JSCrawler> factory = () -> new JSCrawler(storageFolder);
        controller.start(factory, numberOfCrawlers);
    }

}