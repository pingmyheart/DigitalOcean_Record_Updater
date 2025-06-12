package io.github.pingmyheart.digitaloceanrecordupdater.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.support.CronExpression;

import java.util.List;

@ConfigurationProperties("io.github.pingmyheart.digitaloceanrecordupdater")
@Configuration
@Data
public class DigitalOceanRecordUpdaterProperties {
    private Authentication authentication;
    private List<Project> projects;
    private Scheduling scheduling;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Authentication {
        private String digitalOceanToken;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Project {
        private String domain;
        private List<String> hostnames;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Scheduling {
        @Getter(AccessLevel.NONE)
        private String digitalOceanSync;
        @Getter(AccessLevel.NONE)
        private String publicIpSync;

        @Setter(AccessLevel.NONE)
        private CronExpression digitalOceanSyncCronExpression;
        @Setter(AccessLevel.NONE)
        private CronExpression publicIpSyncCronExpression;

        public void setDigitalOceanSync(String digitalOceanSync) {
            this.digitalOceanSync = digitalOceanSync;
            this.digitalOceanSyncCronExpression = CronExpression.parse(digitalOceanSync);
        }

        public void setPublicIpSync(String publicIpSync) {
            this.publicIpSync = publicIpSync;
            this.publicIpSyncCronExpression = CronExpression.parse(publicIpSync);
        }
    }
}
