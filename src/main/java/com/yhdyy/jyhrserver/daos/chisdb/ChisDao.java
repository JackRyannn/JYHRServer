package com.yhdyy.jyhrserver.daos.chisdb;

import com.yhdyy.jyhrserver.bos.HzjbxxBo;

import java.util.List;

public interface ChisDao {
    List<HzjbxxBo> getHzjbxx(String startTime);
}