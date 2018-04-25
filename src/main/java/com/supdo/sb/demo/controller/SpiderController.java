package com.supdo.sb.demo.controller;

import com.supdo.sb.demo.dao.PageListRepository;
import com.supdo.sb.demo.dao.SiteListRepository;
import com.supdo.sb.demo.dao.SpiderRuleRepository;
import com.supdo.sb.demo.entity.PageList;
import com.supdo.sb.demo.entity.SiteList;
import com.supdo.sb.demo.entity.SpiderRule;
import com.supdo.sb.demo.service.BaseService;
import com.supdo.sb.demo.service.SpiderProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PageListRepository pageListRepository;

    @Autowired
    SpiderProcessService spiderProcessService;

    @Autowired
    SpiderRuleRepository spiderRuleRepository;

    @Autowired
    SiteListRepository siteListRepository;

    @GetMapping("/list")
    public String mainView(Map<String, Object> map) {
        //Spider.create(new GithubRepoPageProcessor()).addUrl("https://github.com/code4craft").thread(5).run();
        //Spider.create(githubRepoPageProcessor).addUrl("https://blog.csdn.net/nongshuqiner/").thread(5).run();
        //Spider spider = Spider.create(githubRepoPageProcessor);
        //ResultItems result = spider.get("https://blog.csdn.net/nongshuqiner/");

//        String url = "https://blog.csdn.net/nongshuqiner/";
//        ArrayList<PageList> apl = null;
//        try {
//            Document doc = Jsoup.connect(url).get();
//            apl = spiderProcessService.processList(doc);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        map.put("apl",apl);
        //List pUrls = spiderProcessService.getDistinctPUrl();
        //map.put("pUrls", pUrls);

        LinkedHashMap<String, String> ruleOptions = new LinkedHashMap<>();
        List<SpiderRule> ruleList = spiderRuleRepository.findAllByValid(true);
        for(SpiderRule rule: ruleList){
            ruleOptions.put(rule.getId().toString(), rule.getName());
        }

        SiteList site = new SiteList();
        site.initForm(SiteList.ISiteList.class);
        site.getFields().get("rule").setOptions(ruleOptions);
        map.put("siteForm", site);

        List<SiteList> siteList = siteListRepository.findAll();
        map.put("siteList", siteList);
        return render("/spider/list");
    }

    @PostMapping("/addSite")
    @ResponseBody
    public Result addSite(@Validated({SiteList.ISiteList.class}) SiteList siteForm, BindingResult bindingResult) {
        siteForm.initForm(SiteList.ISiteList.class);
        if(bindingResult.hasErrors()){
            this.result.simple(false, "校验失败！");
            this.result.putItems("site", siteForm.initFieldErrors(bindingResult));
        }else{
            SiteList site = siteListRepository.save(siteForm);
            this.result.simple(true, "保存成功！");
            this.result.putItems("site", site);
        }
        return result;
    }

    @PostMapping("/deleteSite/{id}")
    @ResponseBody
    public Result deleteSite(@PathVariable Long id){
        siteListRepository.delete(id);
        this.result.simple(true, "删除成功！");
        return result;
    }

//    @PostMapping("/AddRule")
//    @ResponseBody
//    public Result addRule(@Validated({SpiderRule.ISpiderRule.class}) SpiderRule ruleForm, BindingResult bindingResult) {
//        ruleForm.initForm(SpiderRule.ISpiderRule.class);
//        if(bindingResult.hasErrors()){
//            this.result.simple(false, "校验失败！");
//            this.result.putItems("rule", ruleForm.initFieldErrors(bindingResult));
//        }else{
////            if(spiderRuleRepository.findByPUrl(ruleForm.getpUrl()).size()>0){
////                this.result.simple(false, "地址已存在！");
////                ruleForm.getFields().get("pUrl").setError("此地址已存在！");
////                this.result.putItems("rule", ruleForm);
////            }else{
////                spiderRuleRepository.save(ruleForm);
////                this.result.simple(true, "保存成功！！");
////            }
//        }
//        return result;
//    }

    @PostMapping("/GetPL")
    @ResponseBody
    public Result getPL(@RequestParam("site") Long siteId, @RequestParam("force") boolean force){
        result.simple(true, "获取成功！");
        BaseService.Result sResult;
        if(force) {
            sResult = spiderProcessService.processSite(siteId);
            result.simple(sResult.isFlag(), sResult.getMsg());
        }
        List<PageList> lpl = pageListRepository.findBySite(siteId);
        result.putItems("lpl", lpl);
        return result;
    }

    @PostMapping("/GetPC/{id}")
    @ResponseBody
    public Result getPCView(@PathVariable Long id){
        BaseService.Result sResult = spiderProcessService.processContent(id);
        result.simple(sResult.isFlag(), sResult.getMsg());
        result.setHtml(((PageList)sResult.getItem("pc")).getContent());
        return result;
    }

    @PostMapping("/PostZootopia/{id}")
    @ResponseBody
    public Result postZootopia(@PathVariable Long id){
        BaseService.Result serviceResult = spiderProcessService.PostZootopia(id);
        if(!serviceResult.isFlag() && serviceResult.getCode()==-101){
            serviceResult = spiderProcessService.PostZootopia(id);
        }
        return result.simple(serviceResult.isFlag(), serviceResult.getMsg());
    }

    @PostMapping("/PostZootopia/all/{siteId}")
    @ResponseBody
    public Result postZootopiaAll(@PathVariable Long siteId){
        List<PageList> lpl = pageListRepository.findBySite(siteId);
        int successCnt = 0;
        for(PageList pl : lpl){
            BaseService.Result grabResult = null;
            BaseService.Result postResult = null;
            if(!pl.isPost()){
                if(!pl.isGrab()){
                    grabResult = spiderProcessService.processContent(pl.getId());
                }
                if(grabResult.isFlag()) {
                    postResult = spiderProcessService.PostZootopia(pl.getId());
                    if(!postResult.isFlag() && postResult.getCode()==-101){
                        postResult = spiderProcessService.PostZootopia(pl.getId());
                    }
                    if(postResult.isFlag()){
                        successCnt += 1;
                    }else{
                        logger.error(String.format("发送失败，URL：%s，报错：%s", pl.getUrl(), postResult.getMsg()));
                    }
                }else{
                    logger.error(String.format("抓取失败，URL：%s，报错：%s", pl.getUrl(), grabResult.getMsg()));
                }

            }
        }
        result.simple(successCnt>0, "成功处理数量：" + successCnt);
        lpl = pageListRepository.findBySite(siteId);
        result.putItems("lpl", lpl);
        return result;
    }
}
