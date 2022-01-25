package com.example.sendmoneyapi;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private int port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Value("${spring.data.mongodb.authentication-database}")
    private String authDatabase;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create(
                "mongodb+srv://esdy:Ezfs9nJPDcY4BcX@cluster0.6ch8z.mongodb.net/sendmoneyapi?retryWrites=true&w=majority");
    }

    // private MongoClientSettings setMongoClientSettings() {
    // MongoCredential credential = MongoCredential.createCredential(
    // Config.USERNAME, authDatabase, Config.PASSWORD);

    // return MongoClientSettings.builder()
    // .credential(credential)
    // .applyToClusterSettings(builder ->
    // builder.hosts(Arrays.asList(new ServerAddress(host, port))))
    // .build();
    // }
}