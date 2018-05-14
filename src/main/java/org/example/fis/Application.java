/*
 * Copyright 2016 Red Hat, Inc.
 * <p>
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */
package org.example.fis;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;
@SpringBootApplication
// load regular Spring XML file from the classpath that contains the Camel XML DSL
@ImportResource({"classpath:spring/camel-context.xml"})
public class Application extends SpringBootServletInitializer {

    /**
     * A main method to start this application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        ServletRegistrationBean servlet = new ServletRegistrationBean(
            new CamelHttpTransportServlet(), "/camel-rest-sql/*");
        servlet.setName("CamelServlet");
        return servlet;
    }

    @Component
    class RestApi extends RouteBuilder {

        @Override
        public void configure() {
            restConfiguration()
                .contextPath("/camel-rest-sql").apiContextPath("/api-doc")
                    .apiProperty("api.title", "Beer Catalog API")
                    .apiProperty("api.version", "1.0")
                    .apiProperty("cors", "true")
                    .apiContextRouteId("doc-api")
                .component("servlet")
                .bindingMode(RestBindingMode.json);

            rest("/beers").description("Beer Catalog API service")
                .get("/").description("The list of all beers")
                    .route().routeId("beers-api")
                    .to("sql:select id,brewery_id,name,cat_id,style_id,abv,ibu,srm,upc,filepath,description from beers?" +
                        "dataSource=dataSource&" +
                        "outputClass=org.example.fis.Beer")
                    .end()
                    .endRest()
                .get("/{id}").description("Details of a beer by id")
                    .route().routeId("beer-api-by-id")                    
                    .toD("sql:select id,brewery_id,name,cat_id,style_id,abv,ibu,srm,upc,filepath,description from beers where id = ${header.id}?" +
                        "dataSource=dataSource&outputType=SelectOne&" +
                        "outputClass=org.example.fis.Beer")    
                    .end()
                    .endRest()            
                .get("/findByName/{name}").description("Details of a beer by name")
                    .route().routeId("beer-api-by-name")                    
                    .toD("sql:select id,brewery_id,name,cat_id,style_id,abv,ibu,srm,upc,filepath,description from beers where name like '%${header.name}%'?" +
                            "dataSource=dataSource&" +
                            "outputClass=org.example.fis.Beer")
                    .end()
                    .endRest()  
                .get("/findByPercentage/{abv}").description("Details of a beer by percentage")
                    .route().routeId("beer-api-by-country")                    
                    .toD("sql:select id,brewery_id,name,cat_id,style_id,abv,ibu,srm,upc,filepath,description from beers where abv >= '${header.abv}'?" +
                            "dataSource=dataSource&" +
                            "outputClass=org.example.fis.Beer")
                    .end()
                    .endRest();                                    
            }
}

}