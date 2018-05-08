package com.supdo.sb.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.supdo.sb.demo.dao.PageListRepository;
import com.supdo.sb.demo.dao.SiteListRepository;
import com.supdo.sb.demo.dao.SpiderRuleRepository;
import com.supdo.sb.demo.entity.PageList;
import com.supdo.sb.demo.entity.SiteList;
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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SpiderProcessService extends BaseService{

    private String myLogin = "quke2";
    private String myPwd = "quke@1256";

    @Autowired
    private MyRedisManager myRedisManager;
    @Autowired
    private SiteListRepository siteListRepository;
    @Autowired
    private PageListRepository pageListRepository;
    @Autowired
    private SpiderRuleRepository spiderRuleRepository;

    Map<String, String> zootopiaTag = new HashMap<>();

    public SpiderProcessService() {
        this.zootopiaTag.put("java", "53");
        this.zootopiaTag.put("python", "57");
    }

    public Result processSite(Long siteId){
        SiteList siteList = siteListRepository.findOne(siteId);
        SpiderRule sr = spiderRuleRepository.findOne(siteList.getRule());
        try {
            URL myUrl = new URL(siteList.getUrl());

            List<String> pages = new ArrayList<String>();
            Document doc = Jsoup.connect(siteList.getUrl()).get();
            if(sr.getPages() != null && sr.getPages().length()>0){
                Elements pageList = doc.select(sr.getPages());
                int cnt = 0;
                for(Element ele : pageList){
                    cnt += 1;
                    String href = ele.attr("href");
                    if(!href.startsWith(myUrl.getProtocol()+"//"+myUrl.getHost())){
                        href = myUrl.getProtocol()+"://"+myUrl.getHost()+href;
                    }
                    pages.add(href);
                    if(cnt >=10) break;
                }
            }
            if(!pages.contains(siteList.getUrl().toString())) {
                pages.add(siteList.getUrl());
            }
            List<PageList> lpl = this.processList(pages, sr, siteList);
            result.simple(true, String.format("获取条数：%s", lpl.size()));
            result.putItem("pageList", lpl);
        } catch (IOException e){
            e.printStackTrace();
            result.simple(false, e.getMessage());
        }
        return result;
    }

    @Transactional
    public List<PageList> processList(List<String> pages, SpiderRule sr, SiteList siteList) throws IOException {
        List<PageList> lpl = new ArrayList<PageList>();
        for(String pageUrl : pages) {
            Document doc = Jsoup.connect(pageUrl).get();
            //提取数据
            Elements eleList = doc.select(sr.getList());
            for (Element ele : eleList) {
                PageList pl = new PageList();
                String url = ele.select(sr.getUrl()).first().attr("href");
                String title = ele.select(sr.getTitle()).first().text();
                if(pageListRepository.findByUrl(url).size() == 0) {
                    pl.setUrl(url);
                    pl.setTitle(title);
                    pl.setSite(siteList.getId());
                    if (pageListRepository.findByUrl(url).size() == 0) {
                        pl = pageListRepository.save(pl);
                    }
                }
                lpl.add(pl);
            }
        }
        return lpl;
    }

    @Transactional
    public Result processContent(Long pageId){
        PageList pc = pageListRepository.findOne(pageId);
        SiteList siteList = siteListRepository.findOne(pc.getSite());
        SpiderRule sr = spiderRuleRepository.findOne(siteList.getRule());
        try {
            Document doc = Jsoup.connect(pc.getUrl()).get();
            Elements contentEle = doc.select(sr.getContent());
            if(contentEle.size()>0){
                pc.setContent(contentEle.first().html());
            }else{
                result.simple(false, "没有获取内容区域");
                return result;
            }
            String content = doc.select(sr.getContent()).first().html();
            pc.setContent(content);
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

    public Map<String, String> LoginZootopia(String login, String password) throws IOException {
        //String loginPageUrl = "http://www.zootopia.unicom.local/u/login/?path=http://www.zootopia.unicom.local/";


        String zootopiaLoginUrl = "http://sso.portal.unicom.local/eip_sso/rest/authentication/login";
        Connection conn = Jsoup.connect(zootopiaLoginUrl);
        conn.data("login", login, "password", password, "appid", "na116",
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

        //获得LtpaToken
        String[] tokens =  ltpa.split(";")[0].split("=");
        //res2.cookie(tokens[0], tokens[1]);
        Map<String, String> zootopiaCookie = new HashMap<>();
        zootopiaCookie.put("LtpaToken", tokens[1]);
        zootopiaCookie.put("topiassid", res2.cookie("topiassid"));
        zootopiaCookie.put("ztunissid", res2.cookie("ztunissid"));

        return zootopiaCookie;
    }

    @Transactional
    public Result PostZootopia(Long id, SiteList site){
        try {
            myRedisManager.init();
            Map<String, String> zootopiaCookie = (Map<String, String>)myRedisManager.get("zootopiaCookie");
            if(zootopiaCookie == null){
                zootopiaCookie = LoginZootopia(myLogin, myPwd);
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
            //shareData.put("tags", "53");
            shareData.put("sections", "");
            String tags = pc.getTags().trim();
            if(tags.length()<1){
                if (site == null) {
                    site = siteListRepository.findOne(pc.getSite());
                }
                shareData.put("tags", site.getTag());
            }else{
                String tagsKey = zootopiaTag.get(tags.toLowerCase());
                if(tagsKey == null){
                    result.simple(false, "未找到Tag的值");
                    return result;
                }else{
                    shareData.put("tags", tagsKey);
                }

            }

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
                    pc.setPostId(shareJson.get("sh_id").toString());
                    pageListRepository.save(pc);
                } else {
                    result.simple(false, (String)shareJson.get("msg"));
                }
            }else {
                result = isLoginZootopia(shareRes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.simple(false, e.getMessage());
        }
        return result;
    }

    public Result getMyShare(int page){
        try {
            myRedisManager.init();
            Map<String, String> zootopiaCookie = (Map<String, String>)myRedisManager.get("zootopiaCookie");
            if(zootopiaCookie == null){
                zootopiaCookie = LoginZootopia(myLogin, myPwd);
                myRedisManager.set("zootopiaCookie", zootopiaCookie);
            }
            String getMyShareUrl = "http://www.zootopia.unicom.local/sharing/api/get_user_share.json";
            Connection conn = Jsoup.connect(getMyShareUrl);
            //user_id=quke2&page_size=10&page=1
            conn.data("user_id", "quke2", "page_size", "50", "page", String.valueOf(page));
            conn.cookies(zootopiaCookie);
            conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            conn.header("Content-Type", "application/x-www-form-urlencoded");
            conn.header("Host", "www.portal.unicom.local");
            conn.header("Origin", "http://www.zootopia.unicom.local");
            conn.header("Referer", "http://http://www.zootopia.unicom.local/");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            Response shareRes = conn.method(Connection.Method.POST).execute();
            String shareBody = shareRes.body();
            if(shareRes.contentType().indexOf("text/javascript;")>-1) {
                JSONObject shareJson = JSON.parseObject(shareBody);
                result.putItem("shareJson", shareJson);
                result.simple(true, "获取分享成功。");
            }else {
                result = isLoginZootopia(shareRes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result.simple(false, e.getMessage());
        }
        return  result;
    }

    private Result isLoginZootopia(Response shareRes) throws IOException {
        String shareBody = shareRes.body();
        Map<String, String> zootopiaCookie = (Map<String, String>)myRedisManager.get("zootopiaCookie");
        if(shareRes.contentType().indexOf("text/html;")>-1){
            //Jsoup.parse(shareBody)
            //未登陆
            if(shareBody.indexOf("/u/login/?path=")>-1){
                zootopiaCookie = LoginZootopia(myLogin, myPwd);
                myRedisManager.set("zootopiaCookie", zootopiaCookie);
                result.simple(false, "会话失效，已重新登陆");
                result.setCode(-101);
            }else{
                result.simple(false, "返回html，尚未处理的情况");
            }
        }else{
            result.simple(false, "尚未处理的情况");
        }
        return result;
    }
}
