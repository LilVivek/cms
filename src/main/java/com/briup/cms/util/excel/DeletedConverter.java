package com.briup.cms.util.excel;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

public class DeletedConverter implements Converter<Integer> {
    @Override
    public Class<?> supportJavaTypeKey() {//映射Java类型
        return Integer.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {//映射excel类型
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {//将单元格数据转换成java数据(context是excel的数据)
        return context.getReadCellData().getStringValue().equals("未被删除") ? 0 : 1;
    }

    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {//将java数据转换成单元格数据(context是java的数据)
        return new WriteCellData<Integer>(context.getValue().equals(0) ? "未被删除" : "被删除");
    }
}
