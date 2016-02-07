package com.eigenmusik.api.messages;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class MessageService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ApplicationContext applicationContext;

    public Map<String, String> getMessages(String locale, String partial) {
        Resource resource = applicationContext.getResource("classpath:messages/" + partial + "/" + locale + ".json");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> jsonMap = new HashMap<>();
        try {
            jsonMap = mapper.readValue(resource.getInputStream(), Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonMap;
    }
}