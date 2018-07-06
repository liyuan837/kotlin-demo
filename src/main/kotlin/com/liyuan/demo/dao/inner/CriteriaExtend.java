package com.liyuan.demo.dao.inner;

import org.springframework.data.mongodb.core.query.Criteria;

@FunctionalInterface
public interface CriteriaExtend {
    Criteria of(Criteria c);
}