package com.supdo.sb.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spider")
public class SpiderController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
        LinkedHashMap<String, String> ruleOptions = new LinkedHashMap<>();
        List<SpiderRule> ruleList = spiderRuleRepository.findAllByValid(true);
        for(SpiderRule rule: ruleList){
            ruleOptions.put(rule.getId().toString(), rule.getName());
        }

        SiteList site = new SiteList();
        site.initForm(SiteList.ISiteList.class);
        site.getFields().get("rule").setOptions(ruleOptions);
        map.put("siteForm", site);

        List<SiteList> siteList = siteListRepository.findByValid(true);
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
    public Result postZootopia(Principal principal, @PathVariable Long id){
        BaseService.Result serviceResult = spiderProcessService.PostZootopia(id, null);
        if(!serviceResult.isFlag() && serviceResult.getCode()==-101){
            serviceResult = spiderProcessService.PostZootopia(id, null);
        }
        result.simple(serviceResult.isFlag(), serviceResult.getMsg());
        String toUser = principal.getName();
        simpMessagingTemplate.convertAndSendToUser(toUser, "/oto/notifications", result);
        return result;
    }

    @PostMapping("/PostZootopia/all/{siteId}")
    @ResponseBody
    public Result postZootopiaAll(Principal principal, @PathVariable Long siteId){
        SiteList site = siteListRepository.findOne(siteId);
        List<PageList> lpl = pageListRepository.findBySite(siteId);
        int successCnt = 0;
        String toUser = principal.getName();
        for(PageList pl : lpl){
            Result myResult = new Result(true, "初始化");
            BaseService.Result grabResult = null;
            BaseService.Result postResult = null;
            if(!pl.isPost()){
                if(!pl.isGrab()){
                    grabResult = spiderProcessService.processContent(pl.getId());
                }
                if(grabResult.isFlag()) {
                    postResult = spiderProcessService.PostZootopia(pl.getId(), site);
                    if(!postResult.isFlag() && postResult.getCode()==-101){
                        postResult = spiderProcessService.PostZootopia(pl.getId(), site);
                    }
                    if(postResult.isFlag()){
                        successCnt += 1;
                        myResult.simple(true, String.format("发送成功！标题：%s；URL：%s", pl.getTitle(), pl.getUrl()));
                    }else{
                        String msg = String.format("发送失败，URL：%s，报错：%s", pl.getUrl(), postResult.getMsg());
                        logger.error(msg);
                        myResult.simple(true, msg);
                    }
                }else{
                    String msg = String.format("抓取失败，URL：%s，报错：%s", pl.getUrl(), grabResult.getMsg());
                    logger.error(msg);
                    myResult.simple(true, msg);
                }
                simpMessagingTemplate.convertAndSendToUser(toUser, "/oto/notifications", myResult);
            }
        }
        result.simple(successCnt>0, "成功处理数量：" + successCnt);
        lpl = pageListRepository.findBySite(siteId);
        result.putItems("lpl", lpl);
        return result;
    }

    @GetMapping("/zootopia")
    public String zootopiaView(Map<String, Object> map){
        return render("/spider/zootopia");
    }

    @PostMapping("/getMyShare")
    @ResponseBody
    public Result getMyShare(Principal principal, @RequestParam("page") int page){
        String toUser = principal.getName();
        int newPage = page;
        BaseService.Result sResult = getMyShareByPage(newPage, toUser);
        JSONObject shareJson = (JSONObject) sResult.getItem("shareJson");
        logger.info("{} -- {}, page:{}", format.format((new Date()).getTime()), sResult.getMsg(), newPage);
        while((boolean)shareJson.get("has_more")){
            newPage += 1;
            sResult = getMyShareByPage(newPage, toUser);
            shareJson = (JSONObject) sResult.getItem("shareJson");
            logger.info("{} -- {}, page:{}", format.format((new Date()).getTime()), sResult.getMsg(), newPage);
        }
        return result;
    }

    @Transactional
    BaseService.Result getMyShareByPage(int page, String toUser){
        BaseService.Result sResult = spiderProcessService.getMyShare(page);
        if(!sResult.isFlag() && sResult.getCode()==-101){
            sResult = spiderProcessService.getMyShare(page);
        }
        result.simple(sResult.isFlag(), sResult.getMsg()+"; page:"+String.valueOf(page));
        simpMessagingTemplate.convertAndSendToUser(toUser, "/oto/notifications", result);
        JSONObject shareJson = (JSONObject) sResult.getItem("shareJson");
        JSONArray shares = shareJson.getJSONArray("shares");
        for(int i=0; i<shares.size(); i++){
            JSONObject share = shares.getJSONObject(i);
            int share_id = share.getInteger("sh_id");
            String title = share.getString("title");
            List<PageList> lpl = pageListRepository.findByTitle(title);
            if(lpl.size() > 0 && lpl.get(0).getPostId().length()==0){
                PageList pl = lpl.get(0);
                pl.setPostId(String.valueOf(share_id));
                pageListRepository.save(pl);
                Result myResult = new Result(true, String.format("share_id: %s, title: %s", pl.getPostId(), pl.getTitle()));
                simpMessagingTemplate.convertAndSendToUser(toUser, "/oto/notifications", myResult);
            }
        }
        return sResult;
    }

    @PostMapping("/voteMyShare")
    @ResponseBody
    public Result voteMyShare(Principal principal, @RequestParam("username") String username, @RequestParam("password") String password){
        String toUser = principal.getName();
        List<PageList> lpl = pageListRepository.findByIsPost(true);
        for(PageList pl : lpl){
            BaseService.Result sResult = spiderProcessService.voteShare(pl.getPostId(), username, password);
            if(!sResult.isFlag() && sResult.getCode()==-101){
                sResult = spiderProcessService.voteShare(pl.getPostId(), username, password);
            }
            Result myResult;
            if(sResult.isFlag()) {
                myResult = new Result(sResult.isFlag(), String.format("投票成功！share_id: %s, title: %s", pl.getPostId(), pl.getTitle()));
            }else{
                myResult = new Result(sResult.isFlag(), sResult.getMsg());
                logger.error(sResult.getMsg());
            }

            simpMessagingTemplate.convertAndSendToUser(toUser, "/oto/notifications", myResult);
        }
        return result;
    }

}
