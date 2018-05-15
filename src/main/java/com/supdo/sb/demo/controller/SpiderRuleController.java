package com.supdo.sb.demo.controller;

import com.supdo.sb.demo.dao.SpiderRuleRepository;
import com.supdo.sb.demo.entity.SpiderRule;
import com.supdo.sb.demo.plugin.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/spiderRule")
public class SpiderRuleController extends BaseController{

    @Autowired
    SpiderRuleRepository spiderRuleRepository;

    @GetMapping("/list")
    public String listView(Map<String, Object> map) {
        SpiderRule rule = new SpiderRule();
        rule.initForm(SpiderRule.ISpiderRule.class);
        map.put("itemForm", rule);

        List<SpiderRule> ruleList = spiderRuleRepository.findAll();
        map.put("items", ruleList);
        return render("spider/rule");
    }

    @PostMapping("/save")
    @ResponseBody
    public Result save(@Validated({SpiderRule.ISpiderRule.class}) SpiderRule ruleForm, BindingResult bindingResult){
        ruleForm.initForm(SpiderRule.ISpiderRule.class);
        if(bindingResult.hasErrors()){
            this.result.simple(false, "校验失败！");
            this.result.putItems("items", ruleForm.initFieldErrors(bindingResult));
        }else{
            SpiderRule newRule = null;
            if(ruleForm.getId() == null){
                newRule = spiderRuleRepository.save(ruleForm);
                this.result.simple(true, "保存成功！");
            } else {
                SpiderRule oldRule = spiderRuleRepository.findOne(ruleForm.getId());
                oldRule.merge(ruleForm);
                newRule = spiderRuleRepository.save(oldRule);
                this.result.simple(true, "保存成功！");
            }
            this.result.putItems("newItem", newRule);
        }
        return result;
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable Long id){
        spiderRuleRepository.delete(id);
        this.result.simple(true, "删除成功！");
        return  result;
    }
}
