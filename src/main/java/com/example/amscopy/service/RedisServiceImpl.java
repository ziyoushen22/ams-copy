package com.example.amscopy.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.exception.AmsErrorCode;
import com.example.amscopy.exception.AmsException;
import com.example.amscopy.model.DictModel;
import com.example.amscopy.service.api.DictService;
import com.example.amscopy.service.api.RedisSerVice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisServiceImpl implements RedisSerVice {

    public static final String KEY_PREFIX = "dic:";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final String PROD_SEQ = "prodSeq:";
    public static final String PROD_SEQ_PREFIX = "TD";
    public static final long PROD_MAX_SEQ_NO = 999999;

    @Autowired
    private DictService dictService;

    @Autowired
    private RedisTemplate redisTemplate;

    public String generateSeq(String orgId, String type) {
        List<String> list = batchGenerateSeq(orgId, 1L, type);
        if (list.get(0) == null) {
            throw new AmsException(AmsErrorCode.SEQ_EXHAUST_FAILED.getCode(), AmsErrorCode.SEQ_EXHAUST_FAILED.getDesc());
        } else {
            return list.get(0);
        }
    }


    public List<String> batchGenerateSeq(String orgId, Long counts, String type) {
        if (orgId == null || counts == null || type == null) {
            return null;
        }
        String redisKey;
        Long currentSeqInRedis;
        switch (type) {
            case PROD_SEQ:
                redisKey = generateRedisKey(PROD_SEQ, orgId);
                currentSeqInRedis = getCurrentSeqNoInRedis(redisKey, counts);
                return buildSeq(currentSeqInRedis, orgId, counts, PROD_SEQ_PREFIX, PROD_MAX_SEQ_NO);
            default:
                return null;
        }
    }

    private String generateRedisKey(String prefix, String orgId) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);
        String today = LocalDate.now().format(formatter);
        builder.append(today);
        builder.append(":").append(orgId);
        return builder.toString();
    }

    private List<String> buildSeq(Long currentSeqInRedis, String id, Long counts, String seqPrefix, long maxCount) {
        String seqFormat = "%0" + (maxCount + "").length() + "d";
        List<String> seqList = new ArrayList<>();
        for (long i = 0; i < counts; i++) {
            long var = currentSeqInRedis - counts + i + 1;
            if (var > maxCount) {
                seqList.add(null);
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(seqPrefix);
            String today = LocalDate.now().format(formatter).toString();
            if (id.length() < 4) {
                id = String.format("%04d", Integer.valueOf(id));
            }
            builder.append(today).append(id);
            String currentSeq = String.format(seqFormat, var);
            builder.append(currentSeq);
            seqList.add(builder.toString());
        }
        return seqList;
    }

    private Long getCurrentSeqNoInRedis(String redisKey, long counts) {
        ValueOperations operations = redisTemplate.opsForValue();
        Boolean hasKey = redisTemplate.hasKey(redisKey);
        if (!hasKey) {
            operations.set(redisKey, 0L, 1, TimeUnit.DAYS);
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(String.class));
        Long increment = operations.increment(redisKey, counts);
        return increment;
    }

    @Async
    @Override
    public void loadDictDataToRedis() throws Exception {
        log.info("begin to cache dict ...");
        List<DictModel> dictModels = dictService.selectAll();
        ValueOperations operations = redisTemplate.opsForValue();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(String.class));
        for (DictModel dictModel : dictModels) {
            String dictType = dictModel.getDictType();
            String dictKey = dictModel.getDictKey();
            String dictValue = dictModel.getDictValue();
            String s = KEY_PREFIX + dictType + ":" + dictKey;
            redisTemplate.delete(s);
            operations.set(s, dictValue);
            String s1 = KEY_PREFIX + dictType + ":" + dictValue;
            redisTemplate.delete(s1);
            operations.set(s1, dictKey);
        }
        Map<String, List<DictModel>> dictMap = dictModels.stream().collect(Collectors.groupingBy(DictModel::getDictType));
        Set<Map.Entry<String, List<DictModel>>> entries = dictMap.entrySet();
        for (Map.Entry<String, List<DictModel>> entry : entries) {
            String key = entry.getKey();
            List<DictModel> value = entry.getValue();
            String s = KEY_PREFIX + key;
            redisTemplate.delete(s);
            operations.set(s, convertToJSONArray(value).toJSONString());
        }
        log.info("finish put dict data to redis cache ...");
    }

    private JSONArray convertToJSONArray(List<DictModel> list) {
        JSONArray jsonArray = new JSONArray();
        for (DictModel dictModel : list) {
            JSONObject json = JSON.parseObject(JSON.toJSONString(dictModel));
            jsonArray.add(json);
        }
        return jsonArray;
    }


}
