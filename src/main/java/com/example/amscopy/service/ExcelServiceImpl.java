package com.example.amscopy.service;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.fastjson.JSONObject;
import com.example.amscopy.exception.AmsErrorCode;
import com.example.amscopy.exception.AmsException;
import com.example.amscopy.model.RiskWarningEventsModel;
import com.example.amscopy.service.api.ExcelService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {

    @Override
    public Object batchImport(InputStream is, Integer orgId, String loginId) {
            RiskWarnEventsExcelListener listener = new RiskWarnEventsExcelListener();
        try {
            HashMap<String, Object> map = new HashMap<>(16);
            map.put("orgId", orgId);
            map.put("loginId", loginId);
            ExcelReader excelReader = new ExcelReader(new BufferedInputStream(is), map, listener);
            excelReader.read(new Sheet(2, 2, RiskWarnEventsExcelInfo.class));
        } catch (Exception e) {
           log.error("excel读取失败...",e);
            return new AmsException(AmsErrorCode.FILE_IS_WRONG.getCode(),AmsErrorCode.FILE_IS_WRONG.getDesc());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONObject json = new JSONObject();
        ThreadLocal<Long> totalRow = listener.getTotalRow();
        List<Object> ignoreData = listener.getIgnoreData().get();
        json.put("totalRow",totalRow);
        if (!CollectionUtils.isEmpty(ignoreData)){
            json.put("ignoreDataCount",ignoreData.size());
            json.put("ignoreData",ignoreData);
        }

        return json;
    }

    @Data
    public class RiskWarnEventsExcelListener extends AnalysisEventListener {
        ThreadLocal<List<Object>> ignoreData = new ThreadLocal<>();
        ThreadLocal<List<RiskWarningEventsModel>> list = new ThreadLocal<>();
        ThreadLocal<Long> totalRow = new ThreadLocal<>();
        private final int batchCount = 1000;

        @Override
        public void invoke(Object o, AnalysisContext analysisContext) {
            init();
            totalRow.set(totalRow.get() + 1);
            Map<String, Object> params = (Map<String, Object>) analysisContext.getCustom();
            RiskWarnEventsExcelInfo excelInfo = (RiskWarnEventsExcelInfo) o;
            if (StringUtils.isBlank(excelInfo.getCompName())) {
                ignoreData.get().add(excelInfo);
                return;
            }
            if (!check(excelInfo.getAnnDt())) {
                ignoreData.get().add(excelInfo);
                return;
            }

            List<RiskWarningEventsModel> models = list.get();
            if (models == null) {
                models = new ArrayList<>();
                list.set(models);
            }
            RiskWarningEventsModel riskWarningEventsModel = new RiskWarningEventsModel();
            BeanUtils.copyProperties(excelInfo, riskWarningEventsModel);
            riskWarningEventsModel.setCreateBy((String) params.get("loginId"));
            models.add(riskWarningEventsModel);
            if (models.size() >= batchCount) {
                //batchInsert();    //如果批量失败，则一条条插入
                models.clear();
            }

        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            //mod 1000 的余数这里处理
            List<RiskWarningEventsModel> models = list.get();
            Map<String, Object> params = (Map<String, Object>) analysisContext.getCustom();
            if (!CollectionUtils.isEmpty(models)) {
                //batchInsert();
                models.clear();
            }
        }

        private void init() {
            List<Object> objects = ignoreData.get();
            if (objects == null) {
                objects = new ArrayList<>();
                ignoreData.set(objects);
            }
            Long aLong = totalRow.get();
            if (aLong == null) {
                totalRow.set(Long.valueOf(0));
            }
        }

        private boolean check(String str) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                sdf.setLenient(false);//指定日期/时间解析是否严格，true不严格，false为严格
                sdf.parse(str);
            } catch (ParseException e) {
                return false;
            }
            return true;
        }
    }

    @Data
    public static class RiskWarnEventsExcelInfo extends BaseRowModel {
        @ExcelProperty(value = "主题名称", index = 0)
        private String compName;
        @ExcelProperty(value = "事件日期", index = 1)
        private String annDt;
        @ExcelProperty(value = "事件描述", index = 2)
        private String content;
    }
}
