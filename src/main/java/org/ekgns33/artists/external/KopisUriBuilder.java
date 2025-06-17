package org.ekgns33.artists.external;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.ekgns33.artists.external.dto.KopisPerformanceRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public final class KopisUriBuilder {

     public static MultiValueMap<String, String> build(KopisPerformanceRequest req) {

        MultiValueMap<String,String> p = new LinkedMultiValueMap<>();
        p.add("stdate", req.getStdate());
        p.add("eddate", req.getEddate());
        p.add("cpage", String.valueOf(req.getCpage()));
        p.add("rows",  String.valueOf(req.getRows()));

        add(p,"shcate",      req.getShcate());
        add(p,"shprfnm",     encode(req.getShprfnm()));
        add(p,"shprfnmfct",  encode(req.getShprfnmfct()));
        add(p,"prfplccd",    req.getPrfplccd());
        add(p,"signgucode",  req.getSigngucode());
        add(p,"signgucodesub",req.getSigngucodesub());
        add(p,"kidstate",    req.getKidstate());
        add(p,"prfstate",    req.getPrfstate());
        add(p,"openrun",     req.getOpenrun());
        add(p,"afterdate",   req.getAfterdate());

        return p;
    }

    private static void add(MultiValueMap<String,String> p,String k,String v){
        if(v!=null&&!v.isBlank()) p.add(k,v.trim());
    }
    private static String encode(String s){
        return (s==null||s.isBlank()) ? null
            : URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    private KopisUriBuilder(){}
}
