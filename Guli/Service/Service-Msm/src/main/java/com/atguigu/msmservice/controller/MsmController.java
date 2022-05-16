package com.atguigu.msmservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.msmservice.service.MsmService;
import com.atguigu.msmservice.utils.RandomUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
@RequestMapping("/edumsm/msm")
public class MsmController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    
    @ApiOperation("发送短信")
    @GetMapping("/send/{phone}")
    public R sendMsm(@PathVariable String phone) {
        String code = RandomUtil.getFourBitRandom();
        Map<String, Object> param = new HashMap<>();
        param.put("code",code);
        boolean isSend = msmService.send(param,phone);
        if (isSend) {
            return R.ok();
        } else {
            return R.error().message("短信发送失败");
        }
    }

    @ApiOperation("发送邮箱")
    @GetMapping("/sendEamil/{email}")
    public R sendEamilCode(@PathVariable String email) {
        // 从redis中获取验证码
        String code = redisTemplate.opsForValue().get(email);
        if (!StringUtils.isEmpty(code)) {
            return R.ok();
        }
        code = msmService.getCode();
        msmService.sendEmail(email,code);
        redisTemplate.opsForValue().set(email,code,5, TimeUnit.MINUTES);
        return R.ok();
    }

}
