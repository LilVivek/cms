package com.briup.cms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.briup.cms.bean.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.briup.cms.bean.Extend.ArticleExtend;
import com.briup.cms.bean.Extend.ArticlePage;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
public interface IArticleService extends IService<Article> {

    void insertOrUpdate(Article article);

    void reviewArticle(Long id, String status);

    void deleteByBatch(List<Long> ids);

    ArticleExtend queryById(Long id);

    String getToken();

    IPage<ArticleExtend> queryByPage(ArticlePage articlePage);
}
