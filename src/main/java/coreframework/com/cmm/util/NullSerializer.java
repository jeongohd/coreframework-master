package coreframework.com.cmm.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * description    :
 * packageName    : coreframework.com.cmm.util
 * fileName       : NullSerializer
 * author         : yoonsikcha
 * date           : 2022/12/14
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/12/14        yoonsikcha       최초 생성
 */
public class NullSerializer extends JsonSerializer<Object> {
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeString("");
    }
}