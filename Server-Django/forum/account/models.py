import json
from uuid import uuid4
from django.db import models


# Create your models here.
class Usr(models.Model):
    # 用户编号
    id = models.UUIDField(primary_key=True, verbose_name='用户编号', auto_created=True, default=uuid4)
    # 登录账号
    username = models.CharField(max_length=50, verbose_name='登录名', unique=True, db_index=True)
    # 登录密码
    password = models.CharField(max_length=50, verbose_name='登录密码')
    # 用户昵称
    nickname = models.CharField(max_length=20, verbose_name='用户昵称', unique=True, null=True, blank=True,
                                db_index=True)
    # 账号注册时间
    create_time = models.DateTimeField(auto_now_add=True, verbose_name='注册时间')
    # 最后修改时间
    update_time = models.DateTimeField(auto_now=True, verbose_name='修改时间')

    class Meta:
        verbose_name = '用户'  # 后台管理系统中的名称
        verbose_name_plural = verbose_name

    def __str__(self):
        return "账号：{};昵称：{}".format(self.username, self.nickname)


class UsrProfile(models.Model):
    GENDER = (
        ('0', '女'),
        ('1', '男'),
    )
    USR_STATUS = {
        ('0', '正常'),
        ('1', '锁定',),
        ('2', '删除'),
    }
    '''用户扩展资料'''
    # 关联用户
    usr = models.OneToOneField(Usr, on_delete=models.CASCADE, primary_key=True, default=None)

    # 年龄
    age = models.IntegerField(default=0, verbose_name='年龄')
    # 性别
    gender = models.CharField(max_length=1, choices=GENDER, verbose_name='性别', null=True, blank=True)
    # 邮箱
    email = models.EmailField(verbose_name='邮箱', null=True, blank=True, db_index=True)
    # 用户状态
    status = models.CharField(max_length=5, choices=USR_STATUS, verbose_name='用户状态', default=0)
    # 个人介绍
    intro = models.TextField(verbose_name='个人介绍', null=True, blank=True)
    # 自己关注的用户
    usrs_liked = models.ManyToManyField('self', related_name='users_you_liked', symmetrical=False, blank=True)
    # 关注自己的用户
    fans = models.ManyToManyField('self', related_name='my_fans', symmetrical=False, blank=True)
    # 资料编号
    id = models.UUIDField(verbose_name='扩展资料编号', auto_created=True, default=uuid4)

    # 粉丝数量
    fans_count = models.IntegerField(verbose_name='粉丝数量', default=0)
    # 关注数量
    usrs_liked_count = models.IntegerField(verbose_name='关注数量', default=0)
    # 访问数量
    visited_count = models.IntegerField(verbose_name='访问次数', default=0)

    # 发文篇数
    article_count = models.IntegerField(verbose_name='文章篇数', default=0)
    # 发文收藏总数量
    article_collected_count = models.IntegerField(verbose_name='收藏总数', default=0)
    # 发文喜欢总数量
    article_liked_count = models.IntegerField(verbose_name='喜欢总数', default=0)
    # 喜欢的文章
    articles_liked = models.ManyToManyField('article.Article', related_name='articleliked')
    # 收藏的文章
    articles_collected = models.ManyToManyField('article.Article', related_name='articlecollected')

    class Meta:
        verbose_name = '用户扩展资料'
        verbose_name_plural = verbose_name

    def __str__(self):
        return self.usr.username+"\n"


