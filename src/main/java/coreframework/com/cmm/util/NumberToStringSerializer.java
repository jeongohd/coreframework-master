package coreframework.com.cmm.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * description    :
 * packageName    : coreframework.com.cmm.util
 * fileName       : NumberToStringSerializer
 * author         : yoonsikcha
 * date           : 2022/12/14
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/12/14        yoonsikcha       최초 생성
 */
public class NumberToStringSerializer extends JsonSerializer<Number> {

    @Override
    public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(value == null) gen.writeString("");
        else gen.writeString(value.toString());
    }

}
