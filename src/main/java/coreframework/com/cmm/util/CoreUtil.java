package coreframework.com.cmm.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * packageName :  coreframework.com.cmm.util
 * fileName : ModelToJson
 * author :  Cha
 * date : 2022/12/30
 * description :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2022/12/30                Cha             최초 생성
 */

public class CoreUtil {

    public  String modelToJson(Object object) throws JsonProcessingException {

        return new ObjectMapper().writeValueAsString(object);
    }
}
