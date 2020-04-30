package pet.sankei.union.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 文案配置
 */
@Data
@Component
public class CopyWritingConfig {
    @Value("${copywriting.guideTriggers}")
    private String guideTriggers;

    @Value("${copywriting.welcome}")
    private String welcome;
}
