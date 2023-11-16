package com.briup.cms.bean.Extend;

import com.briup.cms.bean.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
@Getter
@Setter//需要有setter方法,在mapper的xml里property才能找到
@Accessors(chain = true)
public class CategoryExtend extends Category {
    List<Category> cates;
}
