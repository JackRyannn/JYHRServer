package com.yhdyy.jyhrserver.client;

import com.dtflys.forest.annotation.*;

import java.util.Map;

public interface NetService {
    @Post("http://11.65.10.136:8085/api/recognitionhospital/cdrDocIndex")
    String batchSavePatient(@JSONBody String body);

}
