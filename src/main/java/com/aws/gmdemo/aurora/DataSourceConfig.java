package com.aws.gmdemo.aurora;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {
    private final Gson gson = new Gson();

    @Bean
    public DataSource datasource() {
        final DbSecret dbCredentials = getSecret();

        return DataSourceBuilder.create()
                .url("jdbc:postgresql://gm-demo-instance-1.cyeqrk4nvmzj.us-east-1.rds.amazonaws.com:5432/postgres")
                .username(dbCredentials.getUsername())
                .password(dbCredentials.getPassword())
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    private DbSecret getSecret() {
        String secretName = "rds!cluster-dbe2ba4d-ade9-45c6-8b5f-2962eee98532";

        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

        String secret = "";
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;  

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);

            if (getSecretValueResult.getSecretString() != null) {
                secret = getSecretValueResult.getSecretString();
            }
        } catch (Exception e) {
            throw e;
        }

        return gson.fromJson(secret, DbSecret.class);
    }
}
