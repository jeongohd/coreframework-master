package coreframework.layout.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * packageName :  coreframework.layout.web
 * fileName : LayoutController
 * author :  Cha
 * date : 2022/12/12
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/12/12                Cha             최초 생성
 */
@Controller
public class LayoutController {
    @RequestMapping(value="/layout/layout.cm")
    public String sample() throws Exception {
        return "coreframework/layout/LayoutIdx";
    }
}
