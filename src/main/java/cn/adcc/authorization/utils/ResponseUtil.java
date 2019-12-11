package cn.adcc.authorization.utils;

import cn.adcc.authorization.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void response(HttpServletResponse response, Result result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
