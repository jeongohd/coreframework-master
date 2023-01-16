package coreframework.sample.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * packageName :  coreframework.sample.web
 * fileName : SampleController
 * author :  Cha
 * date : 2022/12/12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/12/12                Cha             최초 생성
 */
@Controller
public class SampleController {
    @RequestMapping(value="/sample/sample.cm")
    public String sample() throws Exception {
        return "coreframework/sample/SampleIdx";
    }
}
