package cn.net.yzl.crm.utils;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;

/**
 * @author wangxiao
 * @version 1.0
 * @date 2021/3/9 21:13
 */
public class ExcelStyleUtils {


    /**
     * 生成表头样式
     * @return
     */
    public static HorizontalCellStyleStrategy getHorizontalCellStyleStrategy(){
        // 头部的样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景颜色
        headWriteCellStyle.setFillForegroundColor((short)41);
        //头部字体
        WriteFont headWriteFont = new WriteFont();
        //头部字体大小
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setFontName("Microsoft YaHei Regular");
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        //字体名称
        contentWriteFont.setFontName("Microsoft YaHei Regular");
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle,
                contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }
}
