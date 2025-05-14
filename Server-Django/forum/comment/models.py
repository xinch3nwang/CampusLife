from uuid import uuid4
from django.db import models
from article.models import Article
from account.models import Usr


# Create your models here.
class Comment(models.Model):
    """评论"""
    id = models.UUIDField(verbose_name='编号', primary_key=True, default=uuid4)
    create_time = models.DateTimeField(verbose_name='创建时间', auto_now_add=True)
    content = models.TextField(verbose_name='评论内容')
    article = models.ForeignKey(verbose_name='评论文章', to=Article, on_delete=models.CASCADE)
    user = models.ForeignKey(verbose_name='发表用户', to=Usr, on_delete=models.CASCADE)
    parent = models.ForeignKey(verbose_name='父级评论', to='self', on_delete=models.CASCADE, null=True, blank=True,
                               related_name='comments')

    # parent_top = models.ForeignKey(verbose_name='顶级评论', to='self', on_delete=models.CASCADE, null=True, blank=True,
    # related_name='comment')

    class Meta:
        verbose_name = '评论'
        verbose_name_plural = verbose_name
        ordering = ['create_time']

    def __str__(self):
        return self.content
