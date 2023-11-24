package com.briup.cms.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.briup.cms.bean.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.briup.cms.bean.Extend.ArticleExtend;
import com.briup.cms.bean.Extend.ArticlePage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    IPage<ArticleExtend> selectArticleExtendByPage(Page<ArticleExtend> page, ArticlePage articlePage);
}
