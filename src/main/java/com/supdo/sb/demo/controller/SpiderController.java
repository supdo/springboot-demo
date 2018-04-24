package com.supdo.sb.demo.controller;

import com.supdo.sb.demo.dao.PageListRepository;
import com.supdo.sb.demo.dao.SpiderRuleRepository;
import com.supdo.sb.demo.entity.PageList;
import com.supdo.sb.demo.entity.SpiderRule;
import com.supdo.sb.demo.service.BaseService;
import com.supdo.sb.demo.service.SpiderProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {

    @Autowired
    private PageListRepository pageListRepository;

    @Autowired
    SpiderProcessService spiderProcessService;

    @Autowired
    SpiderRuleRepository spiderRuleRepository;

    @GetMapping("/test")
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

        SpiderRule rule = new SpiderRule();
        rule.initForm(SpiderRule.ISpiderRule.class);
        map.put("rule", rule);

        List<SpiderRule> lsr =  spiderRuleRepository.findAll();
        map.put("lsr", lsr);
        return render("/spider/test");
    }

    @PostMapping("/AddRule")
    @ResponseBody
    public Result addRule(@Validated({SpiderRule.ISpiderRule.class}) SpiderRule ruleForm, BindingResult bindingResult) {
        ruleForm.initForm(SpiderRule.ISpiderRule.class);
        if(bindingResult.hasErrors()){
            this.result.simple(false, "校验失败！");
            this.result.putItems("rule", ruleForm.initFieldErrors(bindingResult));
        }else{
            if(spiderRuleRepository.findByPUrl(ruleForm.getpUrl()).size()>0){
                this.result.simple(false, "地址已存在！");
                ruleForm.getFields().get("pUrl").setError("此地址已存在！");
                this.result.putItems("rule", ruleForm);
            }else{
                spiderRuleRepository.save(ruleForm);
                this.result.simple(true, "保存成功！！");
            }
        }
        return result;
    }

    @PostMapping("/GetPL")
    //@RequestMapping("/GetPL")
    @ResponseBody
    public Result getPL(@RequestParam("url") String url, @RequestParam("force") boolean force){
        result.simple(true, "成功！");
        if(force) {
            ArrayList<PageList> apl = spiderProcessService.processList(url);
        }
        List<PageList> lpl = pageListRepository.findByPUrl(url);
        result.putItems("lpl", lpl);
        //List pUrls = spiderProcessService.getDistinctPUrl();
        //result.putItems("pUrls", pUrls);
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

    @PostMapping("/PostZootopia/all")
    @ResponseBody
    public Result postZootopiaAll(@RequestParam("url") String url){
        List<PageList> lpl = pageListRepository.findByPUrl(url);
        int successCnt = 0;
        for(PageList pl : lpl){
            BaseService.Result grabResult;
            BaseService.Result postResult;
            if(!pl.isPost()){
                if(!pl.isGrab()){
                    grabResult = spiderProcessService.processContent(pl.getId());
                }
                postResult = spiderProcessService.PostZootopia(pl.getId());
                if(!postResult.isFlag() && postResult.getCode()==-101){
                    postResult = spiderProcessService.PostZootopia(pl.getId());
                }
                if(postResult.isFlag()){
                    successCnt += 1;
                }
            }
        }
        result.simple(successCnt>0, "成功处理数量：" + successCnt);
        lpl = pageListRepository.findByPUrl(url);
        result.putItems("lpl", lpl);
        return result;
    }
}
