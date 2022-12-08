package com.hallo.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hallo
 * @datetime 2022-10-11 20:46
 * @description
 */
@Data
public class CategoryVo {

    /**
     * 类别Id
     */
    private Integer id;

    /**
     * 父类别id当id=0时说明是根节点,一级类别
     */
    private Integer parentId;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 排序编号,同类展示顺序,数值相等则自然排序
     */
    private Integer sortOrder;

    private List<CategoryVo> subCategories;
}
