package com.kof.json.api;

import com.kof.json.entity.Dewooo;
import com.kof.json.entity.UserDto;
import com.kof.json.supports.SupportFirstUpperChar;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestJacksonController {

    @RequestMapping("/testJackson")

    public Object  testJacksonM(@RequestBody Dewooo userDto){

        return  userDto;
    }

    @RequestMapping("/testJackson2")

    public Object  testJacksonM2(@RequestBody @SupportFirstUpperChar({String.class,UserDto.class}) UserDto userDto){

        return  userDto;
    }
}
