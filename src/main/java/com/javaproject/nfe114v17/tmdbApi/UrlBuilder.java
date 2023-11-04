package com.javaproject.nfe114v17.tmdbApi;

public class UrlBuilder {
    private String apiKey;
    private String baseUrl;
    private String route;
    private StringBuilder params= new StringBuilder();


    public UrlBuilder(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public UrlBuilder route (String route){
        this.route=route;
        return this;
    }
    public UrlBuilder addParam (String name, String value){
        params.append("&"+name+"="+value);
        return this;
    }

    public String build(){
        return baseUrl+route+"?api_key="+apiKey+params.toString();

    }

}
