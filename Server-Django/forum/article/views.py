import datetime
import json

from django.core import serializers
from django.shortcuts import render

# Create your views here.
from django.shortcuts import get_list_or_404
from django.http import HttpResponse, JsonResponse
from .models import Article
from account.models import Usr, UsrProfile


from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .serializers import ArticleSerializer
from .models import Article

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def article_publish(request):
    """发表文章"""
    if request.method == 'POST':
        title = request.POST.get('title')
        content = request.POST.get('content')
        tagname = request.POST.get('tagname')
        usr_id = request.POST.get('usr_id')

        article = Article.objects.create(title=title, content=content, tag_name=tagname, author_id=usr_id)
        return JsonResponse({
            'code': 200,
            'message': '文章发布成功',
            'article_id': article.id
        })

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def article_update(request):
    """更新文章"""
    if request.method == 'POST':
        article_id = request.POST.get('articleid')
        Article.objects.filter(pk=article_id).update(
            title=request.POST.get('title'),
            content=request.POST.get('content'),
            tag_name=request.POST.get('tagname')
        )
        return JsonResponse({
            'code': 200,
            'message': '文章更新成功',
            'article_id': article_id
        })

@api_view(['POST'])
@permission_classes([IsAuthenticated])
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
    article.readed_count += 1
    article.save()
    return JsonResponse({
        'code': 200,
        'data': {
            'title': article.title,
            'content': article.content,
            'tag_name': article.tag_name,
            'readed_count': article.readed_count,
            'liked_count': article.liked_count,
            'collected_count': article.collected_count
        }
    })

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def article_like(request):
    """喜欢文章"""
    try:
        article_id = request.POST.get('article_id')
        usr_id = request.POST.get('usr_id')
        usrprofile_id = UsrProfile.objects.get(usr_id=usr_id).usr_id
        article = Article.objects.get(pk=article_id)
        
        if article in UsrProfile.objects.get(usr_id=usrprofile_id).articles_liked.all():
            liked_count = -1
            UsrProfile.objects.get(usr_id=usrprofile_id).articles_liked.remove(article)
        else:
            liked_count = 1
            UsrProfile.objects.get(usr_id=usrprofile_id).articles_liked.add(article)

        article.liked_count += liked_count
        article.save()
        usr_profile = UsrProfile.objects.get(usr_id=usrprofile_id)
        usr_profile.article_liked_count += liked_count
        usr_profile.save()

        return JsonResponse({
            'code': 200,
            'liked_count': article.liked_count,
            'is_liked': liked_count == 1
        })
    except Exception as e:
        return JsonResponse({
            'code': 500,
            'error': str(e)
        }, status=500)

@api_view(['POST'])
@permission_classes([IsAuthenticated])
def article_collect(request):
    """收藏文章"""
    try:
        article_id = request.POST.get('article_id')
        usr_id = request.POST.get('usr_id')
        usrprofile_id = UsrProfile.objects.get(usr_id=usr_id).usr_id
        article = Article.objects.get(pk=article_id)
        
        if article in UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.all():
            collected_count = -1
            UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.remove(article)
        else:
            collected_count = 1
            UsrProfile.objects.get(usr_id=usrprofile_id).articles_collected.add(article)

        article.collected_count += collected_count
        article.save()
        usr_profile = UsrProfile.objects.get(usr_id=usrprofile_id)
        usr_profile.article_collected_count += collected_count
        usr_profile.save()

        return JsonResponse({
            'code': 200,
            'collected_count': article.collected_count,
            'is_collected': collected_count == 1
        })
    except Exception as e:
        return JsonResponse({
            'code': 500,
            'error': str(e)
        }, status=500)

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

def articles_date(request):
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

class ArticlePublishAPIView(APIView):
    def post(self, request):
        serializer = ArticleSerializer(data=request.data)
        if serializer.is_valid():
            article = serializer.save()
            return Response({
                'code': status.HTTP_200_OK,
                'message': '文章发布成功',
                'article_id': article.id
            }, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ArticleUpdateAPIView(APIView):
    def post(self, request):
        article_id = request.data.get('articleid')
        try:
            article = Article.objects.get(pk=article_id)
            serializer = ArticleSerializer(article, data=request.data, partial=True)
            if serializer.is_valid():
                serializer.save()
                return Response({
                    'code': status.HTTP_200_OK,
                    'message': '文章更新成功',
                    'article_id': article_id
                }, status=status.HTTP_200_OK)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except Article.DoesNotExist:
            return Response({
                'error': 'Article not found',
                'code': status.HTTP_404_NOT_FOUND
            }, status=status.HTTP_404_NOT_FOUND)
