from uuid import uuid4
from django.db import models
from django.urls import reverse


# Create your models here.
class Article(models.Model):
    """文章类型"""
    ARTICLE_STATUS = (
        ('0', '正常'),
        ('1', '删除')
    )
    # 编号
    id = models.UUIDField(verbose_name='文章编号', auto_created=True, default=uuid4, primary_key=True)
    # 标题
    title = models.CharField(verbose_name='文章标题', max_length=200)
    # 内容
    content = models.TextField(verbose_name='文章内容')
    # 发布时间
    pub_time = models.DateTimeField(verbose_name='发布时间', auto_now_add=True)
    # 最后修改时间
    update_time = models.DateTimeField(auto_now=True, verbose_name='修改时间')
    # 阅读次数
    readed_count = models.IntegerField(verbose_name='阅读次数', default=0)
    # 点赞次数
    admired_count = models.IntegerField(verbose_name='点赞次数', default=0)
    # 喜欢次数
    liked_count = models.IntegerField(verbose_name='喜欢次数', default=0)
    # 收藏次数
    collected_count = models.IntegerField(verbose_name='收藏次数', default=0)
    # 评论次数
    commented_count = models.IntegerField(verbose_name='评论次数', default=0)
    # 修改时间
    up_time = models.DateTimeField(verbose_name='上次修改时间', auto_now=True)
    # 操作状态
    status = models.CharField(verbose_name='当前状态', choices=ARTICLE_STATUS, default='0', max_length=2)
    # 文章作者
    author = models.ForeignKey('account.Usr', on_delete=models.CASCADE)
    '''文章自定义标签'''
    # 编号
    # ag_id = models.UUIDField(verbose_name='标签编号', auto_created=True, default=uuid4)
    # 描述
    tag_name = models.CharField(verbose_name='标签标题', max_length=20, default='default')

    # # 文章自定义标签
    # tags = models.ManyToManyField(ArticleTag, related_name='articletags')
    # 私密文章
    # is_secure = models.BooleanField(verbose_name='是否私密文章', default=False)

    class Meta:
        # 数据排序
        ordering = ['-pub_time', 'id']
        # 类型展示提示信息
        verbose_name = '文章'
        verbose_name_plural = verbose_name
        # 添加索引支持
        indexes = [models.Index(fields=['title'])]

    def __str__(self):
        return "title：{}, content：{}\n".format(self.title, self.content)

    # def get_absolute_url(self):
    #     return reverse('article:article_detail', kwargs={'article_id': self.id})
