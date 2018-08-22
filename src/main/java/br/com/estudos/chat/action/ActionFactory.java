package br.com.estudos.chat.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActionFactory {

    @Autowired
    ObjectMapper mapper;


    public Object fromJSON(String json) throws JSONException, IOException {
        JSONObject obj = new JSONObject(json);
        String code = obj.optString("code");
        switch (code)  {
            case "login":
                return mapper.readValue(json, LoginAction.class);
        }
        return null;
    }

}
