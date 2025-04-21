import datetime
import json

from django.core import serializers
from django.shortcuts import render

# Create your views here.
from django.shortcuts import get_list_or_404
from django.http import HttpResponse, JsonResponse
from .models import Article
from account.models import Usr, UsrProfile


def article_publish(request):
    """发表文章"""
    if request.method == 'POST':
        title = request.POST.get('title')
        content = request.POST.get('content')
        tagname = request.POST.get('tagname')
        usr_id = request.POST.get('usr_id')

        Article.objects.create(title=title, content=content, tag_name=tagname, author_id=usr_id)
        return HttpResponse("ok")
        # return JsonResponse({'code': '200',
        #                      'msg': '文章发布成功'})


def article_update(request):
    """发表文章"""
    if request.method == 'POST':
        title = request.POST.get('title')
        content = request.POST.get('content')
        tagname = request.POST.get('tagname')
        article_id = request.POST.get('articleid')

        Article.objects.filter(pk=article_id).update(title=title, content=content, tag_name=tagname)
        return HttpResponse("ok")


def article_delete(request):
    """删除文章"""
    article_id = request.POST.get('articleid')
    try:
        Article.objects.get(pk=article_id).delete()
        return JsonResponse({'code': '200'})
    except:
        return JsonResponse({'code': '404'})


def article_detail(request):
    """查询指定编号的文章详情"""
    article_id = request.POST.get('articleid')
    article = Article.objects.get(pk=article_id)
    # 更新阅读次数
    article.readed_count += 1
    article.save()
    return HttpResponse(article.readed_count)


def article_like(request):
    """喜欢文章"""
    article_id = request.POST.get('article_id')
    # types = request.POST.get('types')
    usr_id = request.POST.get('usr_id')
    usrprofile_id = UsrProfile.objects.get(usr_id=usr_id).usr_id
    article = Article.objects.get(pk=article_id)
    authorprofile_id = article.author.id

    try:
        liked_count = 1
        UsrProfile.objects.get(usr_id=usrprofile_id).articles_liked.add(article)
    except:
        liked_count = -1
        UsrProfile.objects.get(usr_id=usrprofile_id).articles_liked.remove(article)

    article.liked_count += liked_count
    article.save()
    UsrProfile.objects.get(usr_id=usrprofile_id).article_liked_count += liked_count
    UsrProfile.objects.get(usr_id=usrprofile_id).save()
    # UsrProfile.objects.get(usr_id=authorprofile_id).article_liked_count += liked_count
    # print(UsrProfile.objects.get(usr_id=authorprofile_id).article_liked_count)
    # UsrProfile.objects.get(usr_id=authorprofile_id).save()

    return HttpResponse(article.liked_count)


def article_collect(request):
    """收藏文章"""
    article_id = request.POST.get('article_id')
    # types = request.POST.get('types')
    usr_id = request.POST.get('usr_id')
    usrprofile_id = UsrProfile.objects.get(usr_id=usr_id).usr_id
    article = Article.objects.get(pk=article_id)
    authorprofile_id = article.author_id

    if article not in UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.all():
        collected_count = 1
        UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.add(article)
    else:
        collected_count = -1
        UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.remove(article)

    article.collected_count += collected_count
    article.save()
    UsrProfile.objects.get(usr_id=authorprofile_id).article_collected_count += collected_count
    UsrProfile.objects.get(usr_id=authorprofile_id).save()

    return HttpResponse(article.collected_count)


def articles_list(request):
    """查询所有文章"""
    articles = Article.objects.filter()
    data = serializers.serialize("json", articles)  # str
    list_json = json.loads(data)  # list
    for i in range(len(list_json)):
        list_json[i]['fields']['author'] = articles[i].author.nickname
    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看用户喜欢的所有文章',
                         'result': list_json})


def articles_tag(request):
    """查询所选tag文章"""
    tag = request.POST.get('tagname')
    articles = Article.objects.filter(tag_name=tag)
    data = serializers.serialize("json", articles)  # str
    list_json = json.loads(data)  # list
    for i in range(len(list_json)):
        list_json[i]['fields']['author'] = articles[i].author.nickname
    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看所选tag的所有文章',
                         'result': list_json})


def articles_author(request):
    """查询所选用户文章"""
    authorid = request.POST.get('usr_id')
    articles = Article.objects.filter(author_id=authorid)
    data = serializers.serialize("json", articles)  # str
    list_json = json.loads(data)  # list
    for i in range(len(list_json)):
        list_json[i]['fields']['author'] = articles[i].author.nickname
    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看所选tag的所有文章',
                         'result': list_json})


'''
def articles_usr(request):
    """查询指定用户的所有文章"""
    # 查询得到用户对象
    usr_id = request.POST.get('usr_id')
    try:
        articles = list(Article.objects.filter(author_id=usr_id))
        data = serializers.serialize("json", articles)  # 获取到的数据类型为字符串(str)
        list_json = json.loads(data)  # 将字符串数据转成json类型
        # print(type(list_json))
        result = {
            "result": list_json
        }
        data_json = json.dumps(result, ensure_ascii=False)
        return JsonResponse({'code': '200',
                             'msg': '查看用户喜欢的所有文章',
                             "result": list_json})
        # return HttpResponse(json.dumps(result, ensure_ascii=False), content_type='application/json')
        # return JsonResponse(result, ensure_ascii=False)
    except:
        return JsonResponse({'code': 404})
'''


def articles_date(request):  # todo
    """查询所选时间内所有文章"""
    date = request.POST.get('date')
    articles = Article.objects.filter(pub_time__gte=date)
    data = serializers.serialize("json", articles)  # str
    list_json = json.loads(data)  # list
    for i in range(len(list_json)):
        list_json[i]['fields']['author'] = articles[i].author.nickname
    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看所选tag的所有文章',
                         'result': list_json})


def articles_today(request):
    """查询今日所有文章"""
    today = datetime.datetime.now().date()
    articles = Article.objects.filter(pub_time__gte=today)
    # print(articles.values('author__id'))
    data = serializers.serialize("json", articles)  # str
    list_json = json.loads(data)  # list
    # print(type(list_json))
    # print(type(list_json[0]))
    for i in range(len(list_json)):
        list_json[i]['fields']['author'] = articles[i].author.nickname
    # result = {
    #     "result": list_json
    # }
    # data_json = json.dumps(result, ensure_ascii=False)
    return JsonResponse({'code': '200',
                         'count': len(list_json),
                         'msg': '查看今日的所有文章',
                         'result': list_json})


def articles_year(request):
    """查询指定年份的文章"""
    year = request.POST.get('year')
    articles = get_list_or_404(Article, pub_time__year=year)
    return JsonResponse({'res_code': '200',
                         'res_msg': '查看指定年份的文章',
                         'articles': articles})

# #@gzip_page
# def articles_month(request, year, month):
#     '''查询指定月份的文章；year和month变量会接受从路由中传递的数据'''
#     articles = get_list_or_404(Article, pub_time__year=year, pub_time__month=month)
#     return render(request, 'article/articles.html',
#                   {'res_code': '200',
#                    'res_msg': '查看指定年份和月份的文章',
#                    'articles': articles})


# def article_edit(request, article_id):
#     '''修改文章'''
#     article = Article.objects.get(pk=article_id)
#     if request.method == "GET":
#         article_sources = ArticleSource.objects.all()
#         return render(request, 'article/article_edit.html', {'article': article, 'article_sources': article_sources})
#     elif request.method == "POST":
#         # 获取文章数据
#         atitle = request.POST.get('articletitle')
#         acontent = request.POST.get('articlecontent')
#         asource = request.POST.get('articlesource')
#         atags = request.POST.get('articletags')
#         asubject = request.POST.get('articlesubject')
#         # 查询得到文章类型数据
#         article_source = ArticleSource.objects.get(pk=asource)
#         article_subject = ArticleSubject.objects.get(pk=asubject) if asubject != '-1' else None

#         # 创建并存储文章数据

#         article.title = atitle
#         article.content = acontent
#         article.source = article_source
#         article.subject = article_subject
#         article.save()

#         # 创建文章类型对象并存储数据库
#         try:
#             atags = atags.split(',')
#             for atag in atags:
#                 article_tag = ArticleTag(name=atag)
#                 article_tag.save()
#                 article.tags.add(article_tag)
#         except:
#             atags = None
#         # 发表文章完成，跳转到查看文章详情路由
#         return redirect(article)

# def articles_liked(request):
#     """查询指定用户喜欢的所有文章"""
#     # 查询得到用户对象
#     usr_id = request.POST.get('user_id')
#     # 查询用户喜欢的所有文章
#     articles = UsrProfile.objects.get(id=usr_id).articles_liked.all()
#     return JsonResponse({'code': '200',
#                          'msg': '查看用户喜欢的所有文章',
#                          'articles': articles})


# def articles_collected(request):

# @gzip_page
# def article_like(request, article_id, types):
#     '''喜欢文章'''
#     # 查询要喜欢的文章
#     article = Article.objects.get(pk=article_id)
#     # 查询当前登录的用户
#     usr = request.session['usr']
#     if types == "ADD":
#         # 更新喜欢次数
#         liked_count = 1
#         # 喜欢文章
#         usr.usrprofile.articles_liked.add(article)
#     else:
#         # 更新喜欢次数
#         liked_count = -1
#         # 喜欢文章
#         usr.usrprofile.articles_liked.remove(article)
#     # 更新喜欢次数
#     article.liked_count += liked_count
#     article.save()
#     # 更新文章用户收获喜欢的次数
#     article.usr.usrprofile.liked_count += liked_count
#     article.usr.usrprofile.save()
#     # 跳转到当前文章页面
#     return redirect(article)


# #@gzip_page
# def article_collect(request, article_id, types):
#     '''收藏文章'''
#     # 查询要收藏的文章
#     article = Article.objects.get(pk=article_id)
#     # 查询当前登录的用户
#     usr = request.session['usr']
#     if types == "ADD":
#         # 更新收藏次数
#         collected_count = 1
#         # 收藏文章
#         usr.usrprofile.articles_collected.add(article)
#     else:
#         # 更新收藏次数
#         collected_count = -1
#         # 收藏文章
#         usr.usrprofile.articles_collected.remove(article)

#     # 更新收藏次数
#     article.collected_count += collected_count
#     article.save()
#     # 更新文章用户的收藏次数
#     article.usr.usrprofile.collected_count += collected_count
#     article.usr.usrprofile.save()
#     # 跳转到当前文章页面
#     return redirect(article)
