package coreframework.com.cmm.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import coreframework.com.cmm.util.NullSerializer;
import coreframework.com.cmm.util.NumberToStringSerializer;

/**
 * description    :
 * packageName    : coreframework.com.cmm.config
 * fileName       : CustomObjectMapper
 * author         : yoonsikcha
 * date           : 2022/12/14
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/12/14        yoonsikcha       최초 생성
 */
public class CustomObjectMapper extends ObjectMapper {


    public CustomObjectMapper(){
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Number.class, new NumberToStringSerializer());
        registerModule(simpleModule);
        getSerializerProvider().setNullValueSerializer(new NullSerializer());
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 없는 필드로 인한 오류 무시
    }
}
