package com.eigenmusik.sources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SourcesService {

    private SourceServiceFactory sourceServiceFactory;

    @Autowired
    public SourcesService(SourceServiceFactory sourceServiceFactory) {
        this.sourceServiceFactory = sourceServiceFactory;
    }

    public List<Source> getSources()
    {
        return Arrays.asList(SourceType.values())
                .stream()
                .map(sourceTypes -> sourceServiceFactory.build(sourceTypes).getSource())
                .collect(Collectors.toList());
    }

}
