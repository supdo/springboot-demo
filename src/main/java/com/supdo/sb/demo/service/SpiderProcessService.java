package com.supdo.sb.demo.service;

import com.supdo.sb.demo.entity.PageContent;
import com.supdo.sb.demo.entity.PageList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class SpiderProcessService {

    public ArrayList<PageList> processList(Document doc){
        ArrayList<PageList> apl = new ArrayList<PageList>();
        //提取数据
        Elements eleList = doc.select("ul.blog-units.blog-units-box > li.blog-unit");
        for(Element ele : eleList){
            PageList pl = new PageList();
            String url = ele.select("a").first().attr("href");
            String title = ele.select("a > h3").first().text();
            pl.setUrl(url);
            pl.setTitle(title);
            apl.add(pl);
        }
        return apl;
    }

    public PageContent processContent(String url){
        PageContent pc = new PageContent();
        try {
            Document doc = Jsoup.connect(url).get();
            Element ele = doc.select("main > article").first();
            pc.setUrl("url");
            pc.setTitle(ele.select("h1.csdn_top").first().text());
            pc.setContent(ele.select("div#article_content").first().text());
        } catch (IOException e){
            e.printStackTrace();
        }
        return pc;
    }
}
