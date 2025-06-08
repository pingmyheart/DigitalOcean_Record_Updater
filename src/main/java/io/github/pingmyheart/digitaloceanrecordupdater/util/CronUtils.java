package io.github.pingmyheart.digitaloceanrecordupdater.util;

import com.cronutils.descriptor.CronDescriptor;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.parser.CronParser;
import org.springframework.stereotype.Component;

@Component
public class CronUtils {

    private CronDefinition cronDefinition() {
        return CronDefinitionBuilder.defineCron()
                .withSeconds().and()
                .withMinutes().and()
                .withHours().and()
                .withDayOfMonth().and()
                .withMonth().and()
                .withDayOfWeek()
                .and().instance();
    }

    private CronParser parser() {
        return new CronParser(cronDefinition());
    }

    private CronDescriptor descriptor() {
        return CronDescriptor.instance();
    }

    public String describeCron(String cron) {
        return descriptor().describe(parser().parse(cron));
    }
}
