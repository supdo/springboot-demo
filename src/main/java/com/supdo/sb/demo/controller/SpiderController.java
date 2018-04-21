package com.supdo.sb.demo.controller;

import com.supdo.sb.demo.entity.PageContent;
import com.supdo.sb.demo.entity.PageList;
import com.supdo.sb.demo.service.GithubRepoPageProcessor;
import com.supdo.sb.demo.service.SpiderProcessService;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {

    @Autowired
    GithubRepoPageProcessor githubRepoPageProcessor;

    @Autowired
    SpiderProcessService spiderProcessService;

    @GetMapping("/test")
    public String mainView(Map<String, Object> map) {
        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
        //Spider.create(githubRepoPageProcessor).addUrl("https://blog.csdn.net/nongshuqiner/").thread(5).run();
        //Spider spider = Spider.create(githubRepoPageProcessor);
        //ResultItems result = spider.get("https://blog.csdn.net/nongshuqiner/");

        String url = "https://blog.csdn.net/nongshuqiner/";
        ArrayList<PageList> apl = null;
        try {
            Document doc = Jsoup.connect(url).get();
            apl = spiderProcessService.processList(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("apl",apl);
        return render("/spider/test");
    }

    @GetMapping("/GetPC")
    public Result getPCView(@RequestParam("url") String url){
        PageContent pc = spiderProcessService.processContent(url);
        result.simple(true, "成功！");
        result.setHtml(pc.getContent());
        return result;
    }
}
