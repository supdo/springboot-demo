package com.supdo.sb.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supdo.sb.demo.dao.PageListRepository;
import com.supdo.sb.demo.dao.SpiderRuleRepository;
import com.supdo.sb.demo.entity.PageList;
import com.supdo.sb.demo.entity.SpiderRule;
import com.supdo.sb.demo.plugin.MyRedisManager;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpiderProcessService extends BaseService{

    @Autowired
    private MyRedisManager myRedisManager;
    @Autowired
    private PageListRepository pageListRepository;
    @Autowired
    private SpiderRuleRepository spiderRuleRepository;

    public List getDistinctPUrl(){
        return pageListRepository.findDistinctPUrl();
    }

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

    public ArrayList<PageList> processList(String plUrl){
        ArrayList<PageList> apl = new ArrayList<PageList>();
        List<SpiderRule> lsr = spiderRuleRepository.findByPUrl(plUrl);
        if(lsr.size()==0){
            return apl;
        }
        SpiderRule sr = lsr.get(0);
        try {
            Document doc = Jsoup.connect(plUrl).get();
            //提取数据
            Elements eleList = doc.select(sr.getList());
            for(Element ele : eleList){
                PageList pl = new PageList();
                String url = ele.select(sr.getUrl()).first().attr("href");
                String title = ele.select(sr.getTitle()).first().text();
                pl.setUrl(url);
                pl.setTitle(title);
                pl.setpUrl(plUrl);
                if(pageListRepository.findByUrl(url).size()==0) {
                    pl = pageListRepository.save(pl);
                }
                apl.add(pl);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
//        try {
//            Document doc = Jsoup.connect(plUrl).get();
//            //提取数据
//            Elements eleList = doc.select("ul.blog-units.blog-units-box > li.blog-unit");
//            for(Element ele : eleList){
//                PageList pl = new PageList();
//                String url = ele.select("a").first().attr("href");
//                String title = ele.select("a > h3").first().text();
//                pl.setUrl(url);
//                pl.setTitle(title);
//                pl.setpUrl(plUrl);
//                if(pageListRepository.findByUrl(url).size()==0) {
//                    pl = pageListRepository.save(pl);
//                }
//                apl.add(pl);
//            }
//        } catch (IOException e){
//            e.printStackTrace();
//        }
        return apl;
    }

    public Result processContent(Long id){
        PageList pc = pageListRepository.findOne(id);
        List<SpiderRule> lsr = spiderRuleRepository.findByPUrl(pc.getpUrl());
        if(lsr.size()==0){
            return result.simple(false, "抓取规则未配置");
        }
        SpiderRule sr = lsr.get(0);
        try {
            Document doc = Jsoup.connect(pc.getUrl()).get();
            pc.setContent(doc.select(sr.getContent()).first().html());
//            Element ele = doc.select("main > article").first();
//            pc.setContent(ele.select("div#article_content").first().html());
            pc.setGrab(true);
            pc = pageListRepository.save(pc);
            result.simple(true, "获取成功");
            result.putItem("pc", pc);
        } catch (IOException e){
            e.printStackTrace();
            result.simple(false, e.getMessage());
        }
        return result;
    }

    public Map<String, String> LoginZootopia() throws IOException {
        //String loginPageUrl = "http://www.zootopia.unicom.local/u/login/?path=http://www.zootopia.unicom.local/";


        String zootopiaLoginUrl = "http://sso.portal.unicom.local/eip_sso/rest/authentication/login";
        Connection conn = Jsoup.connect(zootopiaLoginUrl);
        conn.data("login", "quke2", "password", "quke@1256", "appid", "na116",
                "success", "http://www.zootopia.unicom.local/u/login/sso/on_success/",
                "error", "http://www.zootopia.unicom.local/u/login/sso/on_error/",
                "return", "http://www.zootopia.unicom.local/u/login/");
        conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        conn.header("Content-Type", "application/x-www-form-urlencoded");
        conn.header("Host","sso.portal.unicom.local");
        conn.header("Origin", "http://www.zootopia.unicom.local");
        conn.header("Referer", "http://www.zootopia.unicom.local/u/login/form/");
        conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
        Response res = conn.method(Connection.Method.POST).execute();
        String body1 = res.body();
        //JSESSIONID
        String token = Jsoup.parse(body1).select("input[name='token']").first().val();
        String return_str = Jsoup.parse(body1).select("input[name='return']").first().val();
        String ltpa = Jsoup.parse(body1).select("input[name='ltpa']").first().val();

        Map<String, String> successData = new HashMap<>();
        successData.put("token", token);
        successData.put("return", return_str);
        successData.put("ltpa", ltpa);


        String successUrl = "http://www.zootopia.unicom.local/u/login/sso/on_success/";
        Connection conn2 = Jsoup.connect(successUrl);
        conn2.cookie("JSESSIONID", res.cookie("JSESSIONID"));
        conn2.data(successData);
        conn2.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        conn2.header("Content-Type", "application/x-www-form-urlencoded");
        conn2.header("Host","sso.portal.unicom.local");
        conn2.header("Origin", "http://sso.portal.unicom.local");
        conn2.header("Referer", "http://sso.portal.unicom.local/eip_sso/rest/authentication/login");
        conn2.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
        Response res2 = conn2.method(Connection.Method.POST).execute();
        String body2 = res2.body();

//        String homeUrl = "http://www.zootopia.unicom.local/";
//        Connection conn3 = Jsoup.connect(homeUrl);
//        //conn3.cookie("JSESSIONID", res.cookie("JSESSIONID"));
//        conn3.cookie("topiassid", res2.cookie("topiassid"));
//        conn3.cookie("ztunissid", res2.cookie("ztunissid"));
//        conn3.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        conn3.header("Content-Type", "application/x-www-form-urlencoded");
//        conn3.header("Host","www.zootopia.unicom.local");
//        conn3.header("Origin", "http://www.zootopia.unicom.local");
//        conn3.header("Referer", "http://www.zootopia.unicom.local/u/login/form/");
//        conn3.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
//        Response res3 = conn3.method(Connection.Method.GET).execute();
//        String body3 = res3.body();
        //获得LtpaToken
        String[] tokens =  ltpa.split(";")[0].split("=");
        //res2.cookie(tokens[0], tokens[1]);
        Map<String, String> zootopiaCookie = new HashMap<>();
        zootopiaCookie.put("LtpaToken", tokens[1]);
        zootopiaCookie.put("topiassid", res2.cookie("topiassid"));
        zootopiaCookie.put("ztunissid", res2.cookie("ztunissid"));

        return zootopiaCookie;
    }

    public Result PostZootopia(Long id){
        try {
            myRedisManager.init();
            Map<String, String> zootopiaCookie = (Map<String, String>)myRedisManager.get("zootopiaCookie");
            if(zootopiaCookie == null){
                zootopiaCookie = LoginZootopia();
                myRedisManager.set("zootopiaCookie", zootopiaCookie);
            }
            PageList pc =  pageListRepository.findOne(id);
            if(!pc.isGrab()){
                result.simple(false, "尚未抓取内容");
                return result;
            }
            if(pc.isPost()){
                result.simple(false, "已经发送过了");
                return result;
            }
            Map<String, String> shareData = new HashMap<>();
            shareData.put("title", pc.getTitle());
            shareData.put("body", pc.getContent());
            shareData.put("body_parser", "html");
            shareData.put("source", "1");
            shareData.put("draft", "0");
            shareData.put("tags", "53");
            shareData.put("sections", "");

            String shareUrl = "http://www.zootopia.unicom.local/sharing/api/add_share.json";
            Connection conn = Jsoup.connect(shareUrl);
            conn.cookies(zootopiaCookie);
            conn.data(shareData);
            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.header("Content-Type", "application/x-www-form-urlencoded");
            conn.header("Host", "sso.portal.unicom.local");
            conn.header("Origin", "http://www.zootopia.unicom.local");
            conn.header("Referer", "http://http://www.zootopia.unicom.local/share/create");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            Response shareRes = conn.method(Connection.Method.POST).execute();
            String shareBody = shareRes.body();
            if(shareRes.contentType().indexOf("text/javascript;")>-1) {
                JSONObject shareJson = JSON.parseObject(shareBody);
                if ((Boolean) shareJson.get("result")) {
                    result.simple(true, "发送成功");
                    pc.setPost(true);
                    pageListRepository.save(pc);
                } else {
                    result.simple(false, (String)shareJson.get("msg"));
                }
            }else if(shareRes.contentType().indexOf("text/html;")>-1){
                //Jsoup.parse(shareBody)
                //未登陆
                if(shareBody.indexOf("/u/login/?path=")>-1){
                    zootopiaCookie = LoginZootopia();
                    myRedisManager.set("zootopiaCookie", zootopiaCookie);
                    result.simple(false, "会话失效，已重新登陆");
                    result.setCode(-101);
                }else{
                    result.simple(false, "返回html，尚未处理的情况");
                }
            }else{
                result.simple(false, "尚未处理的情况");
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.simple(false, e.getMessage());
        }
        return result;
    }
}
