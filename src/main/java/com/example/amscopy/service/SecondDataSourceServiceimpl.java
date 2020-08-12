package com.example.amscopy.service;

import com.example.amscopy.constants.AmsConstants;
import com.example.amscopy.model.AbsMonitorCompany;
import com.example.amscopy.service.api.SecondDataSourceService;
import com.example.amscopy.utils.datasources.DataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecondDataSourceServiceimpl implements SecondDataSourceService {

    @Override
    @DataSource(name = AmsConstants.SECOND)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addAbsMonitorCompany(AbsMonitorCompany absMonitorCompany) {
//       TODO  absMonitorCompanyMapper.insert(absMonitorCompany);


        return 0;
    }



}
