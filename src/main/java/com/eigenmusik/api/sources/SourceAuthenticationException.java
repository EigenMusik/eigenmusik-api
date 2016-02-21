package com.eigenmusik.api.sources;

import com.eigenmusik.api.common.ErrorJson;
import com.eigenmusik.api.common.ServiceException;

import java.util.HashMap;
import java.util.Map;

public class SourceAuthenticationException extends ServiceException {

    private SourceType source;

    public SourceAuthenticationException(SourceType source) {
        this.source = source;
    }

    public ErrorJson getError() {
        Map<String, String> params = new HashMap<>();
        params.put("source", source.toString());
        return new ErrorJson("SOURCE_AUTHENTICATION_EXCEPTION", params);

    }
}
