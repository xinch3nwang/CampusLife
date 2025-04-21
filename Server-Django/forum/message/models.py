from uuid import uuid4
from django.db import models
from account.models import Usr

# Create your models here.
class Message(models.Model):
    '''私信'''
    content = models.TextField(verbose_name='私信内容')
    sender = models.ForeignKey(verbose_name='发送用户', related_name='sender', to=Usr, on_delete=models.CASCADE)
    receiver = models.ForeignKey(verbose_name='接收用户', related_name='receiver', to=Usr, on_delete=models.CASCADE)
    id = models.UUIDField(verbose_name='编号', primary_key=True, default=uuid4)
    create_time = models.DateTimeField(verbose_name='创建时间', auto_now_add=True)

    class Meta:
        verbose_name = '用户私信'
        verbose_name_plural = verbose_name