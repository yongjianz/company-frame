package com.yz.learn.utils;

import com.github.pagehelper.Page;
import com.yz.learn.vo.resp.PageVO;

import java.util.List;

public class PageUtil {

    public static <T> PageVO<T> getPageVO(List<T> list){
        PageVO<T> pageVO = new PageVO<>();
        if(list instanceof Page){
            Page page = (Page) list;
            pageVO.setCurPageSize(page.size());
            pageVO.setPageSize(page.getPageSize());
            pageVO.setPageNum(page.getPageNum());
            pageVO.setTotalRows(page.getTotal());
            pageVO.setTotalPages(page.getPages());

            pageVO.setList(list);
        }
        return pageVO;
    }
}
