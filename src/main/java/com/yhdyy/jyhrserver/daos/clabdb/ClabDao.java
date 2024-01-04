package com.yhdyy.jyhrserver.daos.clabdb;

import com.yhdyy.jyhrserver.bos.JybgBo;
import com.yhdyy.jyhrserver.bos.JybgxqBo;

import java.util.List;

public interface ClabDao {
    List<JybgBo> getJybgBo();
    List<JybgxqBo> getJybgxqBo();
}