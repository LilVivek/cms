package com.briup.cms.service.impl;

import com.briup.cms.bean.Article;
import com.briup.cms.mapper.ArticleMapper;
import com.briup.cms.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Vivek
 * @since 2023-11-14
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

}
