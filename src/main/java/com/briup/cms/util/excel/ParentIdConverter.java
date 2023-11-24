package com.briup.cms.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.briup.cms.bean.Category;
import com.briup.cms.exception.ServiceException;
import com.briup.cms.service.ICategoryService;
import com.briup.cms.util.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ParentIdConverter implements Converter<Integer> {
    //@Autowired// Spring Bean的初始化在Converter创建之后 所以不能注入
    //ICategoryService iCategoryService;
    public static List<Category> list;

    @Override
    public Class<?> supportJavaTypeKey() {
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
        if (context.getReadCellData().getStringValue() != null) {
            for (Category category : list) {
                if (context.getReadCellData().getStringValue().equals(category.getName())) {//如果找到
                    return category.getId();
                }
            }//如果遍历完了list还没找到parent就抛异常
            //excel中的父栏目名称在数据库中不存在
            throw new ServiceException(ResultCode.PCATEGORY_IS_INVALID);
        }
        return null;//如果父栏目单元格内容为空 直接认定为父栏目，并将父栏目id设置为null
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
        List<Category> list = ParentIdConverter.list.stream()
                .filter(AllOneLevel -> {
                    return AllOneLevel.getId().equals(context.getValue());//查找有没有父栏目的id=当前栏目parentId的记录
                })//加{}代码块就要写return
                .collect(Collectors.toList());
        if (list.size() == 0) {
            return new WriteCellData<>("");
        } else {//只有一个list.size=1 只有一个父栏目 所以列表下标为0的对象就是父栏目
            return new WriteCellData<>(list.get(0).getName());
        }
    }
}
