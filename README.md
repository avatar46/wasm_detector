# wasm_detector

wasm_detector is a tool to detect if a webpage contains wasm code. 

## Quickstart

This project uses [crawler4j](https://github.com/yasserg/crawler4j), an open source web crawler for Java. You need to install
it first to run this project. 

Run the _JSCrawlController.java_ to run the project. To change the webpage link that you want to run, just modify the link for variable **crawlPages** 
in _JSCrawlController.java_ file. The result will be written to result.txt with only links of webpages that contain wasm codes inside.

## Existing problem
The wasm_detector cannot find all the javascript files for some webpages, which may cause failing to detect some webpages with wasm code.
