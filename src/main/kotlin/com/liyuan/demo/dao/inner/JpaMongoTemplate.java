package com.liyuan.demo.dao.inner;

import com.bm001.oldermanagement.config.MongoConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * @author meiyong_zhou
 */
public class JpaMongoTemplate<T> {

    private final Class<T> clazz;

    /**
     * 获取泛型信息 1.5jvm中加入了获取泛型具体信息，seganature字段好像seganature字段好像
     */
    public JpaMongoTemplate() {
        this.clazz = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 查询条件简化
     *
     * @param object
     * @return
     * @throws IllegalAccessException
     */
    public Criteria criteria(Object object) throws IllegalAccessException {
        Criteria c = new Criteria();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object ins = field.get(object);
            if (ins != null) {
                if (ins instanceof String && !"".equals(ins)) {
                    c.and(field.getName()).regex(ins.toString());
                } else if (ins instanceof Integer
                        || ins instanceof Long
                        || ins instanceof Double
                        || ins instanceof Float) {
                    c.and(field.getName()).is(ins);
                }
            }
        }
        return c;
    }

    /**
     * 无分页信息的查询
     *
     * @param condition
     * @param extend
     * @return
     * @throws IllegalAccessException
     */
    public Flux<T> queryGeneral(Object condition, CriteriaExtend extend) throws IllegalAccessException {
        return MongoConfig.mongoTemplate.find(new Query(extend.of(criteria(condition))), clazz);
    }

    /**
     * 查询数量
     *
     * @param condition
     * @param extend
     * @return
     * @throws IllegalAccessException
     */
    public Mono<Long> countGeneral(Object condition, CriteriaExtend extend) throws IllegalAccessException {
        return MongoConfig.mongoTemplate.count(new Query(extend.of(criteria(condition))), clazz);
    }

    /**
     * 查询，排序并且分页
     *
     * @param condition
     * @param sortProperty
     * @param pageRequest
     * @param extend
     * @return
     * @throws IllegalAccessException
     */
    public Flux<T> queryGeneralSortAndPage(Object condition, String sortProperty, PageRequest pageRequest, CriteriaExtend extend) throws IllegalAccessException {
        return MongoConfig.mongoTemplate.find(Query.query(extend.of(criteria(condition))).with(Sort.by(Sort.Direction.DESC, sortProperty)).with(pageRequest), (Class<T>) clazz);
    }

    /**
     * 查询并且分页
     *
     * @param condition
     * @param pageRequest
     * @param extend
     * @return
     * @throws IllegalAccessException
     */
    public Flux<T> queryGeneralPage(Object condition, PageRequest pageRequest, CriteriaExtend extend) throws IllegalAccessException {
        return MongoConfig.mongoTemplate.find(Query.query(extend.of(criteria(condition))).with(pageRequest), clazz);
    }


}


