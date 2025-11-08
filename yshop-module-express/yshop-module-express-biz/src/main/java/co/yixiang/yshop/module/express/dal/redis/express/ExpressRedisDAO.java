package co.yixiang.yshop.module.express.dal.redis.express;

import co.yixiang.yshop.framework.common.util.json.JsonUtils;
import co.yixiang.yshop.framework.tenant.core.context.TenantContextHolder;
import co.yixiang.yshop.module.express.kdniao.model.dto.KdhundredApiBaseDTO;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import static co.yixiang.yshop.module.express.dal.redis.RedisKeyConstants.YSHOP_EXPRESS_CACHE_KEY;


/**
 * {@link KdhundredApiBaseDTO} 的 RedisDAO
 *
 * @author yshop
 */
@Repository
public class ExpressRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public KdhundredApiBaseDTO get() {
        String redisKey = formatKey();
        return JsonUtils.parseObject(stringRedisTemplate.opsForValue().get(redisKey), KdhundredApiBaseDTO.class);
    }

    public void set(KdhundredApiBaseDTO apiBaseDTO) {
        String redisKey = formatKey();
        stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.toJsonString(apiBaseDTO));
    }

    public void delete() {
        String redisKey = formatKey();
        stringRedisTemplate.delete(redisKey);
    }



    private static String formatKey() {
        Long tenantId = TenantContextHolder.getTenantId();
        return String.format(YSHOP_EXPRESS_CACHE_KEY.getKeyTemplate(),tenantId);
    }

}
